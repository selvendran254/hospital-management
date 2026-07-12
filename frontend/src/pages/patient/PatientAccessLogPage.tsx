import { advancedService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useQuery } from '@tanstack/react-query'

export default function PatientAccessLogPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['patient', 'access-logs'],
    queryFn: () => advancedService.getAccessLogs(),
  })

  if (isLoading) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Patient Access Log</h1>
        <p className="page-subtitle">Track who viewed your records and why.</p>
      </div>
      <DataTable
        columns={[
          { key: 'accessedBy', header: 'Viewed By' },
          { key: 'role', header: 'Role' },
          { key: 'viewedAt', header: 'Viewed At' },
          { key: 'reason', header: 'Reason' },
        ]}
        data={data ?? []}
        keyExtractor={(log) => log.id}
      />
    </div>
  )
}
