import { appointmentService, doctorService, patientService } from '@/api/services/index.ts'
import DoctorCalendar from '@/components/DoctorCalendar.tsx'
import { FormInput, FormSelect, FormTextarea } from '@/components/FormInput.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useMutation, useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import { useForm } from 'react-hook-form'

interface BookForm {
  patientId: string
  doctorId: string
  appointmentDate: string
  startTime: string
  reason: string
  recurrence: string
  isVideoConsultation: boolean
}

export default function BookAppointmentPage() {
  const { register, handleSubmit, reset, setValue, formState: { errors } } = useForm<BookForm>({
    defaultValues: { recurrence: 'NONE', isVideoConsultation: false },
  })
  const [selectedSlot, setSelectedSlot] = useState('')

  const { data: patients, isLoading: loadingPatients } = useQuery({
    queryKey: ['patients-list'],
    queryFn: () => patientService.getAll({ page: 0, size: 100 }),
  })

  const { data: doctors, isLoading: loadingDoctors } = useQuery({
    queryKey: ['doctors-list'],
    queryFn: () => doctorService.getAll({ page: 0, size: 100 }),
  })

  const mutation = useMutation({
    mutationFn: (data: BookForm) =>
      appointmentService.create({
        patientId: data.patientId,
        doctorId: data.doctorId,
        appointmentDate: data.appointmentDate,
        startTime: data.startTime,
        endTime: data.startTime,
        reason: `${data.reason ?? ''}${data.isVideoConsultation ? ' | Video consultation' : ''}${data.recurrence !== 'NONE' ? ` | Recurrence: ${data.recurrence}` : ''}`,
        status: 'PENDING',
      }),
    onSuccess: () => reset(),
  })

  if (loadingPatients || loadingDoctors) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  const patientOptions = (patients?.content ?? []).map((p) => ({
    value: p.id,
    label: p.fullName ?? p.email ?? p.id,
  }))

  const doctorOptions = (doctors?.content ?? []).map((d) => ({
    value: d.id,
    label: `${d.fullName ?? d.specialization} - ${d.specialization}`,
  }))

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <div>
        <h1 className="page-title">Book Appointment</h1>
        <p className="page-subtitle">Schedule an appointment for a patient</p>
      </div>
      <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="card space-y-4">
        <FormSelect label="Patient" required options={patientOptions} registration={register('patientId', { required: 'Required' })} error={errors.patientId} />
        <FormSelect label="Doctor" required options={doctorOptions} registration={register('doctorId', { required: 'Required' })} error={errors.doctorId} />
        <FormInput label="Date" type="date" required registration={register('appointmentDate', { required: 'Required' })} error={errors.appointmentDate} />
        <FormInput label="Time" type="time" required registration={register('startTime', { required: 'Required' })} error={errors.startTime} />
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
          Video consultation
        </label>
        <FormTextarea label="Reason" registration={register('reason')} error={errors.reason} />
        <button type="submit" disabled={mutation.isPending} className="btn-primary w-full">
          {mutation.isPending ? 'Booking...' : 'Book Appointment'}
        </button>
        {mutation.isSuccess && <p className="text-sm text-emerald-600">Appointment booked!</p>}
      </form>
    </div>
  )
}
