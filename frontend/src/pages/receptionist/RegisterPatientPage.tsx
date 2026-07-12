import { patientService } from '@/api/services/index.ts'
import { FormInput, FormSelect } from '@/components/FormInput.tsx'
import type { Patient } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

const genderOptions = [
  { value: 'MALE', label: 'Male' },
  { value: 'FEMALE', label: 'Female' },
  { value: 'OTHER', label: 'Other' },
]

export default function RegisterPatientPage() {
  const { register, handleSubmit, reset, formState: { errors } } = useForm<Partial<Patient>>()

  const mutation = useMutation({
    mutationFn: (data: Partial<Patient>) => patientService.register(data),
    onSuccess: () => reset(),
  })

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <div>
        <h1 className="page-title">Register Patient</h1>
        <p className="page-subtitle">Create a new patient record</p>
      </div>
      <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="card space-y-4">
        <div className="grid gap-4 sm:grid-cols-2">
          <FormInput label="First Name" required registration={register('firstName', { required: 'Required' })} error={errors.firstName} />
          <FormInput label="Last Name" required registration={register('lastName', { required: 'Required' })} error={errors.lastName} />
        </div>
        <FormInput label="Email" type="email" registration={register('email')} error={errors.email} />
        <FormInput label="Phone" registration={register('phone')} error={errors.phone} />
        <FormInput label="Date of Birth" type="date" registration={register('dateOfBirth')} error={errors.dateOfBirth} />
        <FormSelect label="Gender" options={genderOptions} registration={register('gender')} error={errors.gender} />
        <FormInput label="Address" registration={register('address')} error={errors.address} />
        <FormInput label="Emergency Contact" registration={register('emergencyContact')} error={errors.emergencyContact} />
        <button type="submit" disabled={mutation.isPending} className="btn-primary w-full">
          {mutation.isPending ? 'Registering...' : 'Register Patient'}
        </button>
        {mutation.isSuccess && <p className="text-sm text-emerald-600">Patient registered successfully!</p>}
        {mutation.isError && <p className="text-sm text-red-500">Registration failed. Please try again.</p>}
      </form>
    </div>
  )
}
