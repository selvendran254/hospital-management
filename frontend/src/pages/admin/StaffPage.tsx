import { staffService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput } from '@/components/FormInput.tsx'
import type { Staff } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function StaffForm({ onClose, editItem }: { onClose: () => void; editItem?: Staff }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Staff>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Staff>) =>
      editItem ? staffService.update(editItem.id, data) : staffService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Designation" required registration={register('designation', { required: 'Required' })} error={errors.designation} />
      <FormInput label="Employee ID" registration={register('employeeId')} error={errors.employeeId} />
      <FormInput label="Joining Date" type="date" registration={register('joiningDate')} error={errors.joiningDate} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function StaffPage() {
  return (
    <CrudPage<Staff>
      title="Staff"
      queryKey="admin-staff"
      fetchFn={(params) => staffService.getAll(params)}
      deleteFn={(id) => staffService.remove(id)}
      columns={[
        { key: 'fullName', header: 'Name' },
        { key: 'designation', header: 'Designation' },
        { key: 'departmentName', header: 'Department' },
        { key: 'employeeId', header: 'Employee ID' },
      ]}
      renderForm={({ onClose, editItem }) => <StaffForm onClose={onClose} editItem={editItem} />}
    />
  )
}
