const logs = [
  { id: '1', actor: 'admin@medicare.com', action: 'Updated branch branding', time: '2026-07-12 08:15' },
  { id: '2', actor: 'owner@medicare.com', action: 'Changed billing permissions', time: '2026-07-12 07:40' },
  { id: '3', actor: 'it@medicare.com', action: 'Exported appointment report', time: '2026-07-12 06:55' },
]

export default function AuditLogsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Audit Logs</h1>
        <p className="page-subtitle">Track critical actions for governance and compliance.</p>
      </div>
      <div className="card space-y-3">
        {logs.map((entry) => (
          <div key={entry.id} className="rounded-lg bg-slate-100 p-3 dark:bg-slate-800">
            <p className="text-sm font-medium text-slate-900 dark:text-white">{entry.action}</p>
            <p className="text-xs text-slate-500">{entry.actor} - {entry.time}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
