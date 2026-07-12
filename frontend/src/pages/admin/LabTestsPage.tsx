import { labService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import type { LabTest } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function LabTestForm({ onClose, editItem }: { onClose: () => void; editItem?: LabTest }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<LabTest>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<LabTest>) =>
      editItem ? labService.updateTest(editItem.id, data) : labService.createTest(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Test Name" required registration={register('name', { required: 'Required' })} error={errors.name} />
      <FormInput label="Code" registration={register('code')} error={errors.code} />
      <FormInput label="Price" type="number" required registration={register('price', { required: 'Required', valueAsNumber: true })} error={errors.price} />
      <FormTextarea label="Description" registration={register('description')} error={errors.description} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function LabTestsPage() {
  return (
    <CrudPage<LabTest>
      title="Lab Tests"
      queryKey="admin-lab-tests"
      fetchFn={(params) => labService.getTests(params)}
      deleteFn={(id) => labService.removeTest(id)}
      columns={[
        { key: 'name', header: 'Name' },
        { key: 'code', header: 'Code' },
        { key: 'price', header: 'Price', render: (t) => <>${t.price}</> },
      ]}
      renderForm={({ onClose, editItem }) => <LabTestForm onClose={onClose} editItem={editItem} />}
    />
  )
}
