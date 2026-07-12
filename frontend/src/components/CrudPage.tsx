import DataTable, { type Column } from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import Modal from '@/components/Modal.tsx'
import SearchBar from '@/components/SearchBar.tsx'
import { useDebounce } from '@/hooks/useDebounce.ts'
import { usePagination } from '@/hooks/usePagination.ts'
import type { PageResponse } from '@/types/index.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Pencil, Plus, Trash2 } from 'lucide-react'
import { useState, type ReactNode } from 'react'

interface CrudPageProps<T extends { id: string }> {
  title: string
  subtitle?: string
  queryKey: string
  fetchFn: (params: { page: number; size: number; search?: string }) => Promise<PageResponse<T>>
  deleteFn?: (id: string) => Promise<unknown>
  columns: Column<T>[]
  renderForm: (props: { onClose: () => void; editItem?: T }) => ReactNode
  searchPlaceholder?: string
}

export default function CrudPage<T extends { id: string }>({
  title,
  subtitle,
  queryKey,
  fetchFn,
  deleteFn,
  columns,
  renderForm,
  searchPlaceholder,
}: CrudPageProps<T>) {
  const [search, setSearch] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editItem, setEditItem] = useState<T | undefined>()
  const debouncedSearch = useDebounce(search)
  const { page, setPage } = usePagination()
  const queryClient = useQueryClient()

  const { data, isLoading } = useQuery({
    queryKey: [queryKey, page, debouncedSearch],
    queryFn: () => fetchFn({ page, size: 10, search: debouncedSearch || undefined }),
  })

  const deleteMutation = useMutation({
    mutationFn: (id: string) => deleteFn!(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: [queryKey] }),
  })

  const handleEdit = (item: T) => {
    setEditItem(item)
    setModalOpen(true)
  }

  const handleCreate = () => {
    setEditItem(undefined)
    setModalOpen(true)
  }

  const handleClose = () => {
    setModalOpen(false)
    setEditItem(undefined)
    queryClient.invalidateQueries({ queryKey: [queryKey] })
  }

  if (isLoading && !data) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="page-title">{title}</h1>
          {subtitle && <p className="page-subtitle">{subtitle}</p>}
        </div>
        <button type="button" onClick={handleCreate} className="btn-primary flex items-center gap-2">
          <Plus className="h-4 w-4" />
          Add New
        </button>
      </div>

      <SearchBar value={search} onChange={setSearch} placeholder={searchPlaceholder ?? `Search ${title.toLowerCase()}...`} />

      <DataTable
        columns={columns}
        data={data?.content ?? []}
        keyExtractor={(item) => item.id}
        isLoading={isLoading}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
        actions={(item) => (
          <div className="flex gap-2">
            <button type="button" onClick={() => handleEdit(item)} className="rounded p-1 text-sky-600 hover:bg-sky-50 dark:hover:bg-sky-950">
              <Pencil className="h-4 w-4" />
            </button>
            {deleteFn && (
              <button
                type="button"
                onClick={() => {
                  if (window.confirm('Are you sure you want to delete this record?')) {
                    deleteMutation.mutate(item.id)
                  }
                }}
                className="rounded p-1 text-red-600 hover:bg-red-50 dark:hover:bg-red-950"
              >
                <Trash2 className="h-4 w-4" />
              </button>
            )}
          </div>
        )}
      />

      <Modal isOpen={modalOpen} onClose={handleClose} title={editItem ? `Edit ${title}` : `Add ${title}`}>
        {renderForm({ onClose: handleClose, editItem })}
      </Modal>
    </div>
  )
}
