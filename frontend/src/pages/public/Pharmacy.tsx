import { medicineService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { Clock, Pill, Shield } from 'lucide-react'

export default function PharmacyPublic() {
  const { data, isLoading } = useQuery({
    queryKey: ['medicines', 'public'],
    queryFn: () => medicineService.getAll({ page: 0, size: 20 }),
  })

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Hospital Pharmacy</h1>
      <p className="page-subtitle">24/7 in-house pharmacy with genuine medicines</p>

      <div className="mt-8 grid gap-6 sm:grid-cols-3">
        <div className="card text-center">
          <Clock className="mx-auto h-10 w-10 text-primary-600" />
          <h3 className="mt-3 font-semibold">Open 24/7</h3>
          <p className="text-sm text-slate-500">Always available for patients</p>
        </div>
        <div className="card text-center">
          <Shield className="mx-auto h-10 w-10 text-primary-600" />
          <h3 className="mt-3 font-semibold">Genuine Medicines</h3>
          <p className="text-sm text-slate-500">Licensed and quality assured</p>
        </div>
        <div className="card text-center">
          <Pill className="mx-auto h-10 w-10 text-primary-600" />
          <h3 className="mt-3 font-semibold">Wide Range</h3>
          <p className="text-sm text-slate-500">Prescription and OTC medicines</p>
        </div>
      </div>

      <h2 className="mt-12 text-lg font-semibold text-slate-900 dark:text-white">Available Medicines</h2>
      {isLoading ? (
        <div className="flex justify-center py-12">
          <LoadingSpinner />
        </div>
      ) : (
        <div className="mt-4 grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
          {(data?.content ?? []).length > 0 ? (
            data!.content.map((med) => (
              <div key={med.id} className="card">
                <h3 className="font-medium text-slate-900 dark:text-white">{med.name}</h3>
                {med.genericName && <p className="text-xs text-slate-500">{med.genericName}</p>}
                <p className="mt-2 text-sm font-semibold text-primary-600">${med.unitPrice}</p>
                <p className={`text-xs ${med.stockQuantity > 0 ? 'text-emerald-600' : 'text-red-500'}`}>
                  {med.stockQuantity > 0 ? 'In stock' : 'Out of stock'}
                </p>
              </div>
            ))
          ) : (
            <p className="col-span-full text-slate-500">Visit our pharmacy counter for medicine availability.</p>
          )}
        </div>
      )}
    </div>
  )
}
