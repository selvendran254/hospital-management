import { appointmentService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import SearchBar from '@/components/SearchBar.tsx'
import { useDebounce } from '@/hooks/useDebounce.ts'
import { usePagination } from '@/hooks/usePagination.ts'
import type { Appointment } from '@/types/index.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Check, X } from 'lucide-react'
import { useState } from 'react'

export default function AppointmentsPage() {
  const [search, setSearch] = useState('')
  const debouncedSearch = useDebounce(search)
  const { page, setPage } = usePagination()
  const queryClient = useQueryClient()

  const { data, isLoading } = useQuery({
    queryKey: ['admin-appointments', page, debouncedSearch],
    queryFn: () => appointmentService.getAll({ page, size: 10, search: debouncedSearch || undefined }),
  })

  const approveMutation = useMutation({
    mutationFn: (id: string) => appointmentService.approve(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-appointments'] }),
  })

  const cancelMutation = useMutation({
    mutationFn: (id: string) => appointmentService.cancel(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-appointments'] }),
  })

  const columns = [
    { key: 'patientName', header: 'Patient' },
    { key: 'doctorName', header: 'Doctor' },
    { key: 'appointmentDate', header: 'Date' },
    { key: 'startTime', header: 'Time' },
    {
      key: 'status',
      header: 'Status',
      render: (a: Appointment) => (
        <span className={`rounded-full px-2 py-0.5 text-xs font-medium ${
          a.status === 'APPROVED' ? 'bg-emerald-100 text-emerald-700' :
          a.status === 'PENDING' ? 'bg-amber-100 text-amber-700' :
          a.status === 'CANCELLED' ? 'bg-red-100 text-red-700' :
          'bg-slate-100 text-slate-700'
        }`}>{a.status}</span>
      ),
    },
  ]

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Appointments</h1>
        <p className="page-subtitle">Manage all hospital appointments</p>
      </div>
      <SearchBar value={search} onChange={setSearch} placeholder="Search appointments..." />
      <DataTable
        columns={columns}
        data={data?.content ?? []}
        keyExtractor={(a) => a.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
        actions={(a) => (
          <div className="flex gap-2">
            {a.status === 'PENDING' && (
              <button type="button" onClick={() => approveMutation.mutate(a.id)} className="rounded p-1 text-emerald-600 hover:bg-emerald-50" title="Approve">
                <Check className="h-4 w-4" />
              </button>
            )}
            {a.status !== 'CANCELLED' && a.status !== 'COMPLETED' && (
              <button type="button" onClick={() => cancelMutation.mutate(a.id)} className="rounded p-1 text-red-600 hover:bg-red-50" title="Cancel">
                <X className="h-4 w-4" />
              </button>
            )}
          </div>
        )}
      />
    </div>
  )
}
