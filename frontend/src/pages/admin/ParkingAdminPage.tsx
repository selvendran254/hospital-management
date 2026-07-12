import { operationsService } from '@/api/services/operationsService.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Car, ParkingCircle } from 'lucide-react'
import { useState } from 'react'

export default function ParkingAdminPage() {
  const queryClient = useQueryClient()
  const [newSlot, setNewSlot] = useState({ slotNumber: '', floorLevel: 'Ground', slotType: 'VISITOR' })

  const { data: slots = [], isLoading } = useQuery({
    queryKey: ['parking-all'],
    queryFn: () => operationsService.getAllParking(),
  })

  const createMutation = useMutation({
    mutationFn: () => operationsService.createParkingSlot(newSlot),
    onSuccess: () => {
      setNewSlot({ slotNumber: '', floorLevel: 'Ground', slotType: 'VISITOR' })
      queryClient.invalidateQueries({ queryKey: ['parking-all'] })
    },
  })

  const releaseMutation = useMutation({
    mutationFn: (id: string) => operationsService.releaseParking(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['parking-all'] }),
  })

  const statusColor: Record<string, string> = {
    AVAILABLE: 'bg-emerald-100 text-emerald-700',
    RESERVED: 'bg-amber-100 text-amber-700',
    OCCUPIED: 'bg-rose-100 text-rose-700',
    MAINTENANCE: 'bg-slate-100 text-slate-500',
  }

  if (isLoading) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Parking Management</h1>

      <div className="card flex flex-wrap gap-3">
        <input value={newSlot.slotNumber} onChange={(e) => setNewSlot({ ...newSlot, slotNumber: e.target.value })} placeholder="Slot number" className="rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
        <input value={newSlot.floorLevel} onChange={(e) => setNewSlot({ ...newSlot, floorLevel: e.target.value })} placeholder="Floor" className="rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
        <select value={newSlot.slotType} onChange={(e) => setNewSlot({ ...newSlot, slotType: e.target.value })} className="rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800">
          <option value="VISITOR">Visitor</option>
          <option value="STAFF">Staff</option>
          <option value="DISABLED">Disabled</option>
          <option value="EMERGENCY">Emergency</option>
        </select>
        <button type="button" onClick={() => createMutation.mutate()} className="btn-primary">Add Slot</button>
      </div>

      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        {slots.map((slot) => (
          <div key={slot.id} className="card">
            <div className="flex items-center gap-2 font-semibold">
              <ParkingCircle className="h-5 w-5 text-primary-500" />
              {slot.slotNumber}
            </div>
            <p className="mt-1 text-sm text-slate-500">{slot.floorLevel} · {slot.slotType}</p>
            <span className={`mt-2 inline-block rounded-full px-2 py-0.5 text-xs ${statusColor[slot.status]}`}>{slot.status}</span>
            {slot.visitorName && (
              <div className="mt-3 text-sm text-slate-600 dark:text-slate-300">
                <p className="flex items-center gap-1"><Car className="h-3.5 w-3.5" />{slot.vehicleNumber}</p>
                <p>{slot.visitorName} · {slot.visitorPhone}</p>
              </div>
            )}
            {slot.status !== 'AVAILABLE' && (
              <button type="button" onClick={() => releaseMutation.mutate(slot.id)} className="mt-3 text-xs text-primary-600 hover:underline">
                Release Slot
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}
