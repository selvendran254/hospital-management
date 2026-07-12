import { supplierService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput } from '@/components/FormInput.tsx'
import type { Supplier } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function SupplierForm({ onClose, editItem }: { onClose: () => void; editItem?: Supplier }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Supplier>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Supplier>) =>
      editItem ? supplierService.update(editItem.id, data) : supplierService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Name" required registration={register('name', { required: 'Required' })} error={errors.name} />
      <FormInput label="Contact Person" registration={register('contactPerson')} error={errors.contactPerson} />
      <FormInput label="Email" registration={register('email')} error={errors.email} />
      <FormInput label="Phone" registration={register('phone')} error={errors.phone} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function SuppliersPage() {
  return (
    <CrudPage<Supplier>
      title="Suppliers"
      queryKey="pharmacist-suppliers"
      fetchFn={(params) => supplierService.getAll(params)}
      deleteFn={(id) => supplierService.remove(id)}
      columns={[
        { key: 'name', header: 'Name' },
        { key: 'contactPerson', header: 'Contact' },
        { key: 'email', header: 'Email' },
        { key: 'phone', header: 'Phone' },
      ]}
      renderForm={({ onClose, editItem }) => <SupplierForm onClose={onClose} editItem={editItem} />}
    />
  )
}
