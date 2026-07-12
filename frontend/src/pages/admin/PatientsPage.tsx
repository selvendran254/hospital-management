import { patientService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput } from '@/components/FormInput.tsx'
import type { Patient } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function PatientForm({ onClose, editItem }: { onClose: () => void; editItem?: Patient }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Patient>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Patient>) =>
      editItem ? patientService.update(editItem.id, data) : patientService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="First Name" registration={register('firstName')} error={errors.firstName} />
      <FormInput label="Last Name" registration={register('lastName')} error={errors.lastName} />
      <FormInput label="Phone" registration={register('phone')} error={errors.phone} />
      <FormInput label="Email" registration={register('email')} error={errors.email} />
      <FormInput label="Address" registration={register('address')} error={errors.address} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function PatientsPage() {
  return (
    <CrudPage<Patient>
      title="Patients"
      queryKey="admin-patients"
      fetchFn={(params) => patientService.getAll(params)}
      deleteFn={(id) => patientService.remove(id)}
      columns={[
        { key: 'fullName', header: 'Name' },
        { key: 'email', header: 'Email' },
        { key: 'phone', header: 'Phone' },
        { key: 'bloodGroup', header: 'Blood Group' },
        { key: 'mrn', header: 'MRN' },
      ]}
      renderForm={({ onClose, editItem }) => <PatientForm onClose={onClose} editItem={editItem} />}
    />
  )
}
