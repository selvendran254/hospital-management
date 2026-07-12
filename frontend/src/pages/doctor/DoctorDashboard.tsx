import { doctorService } from '@/api/services/index.ts'
import ChartCard from '@/components/ChartCard.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import StatCard from '@/components/StatCard.tsx'
import { useQuery } from '@tanstack/react-query'
import { Calendar, CheckCircle, Clock, Users } from 'lucide-react'
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

const weeklyData = [
  { day: 'Mon', count: 8 }, { day: 'Tue', count: 12 }, { day: 'Wed', count: 10 },
  { day: 'Thu', count: 14 }, { day: 'Fri', count: 11 }, { day: 'Sat', count: 6 }, { day: 'Sun', count: 3 },
]

export default function DoctorDashboard() {
  const { data, isLoading } = useQuery({
    queryKey: ['doctor-dashboard'],
    queryFn: () => doctorService.getDashboard(),
  })

  const stats = data ?? {
    todayAppointments: 8, pendingAppointments: 3, completedAppointments: 5, totalPatients: 120, upcomingLeaves: 1,
  }

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Doctor Dashboard</h1>
        <p className="page-subtitle">Your daily overview</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Today's Appointments" value={stats.todayAppointments} icon={Calendar} color="teal" />
        <StatCard title="Pending" value={stats.pendingAppointments} icon={Clock} color="amber" />
        <StatCard title="Completed" value={stats.completedAppointments} icon={CheckCircle} color="blue" />
        <StatCard title="Total Patients" value={stats.totalPatients} icon={Users} color="violet" />
      </div>
      <ChartCard title="Weekly Appointments" subtitle="Appointments this week">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={weeklyData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="day" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="count" fill="#14b8a6" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </ChartCard>
    </div>
  )
}
