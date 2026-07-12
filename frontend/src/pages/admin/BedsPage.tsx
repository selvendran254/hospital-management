import { roomService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput } from '@/components/FormInput.tsx'
import type { Bed } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function BedForm({ onClose, editItem }: { onClose: () => void; editItem?: Bed }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Bed>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Bed>) =>
      editItem ? roomService.updateBed(editItem.id, data) : roomService.createBed(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Bed Number" required registration={register('bedNumber', { required: 'Required' })} error={errors.bedNumber} />
      <FormInput label="Room ID" required registration={register('roomId', { required: 'Required' })} error={errors.roomId} />
      <FormInput label="Status" registration={register('status')} error={errors.status} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function BedsPage() {
  return (
    <CrudPage<Bed>
      title="Beds"
      queryKey="admin-beds"
      fetchFn={(params) => roomService.getBeds(params)}
      deleteFn={(id) => roomService.removeBed(id)}
      columns={[
        { key: 'bedNumber', header: 'Bed #' },
        { key: 'roomNumber', header: 'Room' },
        { key: 'status', header: 'Status' },
        { key: 'patientName', header: 'Patient' },
      ]}
      renderForm={({ onClose, editItem }) => <BedForm onClose={onClose} editItem={editItem} />}
    />
  )
}
