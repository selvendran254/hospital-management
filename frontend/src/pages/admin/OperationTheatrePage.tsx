const surgeries = [
  { id: 'sr-1', theatre: 'OT-1', procedure: 'Knee Arthroscopy', surgeon: 'Dr. Rahul Sen', slot: '09:00 - 10:30' },
  { id: 'sr-2', theatre: 'OT-2', procedure: 'Bypass Surgery', surgeon: 'Dr. Kiran Das', slot: '11:00 - 14:30' },
  { id: 'sr-3', theatre: 'OT-3', procedure: 'Appendectomy', surgeon: 'Dr. Meera Nair', slot: '15:00 - 16:00' },
]

export default function OperationTheatrePage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Operation Theatre Scheduling</h1>
        <p className="page-subtitle">Calendar-style theatre planning and surgery allocation.</p>
      </div>
      <div className="card">
        <label className="text-sm font-medium text-slate-700 dark:text-slate-300" htmlFor="ot-date">
          Schedule date
        </label>
        <input id="ot-date" type="date" className="input-field mt-2 max-w-xs" defaultValue={new Date().toISOString().slice(0, 10)} />
      </div>
      <div className="grid gap-4">
        {surgeries.map((surgery) => (
          <article key={surgery.id} className="card">
            <p className="text-xs uppercase text-slate-500 dark:text-slate-400">{surgery.theatre}</p>
            <h3 className="mt-1 text-lg font-semibold text-slate-900 dark:text-white">{surgery.procedure}</h3>
            <p className="mt-1 text-sm text-slate-600 dark:text-slate-300">Surgeon: {surgery.surgeon}</p>
            <p className="mt-1 text-sm text-primary-600 dark:text-primary-400">{surgery.slot}</p>
          </article>
        ))}
      </div>
    </div>
  )
}
