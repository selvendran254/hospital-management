import { advancedService, publicService } from '@/api/services/index.ts'
import BackupStatusWidget from '@/components/BackupStatusWidget.tsx'
import ChartCard from '@/components/ChartCard.tsx'
import IcuMonitorWidget from '@/components/IcuMonitorWidget.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import StatCard from '@/components/StatCard.tsx'
import { useQuery } from '@tanstack/react-query'
import { Activity, Bed, Calendar, FlaskConical, MessageSquare, Pill, Stethoscope, Users } from 'lucide-react'
import { Bar, BarChart, CartesianGrid, Legend, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

const fallbackStats = {
  totalPatients: 1250,
  totalDoctors: 85,
  totalAppointments: 342,
  totalDepartments: 24,
  pendingAppointments: 45,
  todayAppointments: 28,
  occupiedBeds: 320,
  availableBeds: 180,
  lowStockMedicines: 12,
  pendingLabReports: 18,
}

const appointmentTrend = [
  { name: 'Mon', appointments: 42, completed: 38 },
  { name: 'Tue', appointments: 55, completed: 50 },
  { name: 'Wed', appointments: 48, completed: 45 },
  { name: 'Thu', appointments: 62, completed: 58 },
  { name: 'Fri', appointments: 51, completed: 48 },
  { name: 'Sat', appointments: 35, completed: 32 },
  { name: 'Sun', appointments: 20, completed: 18 },
]

const departmentData = [
  { name: 'Cardiology', patients: 120 },
  { name: 'Neurology', patients: 95 },
  { name: 'Orthopedics', patients: 110 },
  { name: 'Pediatrics', patients: 85 },
  { name: 'Emergency', patients: 150 },
]

const revenueForecastData = [
  { month: 'Aug', projected: 22 },
  { month: 'Sep', projected: 24 },
  { month: 'Oct', projected: 27 },
  { month: 'Nov', projected: 29 },
]

export default function AdminDashboard() {
  const { data: stats, isLoading } = useQuery({
    queryKey: ['admin', 'dashboard-stats'],
    queryFn: () => publicService.getDashboardStats(),
  })
  const { data: smsSettings } = useQuery({
    queryKey: ['admin', 'sms-settings'],
    queryFn: () => advancedService.getReminderSettings(),
  })
  const { data: backupStatus } = useQuery({
    queryKey: ['admin', 'backup-status'],
    queryFn: () => advancedService.getBackupStatus(),
  })

  const s = stats ?? fallbackStats

  if (isLoading && !stats) {
    return <SkeletonLoader rows={10} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Admin Dashboard</h1>
        <p className="page-subtitle">Hospital overview and key metrics</p>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Total Patients" value={s.totalPatients} icon={Users} color="teal" />
        <StatCard title="Doctors" value={s.totalDoctors} icon={Stethoscope} color="blue" />
        <StatCard title="Today's Appointments" value={s.todayAppointments} icon={Calendar} color="violet" />
        <StatCard title="Pending Appointments" value={s.pendingAppointments} icon={Activity} color="amber" />
      </div>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Departments" value={s.totalDepartments} icon={Activity} color="teal" />
        <StatCard title="Occupied Beds" value={s.occupiedBeds} icon={Bed} color="rose" />
        <StatCard title="Low Stock Medicines" value={s.lowStockMedicines} icon={Pill} color="amber" />
        <StatCard title="Pending Lab Reports" value={s.pendingLabReports} icon={FlaskConical} color="blue" />
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <section className="card space-y-3">
          <h2 className="flex items-center gap-2 text-lg font-semibold text-slate-900 dark:text-white">
            <MessageSquare className="h-5 w-5 text-primary-600" />
            SMS Reminder Settings
          </h2>
          <p className="text-sm text-slate-600 dark:text-slate-300">
            Status: {smsSettings?.smsEnabled ? 'Enabled' : 'Disabled'}
          </p>
          <p className="text-sm text-slate-600 dark:text-slate-300">
            Provider: {smsSettings?.provider ?? '-'} | Sender ID: {smsSettings?.senderId ?? '-'}
          </p>
          <p className="text-sm text-slate-600 dark:text-slate-300">
            Reminder lead time: {smsSettings?.leadHours ?? 0} hours before appointment.
          </p>
        </section>
        <BackupStatusWidget
          lastBackup={backupStatus?.lastBackup ?? 'Unknown'}
          nextBackup={backupStatus?.nextBackup ?? 'Unknown'}
          status={backupStatus?.status ?? 'WARNING'}
        />
      </div>

      <div className="grid gap-6 lg:grid-cols-2">
        <ChartCard title="Weekly Appointments" subtitle="Appointments vs completed">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={appointmentTrend}>
              <CartesianGrid strokeDasharray="3 3" className="stroke-slate-200 dark:stroke-slate-700" />
              <XAxis dataKey="name" className="text-xs" />
              <YAxis className="text-xs" />
              <Tooltip />
              <Legend />
              <Line type="monotone" dataKey="appointments" stroke="#14b8a6" strokeWidth={2} />
              <Line type="monotone" dataKey="completed" stroke="#0ea5e9" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </ChartCard>

        <ChartCard title="Patients by Department" subtitle="Current patient distribution">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={departmentData}>
              <CartesianGrid strokeDasharray="3 3" className="stroke-slate-200 dark:stroke-slate-700" />
              <XAxis dataKey="name" className="text-xs" />
              <YAxis className="text-xs" />
              <Tooltip />
              <Bar dataKey="patients" fill="#14b8a6" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </ChartCard>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <ChartCard title="Revenue Forecast" subtitle="Projected monthly revenue (INR lakhs)">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={revenueForecastData}>
              <CartesianGrid strokeDasharray="3 3" className="stroke-slate-200 dark:stroke-slate-700" />
              <XAxis dataKey="month" className="text-xs" />
              <YAxis className="text-xs" />
              <Tooltip />
              <Bar dataKey="projected" fill="#22c55e" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </ChartCard>
        <IcuMonitorWidget />
      </div>
    </div>
  )
}
