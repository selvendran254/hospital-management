import { roomService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput } from '@/components/FormInput.tsx'
import type { Room } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function RoomForm({ onClose, editItem }: { onClose: () => void; editItem?: Room }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Room>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Room>) =>
      editItem ? roomService.update(editItem.id, data) : roomService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Room Number" required registration={register('roomNumber', { required: 'Required' })} error={errors.roomNumber} />
      <FormInput label="Room Type" required registration={register('roomType', { required: 'Required' })} error={errors.roomType} />
      <FormInput label="Floor" type="number" registration={register('floorNumber', { valueAsNumber: true })} error={errors.floorNumber} />
      <FormInput label="Capacity" type="number" registration={register('capacity', { valueAsNumber: true })} error={errors.capacity} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function RoomsPage() {
  return (
    <CrudPage<Room>
      title="Rooms"
      queryKey="admin-rooms"
      fetchFn={(params) => roomService.getAll(params)}
      deleteFn={(id) => roomService.remove(id)}
      columns={[
        { key: 'roomNumber', header: 'Room #' },
        { key: 'roomType', header: 'Type' },
        { key: 'floorNumber', header: 'Floor' },
        { key: 'capacity', header: 'Capacity' },
      ]}
      renderForm={({ onClose, editItem }) => <RoomForm onClose={onClose} editItem={editItem} />}
    />
  )
}
