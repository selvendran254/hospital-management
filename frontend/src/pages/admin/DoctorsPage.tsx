import { doctorService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import type { Doctor } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function DoctorForm({ onClose, editItem }: { onClose: () => void; editItem?: Doctor }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Doctor>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Doctor>) =>
      editItem ? doctorService.update(editItem.id, data) : doctorService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Specialization" required registration={register('specialization', { required: 'Required' })} error={errors.specialization} />
      <FormInput label="Qualification" registration={register('qualification')} error={errors.qualification} />
      <FormInput label="Experience (years)" type="number" registration={register('experienceYears', { valueAsNumber: true })} error={errors.experienceYears} />
      <FormInput label="Consultation Fee" type="number" registration={register('consultationFee', { valueAsNumber: true })} error={errors.consultationFee} />
      <FormInput label="Phone" registration={register('phone')} error={errors.phone} />
      <FormTextarea label="Bio" registration={register('bio')} error={errors.bio} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function DoctorsPage() {
  return (
    <CrudPage<Doctor>
      title="Doctors"
      queryKey="admin-doctors"
      fetchFn={(params) => doctorService.getAll(params)}
      deleteFn={(id) => doctorService.remove(id)}
      columns={[
        { key: 'fullName', header: 'Name', render: (d) => <>{d.fullName ?? d.specialization}</> },
        { key: 'specialization', header: 'Specialization' },
        { key: 'departmentName', header: 'Department' },
        { key: 'consultationFee', header: 'Fee', render: (d) => <>{d.consultationFee != null ? `$${d.consultationFee}` : '-'}</> },
        { key: 'available', header: 'Available', render: (d) => <>{d.available ? 'Yes' : 'No'}</> },
      ]}
      renderForm={({ onClose, editItem }) => <DoctorForm onClose={onClose} editItem={editItem} />}
    />
  )
}
