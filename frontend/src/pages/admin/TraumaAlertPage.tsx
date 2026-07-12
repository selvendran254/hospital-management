const traumaAlerts = [
  { id: 'T-001', level: 'High', source: 'ER Gate', status: 'Active' },
  { id: 'T-002', level: 'Medium', source: 'Ambulance AMB-04', status: 'Resolved' },
]

export default function TraumaAlertPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Trauma Alert Panel</h1>
        <p className="page-subtitle">Coordinate emergency trauma inflow and response readiness.</p>
      </div>
      <div className="card space-y-3">
        {traumaAlerts.map((alert) => (
          <div key={alert.id} className="flex items-center justify-between rounded-lg bg-slate-100 px-3 py-2 dark:bg-slate-800">
            <div>
              <p className="font-semibold text-slate-900 dark:text-white">{alert.id} - {alert.source}</p>
              <p className="text-xs text-slate-500">Severity: {alert.level}</p>
            </div>
            <span className={`rounded-full px-2 py-1 text-xs ${alert.status === 'Active' ? 'bg-rose-100 text-rose-700' : 'bg-emerald-100 text-emerald-700'}`}>
              {alert.status}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}
