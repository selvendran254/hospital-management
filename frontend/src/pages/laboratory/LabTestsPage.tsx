import { labService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'

export default function LabTestsPage() {
  const { page, setPage } = usePagination()

  const { data, isLoading } = useQuery({
    queryKey: ['lab-tests', page],
    queryFn: () => labService.getTests({ page, size: 10 }),
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Lab Tests</h1>
        <p className="page-subtitle">Available diagnostic tests</p>
      </div>
      <DataTable
        columns={[
          { key: 'name', header: 'Test Name' },
          { key: 'code', header: 'Code' },
          { key: 'price', header: 'Price', render: (t) => <>${t.price}</> },
          { key: 'sampleType', header: 'Sample' },
          { key: 'turnaroundHours', header: 'Turnaround (hrs)' },
        ]}
        data={data?.content ?? []}
        keyExtractor={(t) => t.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
    </div>
  )
}
