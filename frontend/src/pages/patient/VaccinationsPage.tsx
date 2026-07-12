const vaccines = [
  { name: 'Influenza', dueDate: '2026-08-15', status: 'Due' },
  { name: 'Hepatitis B', dueDate: '2026-10-10', status: 'Scheduled' },
  { name: 'COVID Booster', dueDate: '2026-06-01', status: 'Completed' },
]

export default function VaccinationsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Vaccinations</h1>
        <p className="page-subtitle">Upcoming immunizations and vaccine history.</p>
      </div>
      <div className="card overflow-hidden">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-50 dark:bg-slate-800">
            <tr>
              <th className="px-4 py-3">Vaccine</th>
              <th className="px-4 py-3">Due Date</th>
              <th className="px-4 py-3">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-200 dark:divide-slate-700">
            {vaccines.map((item) => (
              <tr key={item.name}>
                <td className="px-4 py-3">{item.name}</td>
                <td className="px-4 py-3">{item.dueDate}</td>
                <td className="px-4 py-3">{item.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
