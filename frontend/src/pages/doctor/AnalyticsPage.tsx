import ChartCard from '@/components/ChartCard.tsx'
import StatCard from '@/components/StatCard.tsx'
import { Activity, Calendar, TrendingUp, Users } from 'lucide-react'
import { Area, AreaChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

const trendData = [
  { month: 'Jan', patients: 45, appointments: 120 },
  { month: 'Feb', patients: 52, appointments: 135 },
  { month: 'Mar', patients: 48, appointments: 128 },
  { month: 'Apr', patients: 61, appointments: 150 },
  { month: 'May', patients: 55, appointments: 142 },
  { month: 'Jun', patients: 67, appointments: 160 },
]

export default function AnalyticsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Analytics</h1>
        <p className="page-subtitle">Performance insights and trends</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Avg. Daily Patients" value={12} icon={Users} color="teal" trend="+8% from last month" trendUp />
        <StatCard title="Completion Rate" value="94%" icon={Activity} color="blue" />
        <StatCard title="Monthly Appointments" value={160} icon={Calendar} color="violet" />
        <StatCard title="Patient Satisfaction" value="4.8" icon={TrendingUp} color="amber" />
      </div>
      <ChartCard title="Patient & Appointment Trends" subtitle="Last 6 months">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={trendData}>
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="month" />
            <YAxis />
            <Tooltip />
            <Area type="monotone" dataKey="patients" stroke="#14b8a6" fill="#14b8a6" fillOpacity={0.2} />
            <Area type="monotone" dataKey="appointments" stroke="#0ea5e9" fill="#0ea5e9" fillOpacity={0.2} />
          </AreaChart>
        </ResponsiveContainer>
      </ChartCard>
    </div>
  )
}
