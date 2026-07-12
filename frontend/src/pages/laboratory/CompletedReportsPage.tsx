import { labService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'

export default function CompletedReportsPage() {
  const { page, setPage } = usePagination()

  const { data, isLoading } = useQuery({
    queryKey: ['lab-reports-completed', page],
    queryFn: () => labService.getReports({ page, size: 10, status: 'COMPLETED' }),
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Completed Reports</h1>
        <p className="page-subtitle">Finalized lab test results</p>
      </div>
      <DataTable
        columns={[
          { key: 'patientName', header: 'Patient' },
          { key: 'testName', header: 'Test' },
          { key: 'results', header: 'Results' },
          { key: 'completedAt', header: 'Completed', render: (r) => <>{r.completedAt ? new Date(r.completedAt).toLocaleDateString() : '-'}</> },
        ]}
        data={data?.content ?? []}
        keyExtractor={(r) => r.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
    </div>
  )
}
