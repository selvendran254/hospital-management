import { medicineService, pharmacyService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import { FormInput, FormSelect } from '@/components/FormInput.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

interface SaleForm {
  medicineId: string
  quantity: number
  unitPrice: number
  saleDate: string
}

export default function SalesPage() {
  const queryClient = useQueryClient()
  const { register, handleSubmit, reset, formState: { errors } } = useForm<SaleForm>()

  const { data: medicines } = useQuery({ queryKey: ['meds-sale'], queryFn: () => medicineService.getAll({ page: 0, size: 100 }) })
  const { data: sales, isLoading } = useQuery({ queryKey: ['sales'], queryFn: () => pharmacyService.getSales({ page: 0, size: 10 }) })

  const mutation = useMutation({
    mutationFn: (data: SaleForm) => pharmacyService.createSale({ ...data, totalAmount: data.quantity * data.unitPrice }),
    onSuccess: () => { reset(); queryClient.invalidateQueries({ queryKey: ['sales'] }) },
  })

  if (isLoading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>

  return (
    <div className="space-y-6">
      <div><h1 className="page-title">Sales</h1><p className="page-subtitle">Record medicine sales</p></div>
      <form onSubmit={handleSubmit((d) => mutation.mutate(d))} className="card space-y-4">
        <FormSelect label="Medicine" required options={(medicines?.content ?? []).map((m) => ({ value: m.id, label: m.name }))} registration={register('medicineId', { required: 'Required' })} error={errors.medicineId} />
        <FormInput label="Quantity" type="number" required registration={register('quantity', { required: 'Required', valueAsNumber: true })} error={errors.quantity} />
        <FormInput label="Unit Price" type="number" required registration={register('unitPrice', { required: 'Required', valueAsNumber: true })} error={errors.unitPrice} />
        <FormInput label="Sale Date" type="date" required registration={register('saleDate', { required: 'Required' })} error={errors.saleDate} />
        <button type="submit" disabled={mutation.isPending} className="btn-primary">Record Sale</button>
      </form>
      <DataTable columns={[
        { key: 'medicineName', header: 'Medicine' }, { key: 'patientName', header: 'Patient' },
        { key: 'quantity', header: 'Qty' }, { key: 'totalAmount', header: 'Total', render: (s) => <>${s.totalAmount}</> },
        { key: 'saleDate', header: 'Date' },
      ]} data={sales?.content ?? []} keyExtractor={(s) => s.id} />
    </div>
  )
}
