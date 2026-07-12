import { contactService } from '@/api/services/index.ts'
import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import { useMutation } from '@tanstack/react-query'
import { Mail, MapPin, Phone } from 'lucide-react'
import { useForm } from 'react-hook-form'

interface ContactForm {
  name: string
  email: string
  phone: string
  subject: string
  message: string
}

export default function Contact() {
  const { register, handleSubmit, reset, formState: { errors } } = useForm<ContactForm>()

  const mutation = useMutation({
    mutationFn: (data: ContactForm) => contactService.send(data),
    onSuccess: () => reset(),
  })

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Contact Us</h1>
      <p className="page-subtitle">We&apos;re here to help. Reach out to us anytime.</p>

      <div className="mt-8 grid gap-8 lg:grid-cols-3">
        <div className="space-y-4">
          <div className="card flex items-start gap-4">
            <MapPin className="mt-1 h-5 w-5 shrink-0 text-primary-600" />
            <div>
              <h3 className="font-semibold">Address</h3>
              <p className="mt-1 text-sm text-slate-500">123 Healthcare Ave, Medical District, City 12345</p>
            </div>
          </div>
          <div className="card flex items-start gap-4">
            <Phone className="mt-1 h-5 w-5 shrink-0 text-primary-600" />
            <div>
              <h3 className="font-semibold">Phone</h3>
              <p className="mt-1 text-sm text-slate-500">+1 (555) 123-4567</p>
            </div>
          </div>
          <div className="card flex items-start gap-4">
            <Mail className="mt-1 h-5 w-5 shrink-0 text-primary-600" />
            <div>
              <h3 className="font-semibold">Email</h3>
              <p className="mt-1 text-sm text-slate-500">info@medicare-hospital.com</p>
            </div>
          </div>
        </div>

        <form
          onSubmit={handleSubmit((data) => mutation.mutate(data))}
          className="card space-y-4 lg:col-span-2"
        >
          <div className="grid gap-4 sm:grid-cols-2">
            <FormInput label="Name" required registration={register('name', { required: 'Name is required' })} error={errors.name} />
            <FormInput label="Email" type="email" required registration={register('email', { required: 'Email is required' })} error={errors.email} />
          </div>
          <div className="grid gap-4 sm:grid-cols-2">
            <FormInput label="Phone" registration={register('phone')} error={errors.phone} />
            <FormInput label="Subject" required registration={register('subject', { required: 'Subject is required' })} error={errors.subject} />
          </div>
          <FormTextarea label="Message" required registration={register('message', { required: 'Message is required' })} error={errors.message} rows={5} />
          <button type="submit" disabled={mutation.isPending} className="btn-primary">
            {mutation.isPending ? 'Sending...' : 'Send Message'}
          </button>
          {mutation.isSuccess && <p className="text-sm text-emerald-600">Message sent successfully!</p>}
          {mutation.isError && <p className="text-sm text-red-500">Failed to send message. Please try again.</p>}
        </form>
      </div>
    </div>
  )
}
