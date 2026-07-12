import ChartCard from '@/components/ChartCard.tsx'
import { Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

const bpTrend = [
  { day: 'Mon', systolic: 122, diastolic: 82 },
  { day: 'Tue', systolic: 124, diastolic: 81 },
  { day: 'Wed', systolic: 120, diastolic: 79 },
  { day: 'Thu', systolic: 126, diastolic: 84 },
  { day: 'Fri', systolic: 121, diastolic: 80 },
]

export default function HealthTrackerPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Health Tracker</h1>
        <p className="page-subtitle">Track blood pressure and vitals over time.</p>
      </div>
      <ChartCard title="Blood Pressure Trend" subtitle="Last 5 days">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart data={bpTrend}>
            <XAxis dataKey="day" />
            <YAxis />
            <Tooltip />
            <Line type="monotone" dataKey="systolic" stroke="#0ea5e9" strokeWidth={2} />
            <Line type="monotone" dataKey="diastolic" stroke="#14b8a6" strokeWidth={2} />
          </LineChart>
        </ResponsiveContainer>
      </ChartCard>
    </div>
  )
}
