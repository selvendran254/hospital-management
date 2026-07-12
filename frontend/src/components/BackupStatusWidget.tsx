import { CloudUpload, ShieldCheck } from 'lucide-react'

interface BackupStatusWidgetProps {
  lastBackup: string
  nextBackup: string
  status: 'HEALTHY' | 'WARNING'
}

export default function BackupStatusWidget({ lastBackup, nextBackup, status }: BackupStatusWidgetProps) {
  const healthy = status === 'HEALTHY'

  return (
    <section className="card space-y-3">
      <div className="flex items-center gap-2">
        <CloudUpload className="h-5 w-5 text-primary-600" />
        <h3 className="text-lg font-semibold text-slate-900 dark:text-white">Backup Status</h3>
      </div>
      <div className="space-y-2 text-sm text-slate-600 dark:text-slate-300">
        <p>Last backup: {lastBackup}</p>
        <p>Next backup: {nextBackup}</p>
      </div>
      <p
        className={`inline-flex items-center gap-2 rounded-full px-3 py-1 text-xs font-medium ${
          healthy
            ? 'bg-emerald-50 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300'
            : 'bg-amber-50 text-amber-700 dark:bg-amber-900/30 dark:text-amber-300'
        }`}
      >
        <ShieldCheck className="h-3.5 w-3.5" />
        {healthy ? 'Backups healthy' : 'Attention needed'}
      </p>
    </section>
  )
}
