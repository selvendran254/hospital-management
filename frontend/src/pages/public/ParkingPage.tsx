import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { operationsService } from '@/api/services/operationsService.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Car, ParkingCircle } from 'lucide-react'
import { useState } from 'react'

export default function ParkingPage() {
  const queryClient = useQueryClient()
  const [form, setForm] = useState({ visitorName: '', visitorPhone: '', vehicleNumber: '' })
  const [message, setMessage] = useState('')

  const { data: slots = [], isLoading } = useQuery({
    queryKey: ['parking-available'],
    queryFn: () => operationsService.getAvailableParking(),
  })

  const { data: stats } = useQuery({
    queryKey: ['parking-stats'],
    queryFn: () => operationsService.getParkingStats(),
  })

  const reserveMutation = useMutation({
    mutationFn: (data: { visitorName: string; visitorPhone: string; vehicleNumber: string; slotId?: string }) =>
      operationsService.reserveParking(data),
    onSuccess: () => {
      setMessage('Parking reserved successfully!')
      setForm({ visitorName: '', visitorPhone: '', vehicleNumber: '' })
      queryClient.invalidateQueries({ queryKey: ['parking-available'] })
      queryClient.invalidateQueries({ queryKey: ['parking-stats'] })
    },
    onError: () => setMessage('Reservation failed. No slots may be available.'),
  })

  function handleReserve(slotId?: string) {
    if (!form.visitorName || !form.visitorPhone || !form.vehicleNumber) {
      setMessage('Please fill all fields.')
      return
    }
    reserveMutation.mutate({ ...form, slotId })
  }

  const statusColor: Record<string, string> = {
    AVAILABLE: 'bg-emerald-100 text-emerald-700',
    RESERVED: 'bg-amber-100 text-amber-700',
    OCCUPIED: 'bg-rose-100 text-rose-700',
    MAINTENANCE: 'bg-slate-100 text-slate-500',
  }

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Visitor Parking</h1>
      <p className="page-subtitle">Reserve a parking slot before your hospital visit</p>

      <div className="mt-6 flex gap-4">
        <div className="card flex items-center gap-3">
          <ParkingCircle className="h-8 w-8 text-primary-500" />
          <div>
            <p className="text-2xl font-bold text-slate-900 dark:text-white">{stats?.available ?? slots.length}</p>
            <p className="text-sm text-slate-500">Slots Available</p>
          </div>
        </div>
      </div>

      <div className="mt-8 grid gap-6 lg:grid-cols-2">
        <div className="card space-y-3">
          <h2 className="font-semibold text-slate-900 dark:text-white">Reserve Parking</h2>
          <input value={form.visitorName} onChange={(e) => setForm({ ...form, visitorName: e.target.value })} placeholder="Your name" className="w-full rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
          <input value={form.visitorPhone} onChange={(e) => setForm({ ...form, visitorPhone: e.target.value })} placeholder="Phone number" className="w-full rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
          <input value={form.vehicleNumber} onChange={(e) => setForm({ ...form, vehicleNumber: e.target.value })} placeholder="Vehicle number (TN 01 AB 1234)" className="w-full rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
          <button type="button" onClick={() => handleReserve()} disabled={reserveMutation.isPending} className="btn-primary w-full">
            Auto-assign Slot
          </button>
          {message && <p className="text-sm text-primary-600">{message}</p>}
        </div>

        <div className="card">
          <h2 className="mb-4 font-semibold text-slate-900 dark:text-white">Available Slots</h2>
          <div className="grid grid-cols-2 gap-3 sm:grid-cols-3">
            {slots.map((slot) => (
              <button
                key={slot.id}
                type="button"
                onClick={() => handleReserve(slot.id)}
                className="rounded-xl border border-slate-200 p-4 text-left transition hover:border-primary-400 hover:shadow dark:border-slate-700"
              >
                <div className="flex items-center gap-2 font-semibold">
                  <Car className="h-4 w-4 text-primary-500" />
                  {slot.slotNumber}
                </div>
                <p className="mt-1 text-xs text-slate-500">{slot.floorLevel} · {slot.slotType}</p>
                <span className={`mt-2 inline-block rounded-full px-2 py-0.5 text-xs ${statusColor[slot.status]}`}>
                  {slot.status}
                </span>
              </button>
            ))}
          </div>
          {slots.length === 0 && <p className="text-sm text-slate-500">No slots available right now.</p>}
        </div>
      </div>
    </div>
  )
}
