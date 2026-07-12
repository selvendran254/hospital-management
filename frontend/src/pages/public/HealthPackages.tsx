import { publicService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import RazorpayCheckoutModal from '@/components/RazorpayCheckoutModal.tsx'
import { useQuery } from '@tanstack/react-query'
import { Check, Package } from 'lucide-react'
import { useState } from 'react'
import { Link } from 'react-router-dom'

const fallbackPackages = [
  { id: '1', name: 'Basic Health Check', description: 'Essential health screening', price: 99, testsIncluded: ['CBC', 'Blood Sugar', 'Lipid Profile'] },
  { id: '2', name: 'Executive Health Package', description: 'Comprehensive executive screening', price: 299, testsIncluded: ['Full Body Checkup', 'ECG', 'Chest X-Ray', 'Liver Function'] },
  { id: '3', name: 'Cardiac Screening', description: 'Heart health assessment', price: 199, testsIncluded: ['ECG', 'Echo', 'Stress Test', 'Lipid Profile'] },
]

export default function HealthPackages() {
  const [selectedPackage, setSelectedPackage] = useState<{ name: string; price: number } | null>(null)

  const { data, isLoading, isError } = useQuery({
    queryKey: ['health-packages'],
    queryFn: () => publicService.getHealthPackages(),
  })

  const packages = isError || !data?.length ? fallbackPackages : data

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Health Packages</h1>
      <p className="page-subtitle">Preventive health screening packages at affordable prices</p>
      <div className="mt-8 grid gap-6 lg:grid-cols-3">
        {packages.map((pkg) => (
          <div key={pkg.id} className="card flex flex-col">
            <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-primary-100 text-primary-600 dark:bg-primary-950">
              <Package className="h-6 w-6" />
            </div>
            <h3 className="mt-4 text-xl font-bold text-slate-900 dark:text-white">{pkg.name}</h3>
            <p className="mt-2 text-slate-500">{pkg.description}</p>
            <p className="mt-4 text-3xl font-bold text-primary-600">${pkg.price}</p>
            {pkg.testsIncluded && (
              <ul className="mt-4 flex-1 space-y-2">
                {pkg.testsIncluded.map((test) => (
                  <li key={test} className="flex items-center gap-2 text-sm text-slate-600 dark:text-slate-400">
                    <Check className="h-4 w-4 text-primary-500" />
                    {test}
                  </li>
                ))}
              </ul>
            )}
            <Link to="/appointment" className="btn-primary mt-6 text-center">
              Book Package
            </Link>
            <button
              type="button"
              className="btn-secondary mt-2 text-center"
              onClick={() => setSelectedPackage({ name: pkg.name, price: Number(pkg.price) })}
            >
              Pay Now
            </button>
          </div>
        ))}
      </div>
      <RazorpayCheckoutModal
        isOpen={!!selectedPackage}
        amount={selectedPackage?.price ?? 0}
        itemLabel={selectedPackage?.name ?? 'Health Package'}
        onClose={() => setSelectedPackage(null)}
        onSuccess={() => setSelectedPackage(null)}
      />
    </div>
  )
}
