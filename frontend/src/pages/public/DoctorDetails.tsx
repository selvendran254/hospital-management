import { doctorService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { ArrowLeft, Calendar, CheckCircle2, DollarSign, Mail, User, XCircle } from 'lucide-react'
import { Link, useParams } from 'react-router-dom'

export default function DoctorDetails() {
  const { id } = useParams<{ id: string }>()

  const { data: doctor, isLoading, isError } = useQuery({
    queryKey: ['doctors', id],
    queryFn: () => doctorService.getById(id!),
    enabled: !!id,
  })

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  if (isError || !doctor) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-12 text-center">
        <p className="text-slate-500">Doctor not found.</p>
        <Link to="/doctors" className="mt-4 inline-block text-primary-600 hover:underline">
          Back to doctors
        </Link>
      </div>
    )
  }

  const name = doctor.fullName ?? `${doctor.firstName ?? ''} ${doctor.lastName ?? ''}`.trim()

  return (
    <div className="mx-auto max-w-4xl px-4 py-12 sm:px-6 lg:px-8">
      <Link to="/doctors" className="inline-flex items-center gap-2 text-sm text-primary-600 hover:underline">
        <ArrowLeft className="h-4 w-4" />
        Back to doctors
      </Link>

      <div className="mt-6 card">
        <div className="flex flex-col gap-6 sm:flex-row">
          <div className="flex h-32 w-32 shrink-0 items-center justify-center rounded-2xl bg-primary-100 text-primary-600 dark:bg-primary-950">
            <User className="h-16 w-16" />
          </div>
          <div className="flex-1">
            <h1 className="text-2xl font-bold text-slate-900 dark:text-white">{name}</h1>
            <p className="mt-1 text-lg text-primary-600">{doctor.specialization}</p>
            <p className="text-slate-500">{doctor.departmentName}</p>
            <div className="mt-3 flex flex-wrap gap-2">
              {doctor.available !== false ? (
                <span className="inline-flex items-center gap-1 rounded-full bg-emerald-50 px-3 py-1 text-xs font-medium text-emerald-700 dark:bg-emerald-950 dark:text-emerald-300">
                  <CheckCircle2 className="h-3.5 w-3.5" />
                  Available for appointments
                </span>
              ) : (
                <span className="inline-flex items-center gap-1 rounded-full bg-slate-100 px-3 py-1 text-xs text-slate-500 dark:bg-slate-800">
                  <XCircle className="h-3.5 w-3.5" />
                  Currently unavailable
                </span>
              )}
              {doctor.gender && (
                <span className="rounded-full bg-slate-100 px-3 py-1 text-xs text-slate-600 dark:bg-slate-800 dark:text-slate-300">
                  {doctor.gender.charAt(0) + doctor.gender.slice(1).toLowerCase()}
                </span>
              )}
            </div>
            <div className="mt-4 flex flex-wrap gap-4 text-sm text-slate-600 dark:text-slate-400">
              {doctor.qualification && <span>{doctor.qualification}</span>}
              {doctor.experienceYears != null && <span>{doctor.experienceYears} years experience</span>}
              {doctor.consultationFee != null && (
                <span className="flex items-center gap-1">
                  <DollarSign className="h-4 w-4" />
                  ${doctor.consultationFee} consultation fee
                </span>
              )}
            </div>
          </div>
        </div>

        {doctor.bio && (
          <div className="mt-6 border-t border-slate-200 pt-6 dark:border-slate-700">
            <h2 className="font-semibold text-slate-900 dark:text-white">About</h2>
            <p className="mt-2 text-slate-600 dark:text-slate-400">{doctor.bio}</p>
          </div>
        )}

        <div className="mt-6 flex flex-wrap gap-3">
          <Link to="/appointment" state={{ doctorId: doctor.id }} className="btn-primary flex items-center gap-2">
            <Calendar className="h-4 w-4" />
            Book Appointment
          </Link>
          {doctor.email && (
            <a href={`mailto:${doctor.email}`} className="btn-secondary flex items-center gap-2">
              <Mail className="h-4 w-4" />
              Contact
            </a>
          )}
        </div>
      </div>
    </div>
  )
}
