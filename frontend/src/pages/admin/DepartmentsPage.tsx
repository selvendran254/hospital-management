import { departmentService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import type { Department } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function DepartmentForm({ onClose, editItem }: { onClose: () => void; editItem?: Department }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Department>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Department>) =>
      editItem ? departmentService.update(editItem.id, data) : departmentService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Name" required registration={register('name', { required: 'Required' })} error={errors.name} />
      <FormTextarea label="Description" registration={register('description')} error={errors.description} />
      <FormInput label="Floor Number" type="number" registration={register('floorNumber', { valueAsNumber: true })} error={errors.floorNumber} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function DepartmentsPage() {
  return (
    <CrudPage<Department>
      title="Departments"
      queryKey="admin-departments"
      fetchFn={(params) => departmentService.getAll(params)}
      deleteFn={(id) => departmentService.remove(id)}
      columns={[
        { key: 'name', header: 'Name' },
        { key: 'description', header: 'Description' },
        { key: 'floorNumber', header: 'Floor' },
        { key: 'headDoctorName', header: 'Head Doctor' },
      ]}
      renderForm={({ onClose, editItem }) => <DepartmentForm onClose={onClose} editItem={editItem} />}
    />
  )
}
