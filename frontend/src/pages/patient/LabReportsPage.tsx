import { labService } from '@/api/services/index.ts'
import AiReportSummary from '@/components/AiReportSummary.tsx'
import DataTable from '@/components/DataTable.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'

export default function LabReportsPage() {
  const { page, setPage } = usePagination()

  const { data, isLoading } = useQuery({
    queryKey: ['patient-lab-reports', page],
    queryFn: () => labService.getMyReports({ page, size: 10 }),
  })

  if (isLoading && !data) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Lab Reports</h1>
        <p className="page-subtitle">Your test results and reports</p>
      </div>
      <DataTable
        columns={[
          { key: 'testName', header: 'Test' },
          { key: 'doctorName', header: 'Ordered By' },
          { key: 'status', header: 'Status' },
          { key: 'orderedAt', header: 'Ordered', render: (r) => <>{r.orderedAt ? new Date(r.orderedAt).toLocaleDateString() : '-'}</> },
          { key: 'completedAt', header: 'Completed', render: (r) => <>{r.completedAt ? new Date(r.completedAt).toLocaleDateString() : '-'}</> },
        ]}
        data={data?.content ?? []}
        keyExtractor={(r) => r.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
      <AiReportSummary
        riskLevel="MEDIUM"
        summary="AI triage identifies mildly elevated inflammatory markers and recommends clinical correlation with symptoms, hydration status, and follow-up CBC within 7 days."
      />
    </div>
  )
}
