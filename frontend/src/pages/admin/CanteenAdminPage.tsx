import { operationsService, type CanteenMenuItem } from '@/api/services/operationsService.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { Plus, Trash2 } from 'lucide-react'
import { useState } from 'react'

export default function CanteenAdminPage() {
  const queryClient = useQueryClient()
  const [form, setForm] = useState({ name: '', description: '', price: '', category: 'Lunch' })

  const { data: menu = [], isLoading } = useQuery({
    queryKey: ['canteen-menu-admin'],
    queryFn: () => operationsService.getAllMenu(),
  })

  const { data: orders = [] } = useQuery({
    queryKey: ['canteen-orders'],
    queryFn: () => operationsService.getFoodOrders(),
  })

  const createMutation = useMutation({
    mutationFn: () => operationsService.createMenuItem({ ...form, price: Number(form.price), veg: true, available: true }),
    onSuccess: () => {
      setForm({ name: '', description: '', price: '', category: 'Lunch' })
      queryClient.invalidateQueries({ queryKey: ['canteen-menu-admin'] })
    },
  })

  const deleteMutation = useMutation({
    mutationFn: (id: string) => operationsService.deleteMenuItem(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['canteen-menu-admin'] }),
  })

  const statusMutation = useMutation({
    mutationFn: ({ id, status }: { id: string; status: string }) => operationsService.updateOrderStatus(id, status),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['canteen-orders'] }),
  })

  if (isLoading) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-8">
      <h1 className="text-2xl font-bold text-slate-900 dark:text-white">Canteen Management</h1>

      <div className="card flex flex-wrap gap-3">
        <input value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} placeholder="Item name" className="rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
        <input value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} placeholder="Description" className="rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
        <input value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} placeholder="Price" type="number" className="w-24 rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800" />
        <select value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} className="rounded-lg border px-3 py-2 dark:border-slate-700 dark:bg-slate-800">
          <option>Breakfast</option><option>Lunch</option><option>Dinner</option><option>Snacks</option>
        </select>
        <button type="button" onClick={() => createMutation.mutate()} className="btn-primary flex items-center gap-1"><Plus className="h-4 w-4" />Add Item</button>
      </div>

      <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
        {menu.map((item: CanteenMenuItem) => (
          <div key={item.id} className="card flex items-start justify-between">
            <div>
              <h3 className="font-semibold">{item.name}</h3>
              <p className="text-sm text-slate-500">{item.category} · ₹{item.price}</p>
            </div>
            <button type="button" onClick={() => deleteMutation.mutate(item.id)} className="text-rose-500"><Trash2 className="h-4 w-4" /></button>
          </div>
        ))}
      </div>

      <div className="card">
        <h2 className="text-lg font-semibold">Food Orders</h2>
        <table className="mt-4 w-full text-sm">
          <thead><tr className="border-b text-left text-slate-500"><th className="py-2">Item</th><th>Room</th><th>₹</th><th>Status</th><th /></tr></thead>
          <tbody>
            {orders.map((order) => (
              <tr key={order.id} className="border-b dark:border-slate-800">
                <td className="py-2">{order.itemName}</td><td>{order.roomNumber}</td><td>{order.totalAmount}</td><td>{order.status}</td>
                <td>
                  {order.status === 'PLACED' && <button type="button" onClick={() => statusMutation.mutate({ id: order.id, status: 'PREPARING' })} className="text-xs text-primary-600">Prepare</button>}
                  {order.status === 'PREPARING' && <button type="button" onClick={() => statusMutation.mutate({ id: order.id, status: 'DELIVERED' })} className="text-xs text-primary-600">Deliver</button>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  )
}
