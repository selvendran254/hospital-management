import { medicineService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput } from '@/components/FormInput.tsx'
import type { Medicine } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function MedicineForm({ onClose, editItem }: { onClose: () => void; editItem?: Medicine }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Medicine>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Medicine>) =>
      editItem ? medicineService.update(editItem.id, data) : medicineService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Name" required registration={register('name', { required: 'Required' })} error={errors.name} />
      <FormInput label="Generic Name" registration={register('genericName')} error={errors.genericName} />
      <FormInput label="Category" registration={register('category')} error={errors.category} />
      <FormInput label="Unit Price" type="number" required registration={register('unitPrice', { required: 'Required', valueAsNumber: true })} error={errors.unitPrice} />
      <FormInput label="Stock Quantity" type="number" registration={register('stockQuantity', { valueAsNumber: true })} error={errors.stockQuantity} />
      <FormInput label="Reorder Level" type="number" registration={register('reorderLevel', { valueAsNumber: true })} error={errors.reorderLevel} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function MedicinesPage() {
  return (
    <CrudPage<Medicine>
      title="Medicines"
      queryKey="admin-medicines"
      fetchFn={(params) => medicineService.getAll(params)}
      deleteFn={(id) => medicineService.remove(id)}
      columns={[
        { key: 'name', header: 'Name' },
        { key: 'category', header: 'Category' },
        { key: 'unitPrice', header: 'Price', render: (m) => <>${m.unitPrice}</> },
        { key: 'stockQuantity', header: 'Stock' },
      ]}
      renderForm={({ onClose, editItem }) => <MedicineForm onClose={onClose} editItem={editItem} />}
    />
  )
}
