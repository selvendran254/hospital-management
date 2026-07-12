import ChartCard from '@/components/ChartCard.tsx'
import { Bar, BarChart, Line, LineChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

const revenue = [
  { month: 'Jan', revenue: 16, margin: 4.2 },
  { month: 'Feb', revenue: 18, margin: 4.5 },
  { month: 'Mar', revenue: 21, margin: 5.1 },
  { month: 'Apr', revenue: 23, margin: 5.8 },
  { month: 'May', revenue: 25, margin: 6.2 },
]

const branchPerformance = [
  { branch: 'Main', score: 92 },
  { branch: 'North', score: 86 },
  { branch: 'South', score: 89 },
]

export default function OwnerAnalyticsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Owner Analytics</h1>
        <p className="page-subtitle">Financial, operational, and branch-level executive insights.</p>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <ChartCard title="Revenue vs Margin" subtitle="Monthly INR in lakhs">
          <ResponsiveContainer width="100%" height="100%">
            <LineChart data={revenue}>
              <XAxis dataKey="month" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="revenue" stroke="#14b8a6" strokeWidth={2} />
              <Line type="monotone" dataKey="margin" stroke="#0ea5e9" strokeWidth={2} />
            </LineChart>
          </ResponsiveContainer>
        </ChartCard>
        <ChartCard title="Branch Performance Index" subtitle="Quality and throughput score">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={branchPerformance}>
              <XAxis dataKey="branch" />
              <YAxis />
              <Tooltip />
              <Bar dataKey="score" fill="#22c55e" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </ChartCard>
      </div>
    </div>
  )
}
