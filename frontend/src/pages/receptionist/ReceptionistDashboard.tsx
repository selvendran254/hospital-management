import StatCard from '@/components/StatCard.tsx'
import { Calendar, CreditCard, LogIn, UserPlus } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function ReceptionistDashboard() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Reception Dashboard</h1>
        <p className="page-subtitle">Front desk operations</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Today's Check-ins" value={15} icon={LogIn} color="teal" />
        <StatCard title="New Registrations" value={4} icon={UserPlus} color="blue" />
        <StatCard title="Appointments" value={28} icon={Calendar} color="violet" />
        <StatCard title="Pending Bills" value={7} icon={CreditCard} color="amber" />
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {[
          { to: '/receptionist/register-patient', title: 'Register Patient', desc: 'Add new patient records' },
          { to: '/receptionist/book-appointment', title: 'Book Appointment', desc: 'Schedule patient visits' },
          { to: '/receptionist/check-in', title: 'Check In', desc: 'Mark patient arrivals' },
          { to: '/receptionist/billing', title: 'Billing', desc: 'Generate and collect payments' },
          { to: '/receptionist/admit-discharge', title: 'Admit / Discharge', desc: 'Manage inpatient flow' },
        ].map((item) => (
          <Link key={item.to} to={item.to} className="card transition hover:border-primary-300">
            <h3 className="font-semibold text-slate-900 dark:text-white">{item.title}</h3>
            <p className="mt-1 text-sm text-slate-500">{item.desc}</p>
          </Link>
        ))}
      </div>
    </div>
  )
}
