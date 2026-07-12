import QueueTokenBoard from '@/components/QueueTokenBoard.tsx'

const publicQueue = [
  { token: 'A-101', patient: 'K. Ramesh', desk: 'Reception 1', status: 'CALLED' as const },
  { token: 'A-102', patient: 'S. Meena', desk: 'Reception 2', status: 'WAITING' as const },
  { token: 'A-103', patient: 'P. Ajay', desk: 'Billing', status: 'IN_PROGRESS' as const },
]

export default function QueueDisplayPage() {
  return (
    <div className="mx-auto max-w-5xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Live Queue Display</h1>
      <p className="page-subtitle">Current token status across reception counters.</p>
      <div className="mt-8">
        <QueueTokenBoard title="Public Queue Board" tokens={publicQueue} />
      </div>
    </div>
  )
}
