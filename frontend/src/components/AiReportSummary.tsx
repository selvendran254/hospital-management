import { Bot, TriangleAlert } from 'lucide-react'

interface AiReportSummaryProps {
  summary: string
  riskLevel: 'LOW' | 'MEDIUM' | 'HIGH'
}

const riskClassMap: Record<AiReportSummaryProps['riskLevel'], string> = {
  LOW: 'bg-emerald-50 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300',
  MEDIUM: 'bg-amber-50 text-amber-700 dark:bg-amber-900/30 dark:text-amber-300',
  HIGH: 'bg-rose-50 text-rose-700 dark:bg-rose-900/30 dark:text-rose-300',
}

export default function AiReportSummary({ summary, riskLevel }: AiReportSummaryProps) {
  return (
    <section className="card space-y-3">
      <div className="flex items-center justify-between">
        <h2 className="flex items-center gap-2 text-lg font-semibold text-slate-900 dark:text-white">
          <Bot className="h-5 w-5 text-primary-600" />
          AI Report Summary
        </h2>
        <span className={`rounded-full px-3 py-1 text-xs font-semibold ${riskClassMap[riskLevel]}`}>
          <span className="inline-flex items-center gap-1">
            <TriangleAlert className="h-3.5 w-3.5" />
            {riskLevel} RISK
          </span>
        </span>
      </div>
      <p className="text-sm leading-relaxed text-slate-600 dark:text-slate-300">{summary}</p>
    </section>
  )
}
