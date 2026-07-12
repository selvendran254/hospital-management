import { FormInput, FormSelect } from '@/components/FormInput.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { useMutation } from '@tanstack/react-query'
import { Heart } from 'lucide-react'
import { useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import type { RegisterPatientRequest } from '@/types/index.ts'

const genderOptions = [
  { value: 'MALE', label: 'Male' },
  { value: 'FEMALE', label: 'Female' },
  { value: 'OTHER', label: 'Other' },
]

const bloodGroupOptions = [
  { value: 'A_POSITIVE', label: 'A+' },
  { value: 'A_NEGATIVE', label: 'A-' },
  { value: 'B_POSITIVE', label: 'B+' },
  { value: 'B_NEGATIVE', label: 'B-' },
  { value: 'AB_POSITIVE', label: 'AB+' },
  { value: 'AB_NEGATIVE', label: 'AB-' },
  { value: 'O_POSITIVE', label: 'O+' },
  { value: 'O_NEGATIVE', label: 'O-' },
]

export default function Register() {
  const { register: registerUser } = useAuth()
  const navigate = useNavigate()
  const { register, handleSubmit, formState: { errors } } = useForm<RegisterPatientRequest>()

  const mutation = useMutation({
    mutationFn: (data: RegisterPatientRequest) => registerUser(data),
    onSuccess: (path) => navigate(path, { replace: true }),
  })

  return (
    <div className="flex min-h-[70vh] items-center justify-center px-4 py-12">
      <div className="w-full max-w-lg">
        <div className="text-center">
          <Heart className="mx-auto h-10 w-10 fill-primary-600 text-primary-600" />
          <h1 className="mt-4 text-2xl font-bold text-slate-900 dark:text-white">Create Account</h1>
          <p className="mt-1 text-sm text-slate-500">Register as a patient at MediCare Hospital</p>
        </div>
        <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="mt-8 card space-y-4">
          <div className="grid gap-4 sm:grid-cols-2">
            <FormInput label="First Name" required registration={register('firstName', { required: 'Required' })} error={errors.firstName} />
            <FormInput label="Last Name" required registration={register('lastName', { required: 'Required' })} error={errors.lastName} />
          </div>
          <FormInput label="Email" type="email" required registration={register('email', { required: 'Required' })} error={errors.email} />
          <FormInput label="Password" type="password" required registration={register('password', { required: 'Required', minLength: { value: 6, message: 'Min 6 characters' } })} error={errors.password} />
          <FormInput label="Phone" registration={register('phone')} error={errors.phone} />
          <FormInput label="Date of Birth" type="date" registration={register('dateOfBirth')} error={errors.dateOfBirth} />
          <div className="grid gap-4 sm:grid-cols-2">
            <FormSelect label="Gender" options={genderOptions} registration={register('gender')} error={errors.gender} />
            <FormSelect label="Blood Group" options={bloodGroupOptions} registration={register('bloodGroup')} error={errors.bloodGroup} />
          </div>
          <FormInput label="Address" registration={register('address')} error={errors.address} />
          <FormInput label="Emergency Contact" registration={register('emergencyContact')} error={errors.emergencyContact} />
          <button type="submit" disabled={mutation.isPending} className="btn-primary w-full">
            {mutation.isPending ? 'Creating account...' : 'Register'}
          </button>
          {mutation.isError && <p className="text-sm text-red-500">Registration failed. Email may already be in use.</p>}
          <p className="text-center text-sm text-slate-500">
            Already have an account? <Link to="/login" className="text-primary-600 hover:underline">Sign in</Link>
          </p>
        </form>
      </div>
    </div>
  )
}
