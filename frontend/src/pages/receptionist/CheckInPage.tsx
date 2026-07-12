import { appointmentService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import SearchBar from '@/components/SearchBar.tsx'
import { useDebounce } from '@/hooks/useDebounce.ts'
import { usePagination } from '@/hooks/usePagination.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { LogIn } from 'lucide-react'
import { useState } from 'react'

export default function CheckInPage() {
  const [search, setSearch] = useState('')
  const debouncedSearch = useDebounce(search)
  const { page, setPage } = usePagination()
  const queryClient = useQueryClient()

  const { data, isLoading } = useQuery({
    queryKey: ['receptionist-appointments', page, debouncedSearch],
    queryFn: () => appointmentService.getAll({ page, size: 10, search: debouncedSearch || undefined }),
  })

  const checkInMutation = useMutation({
    mutationFn: (id: string) => appointmentService.checkIn(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['receptionist-appointments'] }),
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Patient Check-In</h1>
        <p className="page-subtitle">Check in arriving patients</p>
      </div>
      <SearchBar value={search} onChange={setSearch} placeholder="Search by patient name..." />
      <DataTable
        columns={[
          { key: 'patientName', header: 'Patient' },
          { key: 'doctorName', header: 'Doctor' },
          { key: 'appointmentDate', header: 'Date' },
          { key: 'startTime', header: 'Time' },
          { key: 'status', header: 'Status' },
        ]}
        data={data?.content ?? []}
        keyExtractor={(a) => a.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
        actions={(a) =>
          a.status === 'APPROVED' ? (
            <button type="button" onClick={() => checkInMutation.mutate(a.id)} className="btn-primary flex items-center gap-1 !px-2 !py-1 text-xs">
              <LogIn className="h-3 w-3" /> Check In
            </button>
          ) : null
        }
      />
    </div>
  )
}
