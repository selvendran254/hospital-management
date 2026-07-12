import { ambulanceService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput } from '@/components/FormInput.tsx'
import type { Ambulance } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function AmbulanceForm({ onClose, editItem }: { onClose: () => void; editItem?: Ambulance }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<Ambulance>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<Ambulance>) =>
      editItem ? ambulanceService.update(editItem.id, data) : ambulanceService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Vehicle Number" required registration={register('vehicleNumber', { required: 'Required' })} error={errors.vehicleNumber} />
      <FormInput label="Driver Name" registration={register('driverName')} error={errors.driverName} />
      <FormInput label="Driver Phone" registration={register('driverPhone')} error={errors.driverPhone} />
      <FormInput label="Status" registration={register('status')} error={errors.status} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function AmbulancesPage() {
  return (
    <CrudPage<Ambulance>
      title="Ambulances"
      queryKey="admin-ambulances"
      fetchFn={(params) => ambulanceService.getAll(params)}
      deleteFn={(id) => ambulanceService.remove(id)}
      columns={[
        { key: 'vehicleNumber', header: 'Vehicle #' },
        { key: 'driverName', header: 'Driver' },
        { key: 'status', header: 'Status' },
        { key: 'currentLocation', header: 'Location' },
      ]}
      renderForm={({ onClose, editItem }) => <AmbulanceForm onClose={onClose} editItem={editItem} />}
    />
  )
}
