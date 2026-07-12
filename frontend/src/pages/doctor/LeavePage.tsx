import { doctorService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import Modal from '@/components/Modal.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import type { DoctorLeave } from '@/types/index.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Plus } from 'lucide-react'
import { useState } from 'react'
import { useForm } from 'react-hook-form'

export default function LeavePage() {
  const [open, setOpen] = useState(false)
  const { page, setPage } = usePagination()
  const queryClient = useQueryClient()
  const { register, handleSubmit, reset, formState: { errors } } = useForm<Partial<DoctorLeave>>()

  const { data, isLoading } = useQuery({
    queryKey: ['doctor-leaves', page],
    queryFn: () => doctorService.getLeaves({ page, size: 10 }),
  })

  const mutation = useMutation({
    mutationFn: (formData: Partial<DoctorLeave>) => doctorService.requestLeave(formData),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['doctor-leaves'] })
      setOpen(false)
      reset()
    },
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="page-title">Leave Requests</h1>
          <p className="page-subtitle">Request and track leave</p>
        </div>
        <button type="button" onClick={() => setOpen(true)} className="btn-primary flex items-center gap-2">
          <Plus className="h-4 w-4" /> Request Leave
        </button>
      </div>
      <DataTable
        columns={[
          { key: 'startDate', header: 'Start' },
          { key: 'endDate', header: 'End' },
          { key: 'reason', header: 'Reason' },
          { key: 'isApproved', header: 'Status', render: (l) => <>{l.isApproved ? 'Approved' : 'Pending'}</> },
        ]}
        data={data?.content ?? []}
        keyExtractor={(l) => l.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
      <Modal isOpen={open} onClose={() => setOpen(false)} title="Request Leave">
        <form onSubmit={handleSubmit((d) => mutation.mutate(d))} className="space-y-4">
          <FormInput label="Start Date" type="date" required registration={register('startDate', { required: 'Required' })} error={errors.startDate} />
          <FormInput label="End Date" type="date" required registration={register('endDate', { required: 'Required' })} error={errors.endDate} />
          <FormTextarea label="Reason" registration={register('reason')} error={errors.reason} />
          <button type="submit" disabled={mutation.isPending} className="btn-primary w-full">{mutation.isPending ? 'Submitting...' : 'Submit Request'}</button>
        </form>
      </Modal>
    </div>
  )
}
