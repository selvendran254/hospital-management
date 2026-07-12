import { appointmentService, billingService, labService } from '@/api/services/index.ts'
import StatCard from '@/components/StatCard.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { useQuery } from '@tanstack/react-query'
import { Calendar, CreditCard, FlaskConical, Heart } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function PatientDashboard() {
  const { user } = useAuth()

  const { data: appointments } = useQuery({
    queryKey: ['patient-appointments-count'],
    queryFn: () => appointmentService.getMyAppointments({ page: 0, size: 1 }),
  })

  const { data: bills } = useQuery({
    queryKey: ['patient-bills-count'],
    queryFn: () => billingService.getMyBills({ page: 0, size: 1 }),
  })

  const { data: reports } = useQuery({
    queryKey: ['patient-reports-count'],
    queryFn: () => labService.getMyReports({ page: 0, size: 1 }),
  })

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Welcome back{user?.fullName ? `, ${user.fullName}` : ''}</h1>
        <p className="page-subtitle">Your health dashboard</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Appointments" value={appointments?.totalElements ?? 0} icon={Calendar} color="teal" />
        <StatCard title="Lab Reports" value={reports?.totalElements ?? 0} icon={FlaskConical} color="blue" />
        <StatCard title="Bills" value={bills?.totalElements ?? 0} icon={CreditCard} color="amber" />
        <StatCard title="Health Score" value="Good" icon={Heart} color="rose" />
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        <Link to="/patient/appointments" className="card transition hover:border-primary-300">
          <h3 className="font-semibold text-slate-900 dark:text-white">Book Appointment</h3>
          <p className="mt-1 text-sm text-slate-500">Schedule a visit with a specialist</p>
        </Link>
        <Link to="/patient/lab-reports" className="card transition hover:border-primary-300">
          <h3 className="font-semibold text-slate-900 dark:text-white">View Lab Reports</h3>
          <p className="mt-1 text-sm text-slate-500">Check your test results</p>
        </Link>
        <Link to="/patient/bills" className="card transition hover:border-primary-300">
          <h3 className="font-semibold text-slate-900 dark:text-white">Pay Bills</h3>
          <p className="mt-1 text-sm text-slate-500">View and pay outstanding bills</p>
        </Link>
      </div>
    </div>
  )
}
