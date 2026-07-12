import { appointmentService, doctorService } from '@/api/services/index.ts'
import DoctorCalendar from '@/components/DoctorCalendar.tsx'
import { FormInput, FormSelect, FormTextarea } from '@/components/FormInput.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { useMutation, useQuery } from '@tanstack/react-query'
import { Calendar } from 'lucide-react'
import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { Link, useLocation, useNavigate } from 'react-router-dom'

interface BookingForm {
  doctorId: string
  appointmentDate: string
  startTime: string
  reason: string
  isVideoConsultation: boolean
  recurrence: string
}

export default function AppointmentBooking() {
  const { isAuthenticated } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const preselectedDoctor = (location.state as { doctorId?: string })?.doctorId
  const [selectedSlot, setSelectedSlot] = useState('')

  const { data: doctorsData, isLoading: loadingDoctors } = useQuery({
    queryKey: ['doctors', 'booking'],
    queryFn: () => doctorService.getAll({ page: 0, size: 100 }),
  })

  const { register, handleSubmit, setValue, formState: { errors } } = useForm<BookingForm>({
    defaultValues: { doctorId: preselectedDoctor ?? '', isVideoConsultation: false, recurrence: 'NONE' },
  })

  const mutation = useMutation({
    mutationFn: (data: BookingForm) =>
      appointmentService.create({
        doctorId: data.doctorId,
        appointmentDate: data.appointmentDate,
        startTime: data.startTime,
        endTime: data.startTime,
        reason: `${data.reason ?? ''}${data.isVideoConsultation ? ' | Video consultation' : ''}${data.recurrence !== 'NONE' ? ` | Recurrence: ${data.recurrence}` : ''}`,
        status: 'PENDING',
      }),
    onSuccess: () => navigate(isAuthenticated ? '/patient/appointments' : '/login'),
  })

  const doctorOptions = (doctorsData?.content ?? []).map((d) => ({
    value: d.id,
    label: `${d.fullName ?? d.specialization} - ${d.specialization}`,
  }))

  if (loadingDoctors) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-2xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Book an Appointment</h1>
      <p className="page-subtitle">Schedule a visit with one of our specialists</p>

      {!isAuthenticated && (
        <div className="mt-4 rounded-lg border border-amber-200 bg-amber-50 p-4 text-sm text-amber-800 dark:border-amber-800 dark:bg-amber-950 dark:text-amber-200">
          Please <Link to="/login" className="font-medium underline">login</Link> or{' '}
          <Link to="/register" className="font-medium underline">register</Link> to book an appointment.
        </div>
      )}

      <form
        onSubmit={handleSubmit((data) => mutation.mutate(data))}
        className="mt-8 card space-y-4"
      >
        <FormSelect
          label="Select Doctor"
          required
          options={doctorOptions}
          registration={register('doctorId', { required: 'Please select a doctor' })}
          error={errors.doctorId}
        />
        <FormInput
          label="Appointment Date"
          type="date"
          required
          registration={register('appointmentDate', { required: 'Date is required' })}
          error={errors.appointmentDate}
        />
        <FormInput
          label="Preferred Time"
          type="time"
          required
          registration={register('startTime', { required: 'Time is required' })}
          error={errors.startTime}
        />
        <DoctorCalendar
          slots={['09:00', '09:30', '10:00', '10:30', '11:00', '11:30']}
          selectedSlot={selectedSlot}
          onSelectSlot={(slot) => {
            setSelectedSlot(slot)
            setValue('startTime', slot)
          }}
        />
        <FormSelect
          label="Recurring Appointment"
          options={[
            { value: 'NONE', label: 'No recurring schedule' },
            { value: 'WEEKLY', label: 'Weekly for 4 weeks' },
            { value: 'MONTHLY', label: 'Monthly for 3 months' },
          ]}
          registration={register('recurrence')}
        />
        <label className="flex items-center gap-2 text-sm text-slate-700 dark:text-slate-300">
          <input type="checkbox" className="h-4 w-4" {...register('isVideoConsultation')} />
          Enable video consultation
        </label>
        <FormTextarea
          label="Reason for Visit"
          registration={register('reason')}
          error={errors.reason}
          placeholder="Briefly describe your symptoms or reason for visit"
        />
        <button
          type="submit"
          disabled={!isAuthenticated || mutation.isPending}
          className="btn-primary flex w-full items-center justify-center gap-2"
        >
          <Calendar className="h-4 w-4" />
          {mutation.isPending ? 'Booking...' : 'Book Appointment'}
        </button>
        {mutation.isError && (
          <p className="text-sm text-red-500">Failed to book appointment. Please try again.</p>
        )}
      </form>
    </div>
  )
}
