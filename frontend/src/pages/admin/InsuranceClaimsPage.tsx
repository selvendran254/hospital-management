import { advancedService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useQuery } from '@tanstack/react-query'

export default function InsuranceClaimsPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['insurance-claims', 'admin'],
    queryFn: () => advancedService.getInsuranceClaims(),
  })

  if (isLoading) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Insurance Claims</h1>
        <p className="page-subtitle">Review claims, policy coverage, and payer approvals.</p>
      </div>
      <DataTable
        columns={[
          { key: 'policyNumber', header: 'Policy Number' },
          { key: 'patientName', header: 'Patient' },
          { key: 'amount', header: 'Amount', render: (claim) => <>INR {claim.amount.toLocaleString()}</> },
          { key: 'status', header: 'Status' },
        ]}
        data={data ?? []}
        keyExtractor={(claim) => claim.id}
      />
    </div>
  )
}
