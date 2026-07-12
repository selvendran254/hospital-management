import type { LucideIcon } from 'lucide-react'

interface StatCardProps {
  title: string
  value: string | number
  icon: LucideIcon
  trend?: string
  trendUp?: boolean
  color?: 'teal' | 'blue' | 'amber' | 'rose' | 'violet'
}

const colorMap = {
  teal: 'bg-primary-50 text-primary-600 dark:bg-primary-950 dark:text-primary-400',
  blue: 'bg-sky-50 text-sky-600 dark:bg-sky-950 dark:text-sky-400',
  amber: 'bg-amber-50 text-amber-600 dark:bg-amber-950 dark:text-amber-400',
  rose: 'bg-rose-50 text-rose-600 dark:bg-rose-950 dark:text-rose-400',
  violet: 'bg-violet-50 text-violet-600 dark:bg-violet-950 dark:text-violet-400',
}

export default function StatCard({ title, value, icon: Icon, trend, trendUp, color = 'teal' }: StatCardProps) {
  return (
    <div className="card flex items-start justify-between">
      <div>
        <p className="text-sm font-medium text-slate-500 dark:text-slate-400">{title}</p>
        <p className="mt-2 text-3xl font-bold text-slate-900 dark:text-white">{value}</p>
        {trend && (
          <p className={`mt-1 text-xs ${trendUp ? 'text-emerald-600' : 'text-red-500'}`}>{trend}</p>
        )}
      </div>
      <div className={`rounded-xl p-3 ${colorMap[color]}`}>
        <Icon className="h-6 w-6" />
      </div>
    </div>
  )
}
