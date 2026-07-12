import { FormInput } from '@/components/FormInput.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { useMutation } from '@tanstack/react-query'
import { Heart } from 'lucide-react'
import { useForm } from 'react-hook-form'
import { Link, useLocation, useNavigate } from 'react-router-dom'

interface LoginForm {
  email: string
  password: string
}

export default function Login() {
  const { login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const from = (location.state as { from?: { pathname: string } })?.from?.pathname

  const { register, handleSubmit, formState: { errors } } = useForm<LoginForm>()

  const mutation = useMutation({
    mutationFn: async (data: LoginForm) => {
      const path = await login(data)
      return path
    },
    onSuccess: (path) => navigate(from ?? path, { replace: true }),
  })

  return (
    <div className="flex min-h-[70vh] items-center justify-center px-4 py-12">
      <div className="w-full max-w-md">
        <div className="text-center">
          <Heart className="mx-auto h-10 w-10 fill-primary-600 text-primary-600" />
          <h1 className="mt-4 text-2xl font-bold text-slate-900 dark:text-white">Welcome Back</h1>
          <p className="mt-1 text-sm text-slate-500">Sign in to your MediCare account</p>
        </div>
        <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="mt-8 card space-y-4">
          <FormInput label="Email" type="email" required registration={register('email', { required: 'Email is required' })} error={errors.email} />
          <FormInput label="Password" type="password" required registration={register('password', { required: 'Password is required' })} error={errors.password} />
          <div className="flex justify-end">
            <Link to="/forgot-password" className="text-sm text-primary-600 hover:underline">Forgot password?</Link>
          </div>
          <button type="submit" disabled={mutation.isPending} className="btn-primary w-full">
            {mutation.isPending ? 'Signing in...' : 'Sign In'}
          </button>
          {mutation.isError && <p className="text-sm text-red-500">Invalid email or password.</p>}
          <p className="text-center text-sm text-slate-500">
            Don&apos;t have an account? <Link to="/register" className="text-primary-600 hover:underline">Register</Link>
          </p>
        </form>
      </div>
    </div>
  )
}
