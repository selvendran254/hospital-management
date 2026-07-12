import StatCard from '@/components/StatCard.tsx'
import { ClipboardList, FlaskConical, CheckCircle, Clock } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function LaboratoryDashboard() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Laboratory Dashboard</h1>
        <p className="page-subtitle">Diagnostic lab operations</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Pending Reports" value={18} icon={Clock} color="amber" />
        <StatCard title="Completed Today" value={24} icon={CheckCircle} color="teal" />
        <StatCard title="Total Tests" value={156} icon={FlaskConical} color="blue" />
        <StatCard title="In Progress" value={8} icon={ClipboardList} color="violet" />
      </div>
      <div className="grid gap-4 sm:grid-cols-3">
        <Link to="/laboratory/tests" className="card hover:border-primary-300">
          <h3 className="font-semibold">Manage Tests</h3>
          <p className="mt-1 text-sm text-slate-500">View available lab tests</p>
        </Link>
        <Link to="/laboratory/pending" className="card hover:border-primary-300">
          <h3 className="font-semibold">Pending Reports</h3>
          <p className="mt-1 text-sm text-slate-500">Process pending test results</p>
        </Link>
        <Link to="/laboratory/completed" className="card hover:border-primary-300">
          <h3 className="font-semibold">Completed Reports</h3>
          <p className="mt-1 text-sm text-slate-500">View finalized reports</p>
        </Link>
      </div>
    </div>
  )
}
