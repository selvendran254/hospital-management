const reminders = [
  { medicine: 'Metformin 500mg', time: '08:00 AM', status: 'Pending' },
  { medicine: 'Atorvastatin 10mg', time: '02:00 PM', status: 'Taken' },
  { medicine: 'Vitamin D3', time: '08:00 PM', status: 'Pending' },
]

export default function MedicineRemindersPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Medicine Reminders</h1>
        <p className="page-subtitle">Track scheduled doses and adherence.</p>
      </div>
      <div className="card space-y-3">
        {reminders.map((entry) => (
          <div key={`${entry.medicine}-${entry.time}`} className="flex items-center justify-between rounded-lg bg-slate-100 px-3 py-2 dark:bg-slate-800">
            <div>
              <p className="font-medium text-slate-900 dark:text-white">{entry.medicine}</p>
              <p className="text-xs text-slate-500">{entry.time}</p>
            </div>
            <span className={`rounded-full px-2 py-1 text-xs ${entry.status === 'Taken' ? 'bg-emerald-100 text-emerald-700' : 'bg-amber-100 text-amber-700'}`}>
              {entry.status}
            </span>
          </div>
        ))}
      </div>
    </div>
  )
}
