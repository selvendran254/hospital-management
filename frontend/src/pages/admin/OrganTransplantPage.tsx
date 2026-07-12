const donors = [
  { id: 'donor-1', name: 'Karthik S', organ: 'Kidney', bloodGroup: 'O+' },
  { id: 'donor-2', name: 'Latha P', organ: 'Liver', bloodGroup: 'A+' },
]

const recipients = [
  { id: 'rec-1', name: 'Mohan R', organNeeded: 'Kidney', urgency: 'High' },
  { id: 'rec-2', name: 'Nivetha K', organNeeded: 'Liver', urgency: 'Medium' },
]

export default function OrganTransplantPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Organ Transplant Registry</h1>
        <p className="page-subtitle">Track donor and recipient matching queues.</p>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <section className="card">
          <h2 className="text-lg font-semibold text-slate-900 dark:text-white">Donors</h2>
          <ul className="mt-4 space-y-3">
            {donors.map((donor) => (
              <li key={donor.id} className="rounded-lg border border-slate-200 p-3 dark:border-slate-700">
                <p className="font-medium text-slate-900 dark:text-white">{donor.name}</p>
                <p className="text-sm text-slate-500 dark:text-slate-400">
                  {donor.organ} | Blood Group {donor.bloodGroup}
                </p>
              </li>
            ))}
          </ul>
        </section>
        <section className="card">
          <h2 className="text-lg font-semibold text-slate-900 dark:text-white">Recipients</h2>
          <ul className="mt-4 space-y-3">
            {recipients.map((recipient) => (
              <li key={recipient.id} className="rounded-lg border border-slate-200 p-3 dark:border-slate-700">
                <p className="font-medium text-slate-900 dark:text-white">{recipient.name}</p>
                <p className="text-sm text-slate-500 dark:text-slate-400">
                  Needs {recipient.organNeeded} | Urgency: {recipient.urgency}
                </p>
              </li>
            ))}
          </ul>
        </section>
      </div>
    </div>
  )
}
