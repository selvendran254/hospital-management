import { billingService, patientService } from '@/api/services/index.ts'
import { FormInput, FormSelect } from '@/components/FormInput.tsx'
import DataTable from '@/components/DataTable.tsx'
import InvoicePreview from '@/components/InvoicePreview.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'
import { useState } from 'react'

interface BillForm {
  patientId: string
  totalAmount: number
  description: string
  gstNumber: string
  gstPercentage: number
}

export default function BillingPage() {
  const queryClient = useQueryClient()
  const [previewBillId, setPreviewBillId] = useState<string | null>(null)
  const { register, handleSubmit, reset, formState: { errors } } = useForm<BillForm>()

  const { data: patients, isLoading: loadingPatients } = useQuery({
    queryKey: ['patients-billing'],
    queryFn: () => patientService.getAll({ page: 0, size: 100 }),
  })

  const { data: bills, isLoading: loadingBills } = useQuery({
    queryKey: ['receptionist-bills'],
    queryFn: () => billingService.getBills({ page: 0, size: 10 }),
  })

  const mutation = useMutation({
    mutationFn: (data: BillForm) =>
      billingService.createBill({
        patientId: data.patientId,
        totalAmount: data.totalAmount,
        description: `${data.description ?? ''} | GST No: ${data.gstNumber || '-'} | GST%: ${data.gstPercentage || 0}`,
        status: 'PENDING',
      }),
    onSuccess: () => {
      reset()
      queryClient.invalidateQueries({ queryKey: ['receptionist-bills'] })
    },
  })

  if (loadingPatients || loadingBills) {
    return <SkeletonLoader rows={10} className="card" />
  }

  const patientOptions = (patients?.content ?? []).map((p) => ({
    value: p.id,
    label: p.fullName ?? p.email ?? p.id,
  }))

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Billing</h1>
        <p className="page-subtitle">Generate and manage patient bills</p>
      </div>
      <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="card space-y-4">
        <FormSelect label="Patient" required options={patientOptions} registration={register('patientId', { required: 'Required' })} error={errors.patientId} />
        <FormInput label="Amount" type="number" required registration={register('totalAmount', { required: 'Required', valueAsNumber: true })} error={errors.totalAmount} />
        <FormInput label="GST Number" registration={register('gstNumber')} error={errors.gstNumber} />
        <FormInput label="GST %" type="number" registration={register('gstPercentage', { valueAsNumber: true })} error={errors.gstPercentage} />
        <FormInput label="Description" registration={register('description')} error={errors.description} />
        <button type="submit" disabled={mutation.isPending} className="btn-primary">Generate Bill</button>
      </form>
      <DataTable
        columns={[
          { key: 'patientName', header: 'Patient' },
          { key: 'totalAmount', header: 'Amount', render: (b) => <>${b.totalAmount}</> },
          { key: 'status', header: 'Status' },
          { key: 'createdAt', header: 'Date', render: (b) => <>{b.createdAt ? new Date(b.createdAt).toLocaleDateString() : '-'}</> },
        ]}
        data={bills?.content ?? []}
        keyExtractor={(b) => b.id}
        actions={(bill) => (
          <button type="button" className="btn-secondary !py-1.5 text-xs" onClick={() => setPreviewBillId(bill.id)}>
            Preview
          </button>
        )}
      />
      {previewBillId && (
        <InvoicePreview
          invoiceNumber={previewBillId}
          patientName={(bills?.content ?? []).find((bill) => bill.id === previewBillId)?.patientName ?? 'Patient'}
          totalAmount={Number((bills?.content ?? []).find((bill) => bill.id === previewBillId)?.totalAmount ?? 0)}
          dueDate={new Date().toLocaleDateString()}
        />
      )}
    </div>
  )
}
