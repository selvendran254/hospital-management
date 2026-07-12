import QueueTokenBoard from '@/components/QueueTokenBoard.tsx'

const tokens = [
  { token: 'R-208', patient: 'N. Kavitha', desk: 'Reception 1', status: 'WAITING' as const },
  { token: 'R-209', patient: 'M. Dinesh', desk: 'Reception 2', status: 'CALLED' as const },
  { token: 'B-145', patient: 'R. Harish', desk: 'Billing', status: 'IN_PROGRESS' as const },
]

export default function QueueTokenPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Queue Token Display</h1>
        <p className="page-subtitle">Manage real-time patient queue tokens at reception.</p>
      </div>
      <QueueTokenBoard title="Reception Queue Console" tokens={tokens} />
    </div>
  )
}
