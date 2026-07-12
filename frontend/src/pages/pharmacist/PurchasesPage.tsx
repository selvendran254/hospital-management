import { pharmacyService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import { FormInput, FormSelect } from '@/components/FormInput.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { medicineService, supplierService } from '@/api/services/index.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

interface PurchaseForm {
  medicineId: string
  supplierId: string
  quantity: number
  unitPrice: number
  purchaseDate: string
}

export default function PurchasesPage() {
  const queryClient = useQueryClient()
  const { register, handleSubmit, reset, formState: { errors } } = useForm<PurchaseForm>()

  const { data: medicines } = useQuery({ queryKey: ['meds-purchase'], queryFn: () => medicineService.getAll({ page: 0, size: 100 }) })
  const { data: suppliers } = useQuery({ queryKey: ['suppliers-purchase'], queryFn: () => supplierService.getAll({ page: 0, size: 100 }) })
  const { data: purchases, isLoading } = useQuery({ queryKey: ['purchases'], queryFn: () => pharmacyService.getPurchases({ page: 0, size: 10 }) })

  const mutation = useMutation({
    mutationFn: (data: PurchaseForm) => pharmacyService.createPurchase({ ...data, totalAmount: data.quantity * data.unitPrice }),
    onSuccess: () => { reset(); queryClient.invalidateQueries({ queryKey: ['purchases'] }) },
  })

  if (isLoading) return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>

  return (
    <div className="space-y-6">
      <div><h1 className="page-title">Purchases</h1><p className="page-subtitle">Record medicine purchases</p></div>
      <form onSubmit={handleSubmit((d) => mutation.mutate(d))} className="card space-y-4">
        <FormSelect label="Medicine" required options={(medicines?.content ?? []).map((m) => ({ value: m.id, label: m.name }))} registration={register('medicineId', { required: 'Required' })} error={errors.medicineId} />
        <FormSelect label="Supplier" required options={(suppliers?.content ?? []).map((s) => ({ value: s.id, label: s.name }))} registration={register('supplierId', { required: 'Required' })} error={errors.supplierId} />
        <FormInput label="Quantity" type="number" required registration={register('quantity', { required: 'Required', valueAsNumber: true })} error={errors.quantity} />
        <FormInput label="Unit Price" type="number" required registration={register('unitPrice', { required: 'Required', valueAsNumber: true })} error={errors.unitPrice} />
        <FormInput label="Purchase Date" type="date" required registration={register('purchaseDate', { required: 'Required' })} error={errors.purchaseDate} />
        <button type="submit" disabled={mutation.isPending} className="btn-primary">Record Purchase</button>
      </form>
      <DataTable columns={[
        { key: 'medicineName', header: 'Medicine' }, { key: 'supplierName', header: 'Supplier' },
        { key: 'quantity', header: 'Qty' }, { key: 'totalAmount', header: 'Total', render: (p) => <>${p.totalAmount}</> },
        { key: 'purchaseDate', header: 'Date' },
      ]} data={purchases?.content ?? []} keyExtractor={(p) => p.id} />
    </div>
  )
}
