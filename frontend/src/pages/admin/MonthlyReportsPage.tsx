import { advancedService } from '@/api/services/index.ts'
import ChartCard from '@/components/ChartCard.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useQuery } from '@tanstack/react-query'
import { jsPDF } from 'jspdf'
import { Download } from 'lucide-react'
import { Bar, BarChart, CartesianGrid, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

export default function MonthlyReportsPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['reports', 'monthly-revenue'],
    queryFn: () => advancedService.getMonthlyRevenue(),
  })

  function handleDownloadPdf() {
    const doc = new jsPDF()
    doc.setFontSize(16)
    doc.text('Monthly Revenue Report', 14, 18)
    doc.setFontSize(10)
    ;(data ?? []).forEach((entry, index) => {
      doc.text(`${entry.month}: INR ${entry.revenue.toFixed(2)} lakhs`, 14, 30 + index * 8)
    })
    doc.save('monthly-reports.pdf')
  }

  if (isLoading) {
    return <SkeletonLoader rows={10} className="card" />
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <div>
          <h1 className="page-title">Monthly Reports</h1>
          <p className="page-subtitle">Revenue overview with export-ready PDF reports.</p>
        </div>
        <button type="button" className="btn-primary gap-2" onClick={handleDownloadPdf}>
          <Download className="h-4 w-4" />
          Download PDF
        </button>
      </div>
      <ChartCard title="Monthly Revenue" subtitle="INR in lakhs">
        <ResponsiveContainer width="100%" height="100%">
          <BarChart data={data}>
            <CartesianGrid strokeDasharray="3 3" className="stroke-slate-200 dark:stroke-slate-700" />
            <XAxis dataKey="month" className="text-xs" />
            <YAxis className="text-xs" />
            <Tooltip />
            <Bar dataKey="revenue" fill="#14b8a6" radius={[4, 4, 0, 0]} />
          </BarChart>
        </ResponsiveContainer>
      </ChartCard>
    </div>
  )
}
