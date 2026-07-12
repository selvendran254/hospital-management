import { labService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import { FormTextarea } from '@/components/FormInput.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import Modal from '@/components/Modal.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import type { LabReport } from '@/types/index.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useState } from 'react'
import { useForm } from 'react-hook-form'

export default function PendingReportsPage() {
  const [selected, setSelected] = useState<LabReport | null>(null)
  const [autoEmailEnabled, setAutoEmailEnabled] = useState(true)
  const { page, setPage } = usePagination()
  const queryClient = useQueryClient()
  const { register, handleSubmit, reset } = useForm<{ results: string; notes: string }>()

  const { data, isLoading } = useQuery({
    queryKey: ['lab-reports-pending', page],
    queryFn: () => labService.getReports({ page, size: 10, status: 'PENDING' }),
  })

  const mutation = useMutation({
    mutationFn: ({ id, results, notes }: { id: string; results: string; notes: string }) =>
      labService.updateReport(id, { results, notes, status: 'COMPLETED', completedAt: new Date().toISOString() }),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['lab-reports-pending'] })
      setSelected(null)
      reset()
    },
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Pending Reports</h1>
        <p className="page-subtitle">Complete pending lab reports</p>
        <label className="mt-3 inline-flex items-center gap-2 rounded-lg bg-slate-100 px-3 py-2 text-sm dark:bg-slate-800">
          <input
            type="checkbox"
            checked={autoEmailEnabled}
            onChange={(event) => setAutoEmailEnabled(event.target.checked)}
          />
          Auto-email report to patient
        </label>
      </div>
      <DataTable
        columns={[
          { key: 'patientName', header: 'Patient' },
          { key: 'testName', header: 'Test' },
          { key: 'doctorName', header: 'Ordered By' },
          { key: 'orderedAt', header: 'Ordered', render: (r) => <>{r.orderedAt ? new Date(r.orderedAt).toLocaleDateString() : '-'}</> },
        ]}
        data={data?.content ?? []}
        keyExtractor={(r) => r.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
        actions={(r) => (
          <button type="button" onClick={() => setSelected(r)} className="text-xs text-primary-600 hover:underline">
            Complete
          </button>
        )}
      />
      <Modal isOpen={!!selected} onClose={() => setSelected(null)} title="Complete Report">
        <form onSubmit={handleSubmit((d) => selected && mutation.mutate({ id: selected.id, ...d }))} className="space-y-4">
          <FormTextarea label="Results" required registration={register('results', { required: true })} />
          <FormTextarea label="Notes" registration={register('notes')} />
          <button type="submit" disabled={mutation.isPending} className="btn-primary w-full">
            {mutation.isPending ? 'Saving...' : 'Submit Report'}
          </button>
        </form>
      </Modal>
    </div>
  )
}
