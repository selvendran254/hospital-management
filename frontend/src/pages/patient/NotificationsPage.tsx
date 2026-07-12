import { notificationService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'

export default function NotificationsPage() {
  const { page, setPage } = usePagination()
  const queryClient = useQueryClient()

  const { data, isLoading } = useQuery({
    queryKey: ['patient-notifications', page],
    queryFn: () => notificationService.getAll({ page, size: 10 }),
  })

  const markRead = useMutation({
    mutationFn: (id: string) => notificationService.markAsRead(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['patient-notifications'] }),
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Notifications</h1>
        <p className="page-subtitle">Stay updated on your healthcare</p>
      </div>
      <DataTable
        columns={[
          { key: 'title', header: 'Title' },
          { key: 'message', header: 'Message' },
          { key: 'type', header: 'Type' },
          { key: 'createdAt', header: 'Date', render: (n) => <>{new Date(n.createdAt).toLocaleDateString()}</> },
          { key: 'isRead', header: 'Read', render: (n) => <>{n.isRead ? 'Yes' : 'No'}</> },
        ]}
        data={data?.content ?? []}
        keyExtractor={(n) => n.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
        actions={(n) =>
          !n.isRead ? (
            <button type="button" onClick={() => markRead.mutate(n.id)} className="text-xs text-primary-600 hover:underline">
              Mark read
            </button>
          ) : null
        }
      />
    </div>
  )
}
