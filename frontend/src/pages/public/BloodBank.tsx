import { bloodService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { Droplets, Phone } from 'lucide-react'

const bloodGroups = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-']

const groupMap: Record<string, string> = {
  A_POSITIVE: 'A+',
  A_NEGATIVE: 'A-',
  B_POSITIVE: 'B+',
  B_NEGATIVE: 'B-',
  AB_POSITIVE: 'AB+',
  AB_NEGATIVE: 'AB-',
  O_POSITIVE: 'O+',
  O_NEGATIVE: 'O-',
}

export default function BloodBank() {
  const { data: inventory, isLoading } = useQuery({
    queryKey: ['blood', 'inventory'],
    queryFn: () => bloodService.getInventory(),
  })

  const stockMap = new Map(
    (inventory ?? []).map((item) => [groupMap[item.bloodGroup] ?? item.bloodGroup, item.unitsAvailable]),
  )

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Blood Bank</h1>
      <p className="page-subtitle">Blood availability and donation information</p>

      <div className="mt-8 card bg-gradient-to-r from-red-600 to-rose-600 text-white">
        <div className="flex items-center gap-4">
          <Droplets className="h-12 w-12" />
          <div>
            <h2 className="text-xl font-bold">Need Blood Urgently?</h2>
            <p className="text-red-100">Contact our blood bank 24/7</p>
            <a href="tel:+15551234567" className="mt-2 inline-flex items-center gap-2 font-semibold">
              <Phone className="h-4 w-4" />
              +1 (555) 123-4567
            </a>
          </div>
        </div>
      </div>

      <h2 className="mt-10 text-lg font-semibold text-slate-900 dark:text-white">Current Blood Stock</h2>
      {isLoading ? (
        <div className="flex justify-center py-12">
          <LoadingSpinner />
        </div>
      ) : (
        <div className="mt-4 grid grid-cols-2 gap-4 sm:grid-cols-4">
          {bloodGroups.map((group) => {
            const units = stockMap.get(group) ?? 0
            const low = units < 5
            return (
              <div
                key={group}
                className={`card text-center ${low ? 'border-red-300 dark:border-red-800' : ''}`}
              >
                <p className="text-2xl font-bold text-red-600">{group}</p>
                <p className={`mt-1 text-lg font-semibold ${low ? 'text-red-500' : 'text-slate-700 dark:text-slate-300'}`}>
                  {units} units
                </p>
                {low && <p className="mt-1 text-xs text-red-500">Low stock</p>}
              </div>
            )
          })}
        </div>
      )}

      <div className="mt-10 card">
        <h2 className="font-semibold text-slate-900 dark:text-white">Become a Blood Donor</h2>
        <p className="mt-2 text-slate-600 dark:text-slate-400">
          Your donation can save up to three lives. Visit our blood bank Monday through Saturday,
          8 AM to 6 PM. Walk-ins welcome. Must be 18+ and in good health.
        </p>
      </div>
    </div>
  )
}
