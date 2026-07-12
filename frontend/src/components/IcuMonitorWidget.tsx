const beds = [
  { bed: 'ICU-01', pulse: 82, spo2: 97, status: 'Stable' },
  { bed: 'ICU-03', pulse: 108, spo2: 92, status: 'Needs attention' },
  { bed: 'ICU-08', pulse: 76, spo2: 99, status: 'Stable' },
]

export default function IcuMonitorWidget() {
  return (
    <div className="card">
      <h3 className="text-lg font-semibold text-slate-900 dark:text-white">ICU Monitor</h3>
      <div className="mt-4 space-y-2">
        {beds.map((item) => (
          <div key={item.bed} className="grid grid-cols-4 items-center rounded-lg bg-slate-100 px-3 py-2 text-xs dark:bg-slate-800">
            <span className="font-semibold">{item.bed}</span>
            <span>Pulse: {item.pulse}</span>
            <span>SpO2: {item.spo2}%</span>
            <span className={item.status === 'Stable' ? 'text-emerald-600' : 'text-rose-500'}>{item.status}</span>
          </div>
        ))}
      </div>
    </div>
  )
}
