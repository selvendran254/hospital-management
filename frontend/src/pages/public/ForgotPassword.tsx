import { authService } from '@/api/services/index.ts'
import { FormInput } from '@/components/FormInput.tsx'
import { useMutation } from '@tanstack/react-query'
import { ArrowLeft } from 'lucide-react'
import { useForm } from 'react-hook-form'
import { Link } from 'react-router-dom'

interface ForgotForm {
  email: string
}

export default function ForgotPassword() {
  const { register, handleSubmit, formState: { errors } } = useForm<ForgotForm>()

  const mutation = useMutation({
    mutationFn: (data: ForgotForm) => authService.forgotPassword(data),
  })

  return (
    <div className="flex min-h-[70vh] items-center justify-center px-4 py-12">
      <div className="w-full max-w-md">
        <Link to="/login" className="inline-flex items-center gap-2 text-sm text-primary-600 hover:underline">
          <ArrowLeft className="h-4 w-4" />
          Back to login
        </Link>
        <h1 className="mt-6 text-2xl font-bold text-slate-900 dark:text-white">Forgot Password</h1>
        <p className="mt-1 text-sm text-slate-500">Enter your email and we&apos;ll send reset instructions.</p>
        <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="mt-8 card space-y-4">
          <FormInput label="Email" type="email" required registration={register('email', { required: 'Email is required' })} error={errors.email} />
          <button type="submit" disabled={mutation.isPending} className="btn-primary w-full">
            {mutation.isPending ? 'Sending...' : 'Send Reset Link'}
          </button>
          {mutation.isSuccess && (
            <p className="text-sm text-emerald-600">If an account exists, reset instructions have been sent.</p>
          )}
          {mutation.isError && <p className="text-sm text-red-500">Something went wrong. Please try again.</p>}
        </form>
      </div>
    </div>
  )
}
