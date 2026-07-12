import ChartCard from '@/components/ChartCard.tsx'
import { Area, AreaChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

const annualSeries = [
  { quarter: 'Q1', revenue: 38, expenses: 22 },
  { quarter: 'Q2', revenue: 44, expenses: 27 },
  { quarter: 'Q3', revenue: 49, expenses: 30 },
  { quarter: 'Q4', revenue: 55, expenses: 32 },
]

export default function FinancialYearReportPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Financial Year Report</h1>
        <p className="page-subtitle">Annual revenue and expenditure trends across all branches.</p>
      </div>
      <ChartCard title="Annual Financial Trend" subtitle="INR in crores">
        <ResponsiveContainer width="100%" height="100%">
          <AreaChart data={annualSeries}>
            <CartesianGrid strokeDasharray="3 3" className="stroke-slate-200 dark:stroke-slate-700" />
            <XAxis dataKey="quarter" className="text-xs" />
            <YAxis className="text-xs" />
            <Tooltip />
            <Area type="monotone" dataKey="revenue" stroke="#14b8a6" fill="#14b8a64d" />
            <Area type="monotone" dataKey="expenses" stroke="#f97316" fill="#f9731640" />
          </AreaChart>
        </ResponsiveContainer>
      </ChartCard>
    </div>
  )
}
