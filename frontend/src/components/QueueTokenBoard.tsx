interface QueueToken {
  token: string
  patient: string
  desk: string
  status: 'WAITING' | 'CALLED' | 'IN_PROGRESS'
}

interface QueueTokenBoardProps {
  title: string
  tokens: QueueToken[]
}

const statusStyles: Record<QueueToken['status'], string> = {
  WAITING: 'bg-amber-100 text-amber-700 dark:bg-amber-900/30 dark:text-amber-300',
  CALLED: 'bg-sky-100 text-sky-700 dark:bg-sky-900/30 dark:text-sky-300',
  IN_PROGRESS: 'bg-emerald-100 text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300',
}

export default function QueueTokenBoard({ title, tokens }: QueueTokenBoardProps) {
  return (
    <div className="card">
      <h2 className="text-xl font-semibold text-slate-900 dark:text-white">{title}</h2>
      <div className="mt-4 overflow-hidden rounded-xl border border-slate-200 dark:border-slate-700">
        <table className="w-full text-left text-sm">
          <thead className="bg-slate-50 dark:bg-slate-800">
            <tr>
              <th className="px-4 py-3">Token</th>
              <th className="px-4 py-3">Patient</th>
              <th className="px-4 py-3">Desk</th>
              <th className="px-4 py-3">Status</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-slate-200 dark:divide-slate-700">
            {tokens.map((entry) => (
              <tr key={entry.token}>
                <td className="px-4 py-3 font-semibold">{entry.token}</td>
                <td className="px-4 py-3">{entry.patient}</td>
                <td className="px-4 py-3">{entry.desk}</td>
                <td className="px-4 py-3">
                  <span className={`rounded-full px-2.5 py-1 text-xs font-medium ${statusStyles[entry.status]}`}>
                    {entry.status.replace('_', ' ')}
                  </span>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
