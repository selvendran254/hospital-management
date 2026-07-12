import { advancedService, appointmentService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import FeedbackForm from '@/components/FeedbackForm.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'
import { Link } from 'react-router-dom'

export default function PatientAppointmentsPage() {
  const { page, setPage } = usePagination()
  const [feedbackSaved, setFeedbackSaved] = useState(false)

  const { data, isLoading } = useQuery({
    queryKey: ['patient-appointments', page],
    queryFn: () => appointmentService.getMyAppointments({ page, size: 10 }),
  })
  const { data: reminderSettings } = useQuery({
    queryKey: ['appointment-reminder-settings'],
    queryFn: () => advancedService.getReminderSettings(),
  })

  if (isLoading && !data) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="page-title">My Appointments</h1>
          <p className="page-subtitle">View and manage your appointments</p>
        </div>
        <Link to="/appointment" className="btn-primary">Book New</Link>
      </div>
      <DataTable
        columns={[
          { key: 'doctorName', header: 'Doctor' },
          { key: 'appointmentDate', header: 'Date' },
          { key: 'startTime', header: 'Time' },
          { key: 'status', header: 'Status' },
          { key: 'reason', header: 'Reason' },
        ]}
        data={data?.content ?? []}
        keyExtractor={(a) => a.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
      <div className="card">
        <h2 className="text-lg font-semibold text-slate-900 dark:text-white">SMS Reminder Settings</h2>
        <p className="mt-2 text-sm text-slate-600 dark:text-slate-300">
          SMS reminders: {reminderSettings?.smsEnabled ? 'Enabled' : 'Disabled'} | Reminder lead time:{' '}
          {reminderSettings?.leadHours ?? 0} hours before appointment.
        </p>
        <p className="mt-1 text-sm text-slate-600 dark:text-slate-300">
          Provider: {reminderSettings?.provider ?? '-'} | Sender ID: {reminderSettings?.senderId ?? '-'}
        </p>
      </div>
      <FeedbackForm
        doctorName="Assigned Consultant"
        onSubmit={() => {
          setFeedbackSaved(true)
        }}
      />
      {feedbackSaved && (
        <div className="rounded-lg bg-emerald-50 px-4 py-3 text-sm text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300">
          Feedback submitted successfully.
        </div>
      )}
    </div>
  )
}
