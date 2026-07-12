import { prescriptionService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'

export default function PrescriptionsPage() {
  const { page, setPage } = usePagination()

  const { data, isLoading } = useQuery({
    queryKey: ['doctor-prescriptions', page],
    queryFn: () => prescriptionService.getAll({ page, size: 10 }),
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Prescriptions</h1>
        <p className="page-subtitle">Manage patient prescriptions</p>
      </div>
      <DataTable
        columns={[
          { key: 'patientName', header: 'Patient' },
          { key: 'diagnosis', header: 'Diagnosis' },
          { key: 'medications', header: 'Medications' },
          { key: 'createdAt', header: 'Date', render: (p) => <>{p.createdAt ? new Date(p.createdAt).toLocaleDateString() : '-'}</> },
        ]}
        data={data?.content ?? []}
        keyExtractor={(p) => p.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
    </div>
  )
}
