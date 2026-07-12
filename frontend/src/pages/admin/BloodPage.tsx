import { bloodService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import type { BloodInventory } from '@/types/index.ts'
import { useQuery } from '@tanstack/react-query'

const groupLabels: Record<string, string> = {
  A_POSITIVE: 'A+', A_NEGATIVE: 'A-', B_POSITIVE: 'B+', B_NEGATIVE: 'B-',
  AB_POSITIVE: 'AB+', AB_NEGATIVE: 'AB-', O_POSITIVE: 'O+', O_NEGATIVE: 'O-',
}

export default function BloodPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['admin-blood-inventory'],
    queryFn: () => bloodService.getInventory(),
  })

  const columns = [
    { key: 'bloodGroup', header: 'Blood Group', render: (b: BloodInventory) => <>{groupLabels[b.bloodGroup] ?? b.bloodGroup}</> },
    { key: 'unitsAvailable', header: 'Units Available' },
    { key: 'lastUpdated', header: 'Last Updated', render: (b: BloodInventory) => <>{b.lastUpdated ? new Date(b.lastUpdated).toLocaleDateString() : '-'}</> },
  ]

  if (isLoading) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Blood Bank</h1>
        <p className="page-subtitle">Monitor blood inventory levels</p>
      </div>
      <DataTable columns={columns} data={data ?? []} keyExtractor={(b) => b.id} emptyMessage="No inventory data" />
    </div>
  )
}
