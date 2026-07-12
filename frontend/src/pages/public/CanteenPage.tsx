import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { operationsService } from '@/api/services/operationsService.ts'
import { useAuth } from '@/hooks/useAuth.ts'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Leaf, ShoppingBag, UtensilsCrossed } from 'lucide-react'
import { useState } from 'react'

export default function CanteenPage() {
  const { user } = useAuth()
  const queryClient = useQueryClient()
  const [roomNumber, setRoomNumber] = useState('')
  const [notes, setNotes] = useState('')
  const [message, setMessage] = useState('')

  const { data: menu = [], isLoading } = useQuery({
    queryKey: ['canteen-menu'],
    queryFn: () => operationsService.getCanteenMenu(),
  })

  const orderMutation = useMutation({
    mutationFn: (data: { menuItemId: string; quantity: number; roomNumber?: string; notes?: string; patientId?: string }) =>
      operationsService.placeFoodOrder(data),
    onSuccess: () => {
      setMessage('Order placed successfully! Food will be delivered to your room.')
      queryClient.invalidateQueries({ queryKey: ['canteen-orders'] })
    },
    onError: () => setMessage('Failed to place order. Please try again.'),
  })

  function handleOrder(itemId: string) {
    if (!roomNumber.trim()) {
      setMessage('Please enter your room number.')
      return
    }
    orderMutation.mutate({
      menuItemId: itemId,
      quantity: 1,
      roomNumber,
      notes,
      patientId: user?.id,
    })
  }

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  const categories = [...new Set(menu.map((m) => m.category ?? 'Other'))]

  return (
    <div className="mx-auto max-w-6xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Hospital Canteen</h1>
      <p className="page-subtitle">Order fresh meals delivered to your room</p>

      <div className="mt-6 card max-w-md space-y-3">
        <label className="block text-sm font-medium">Room Number *</label>
        <input value={roomNumber} onChange={(e) => setRoomNumber(e.target.value)} placeholder="e.g. W-101" className="w-full rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
        <label className="block text-sm font-medium">Special Instructions</label>
        <input value={notes} onChange={(e) => setNotes(e.target.value)} placeholder="No spice, less salt..." className="w-full rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
      </div>

      {message && <p className="mt-4 text-sm text-primary-600">{message}</p>}

      {categories.map((category) => (
        <div key={category} className="mt-8">
          <h2 className="flex items-center gap-2 text-lg font-semibold text-slate-900 dark:text-white">
            <UtensilsCrossed className="h-5 w-5 text-primary-500" />
            {category}
          </h2>
          <div className="mt-4 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
            {menu.filter((m) => (m.category ?? 'Other') === category).map((item) => (
              <div key={item.id} className="card flex flex-col">
                <div className="flex items-start justify-between">
                  <h3 className="font-semibold text-slate-900 dark:text-white">{item.name}</h3>
                  {item.veg ? (
                    <span className="flex items-center gap-1 rounded-full bg-green-100 px-2 py-0.5 text-xs text-green-700">
                      <Leaf className="h-3 w-3" /> Veg
                    </span>
                  ) : (
                    <span className="rounded-full bg-orange-100 px-2 py-0.5 text-xs text-orange-700">Non-Veg</span>
                  )}
                </div>
                <p className="mt-1 flex-1 text-sm text-slate-500">{item.description}</p>
                <div className="mt-4 flex items-center justify-between">
                  <span className="text-lg font-bold text-primary-600">₹{item.price}</span>
                  <button type="button" onClick={() => handleOrder(item.id)} disabled={orderMutation.isPending} className="btn-primary flex items-center gap-1 text-sm">
                    <ShoppingBag className="h-4 w-4" />
                    Order
                  </button>
                </div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </div>
  )
}
