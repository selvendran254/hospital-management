import { advancedService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useQuery } from '@tanstack/react-query'

export default function InsuranceClaimsPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['insurance-claims', 'patient'],
    queryFn: () => advancedService.getInsuranceClaims(),
  })

  if (isLoading) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">My Insurance Claims</h1>
        <p className="page-subtitle">Track claim progress, approved amounts, and payer remarks.</p>
      </div>
      <DataTable
        columns={[
          { key: 'policyNumber', header: 'Policy Number' },
          { key: 'amount', header: 'Amount', render: (claim) => <>INR {claim.amount.toLocaleString()}</> },
          { key: 'status', header: 'Status' },
        ]}
        data={data ?? []}
        keyExtractor={(claim) => claim.id}
      />
    </div>
  )
}
