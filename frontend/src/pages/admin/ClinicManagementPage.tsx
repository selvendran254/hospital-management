import { advancedService } from '@/api/services/index.ts'
import ClinicSelector from '@/components/ClinicSelector.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'

export default function ClinicManagementPage() {
  const { data: clinics, isLoading } = useQuery({
    queryKey: ['admin', 'clinics'],
    queryFn: () => advancedService.getClinics(),
  })

  const [selectedClinicId, setSelectedClinicId] = useState('clinic-1')

  if (isLoading) {
    return <SkeletonLoader rows={7} className="card" />
  }

  const activeClinic = (clinics ?? []).find((clinic) => clinic.id === selectedClinicId) ?? clinics?.[0]

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Clinic Management</h1>
        <p className="page-subtitle">Multi-tenant clinic switching, status, and branch controls.</p>
      </div>
      <ClinicSelector clinics={clinics ?? []} selectedClinicId={activeClinic?.id ?? ''} onSelect={setSelectedClinicId} />
      <div className="grid gap-4 md:grid-cols-3">
        <div className="card">
          <p className="text-sm text-slate-500 dark:text-slate-400">Active Clinic</p>
          <p className="mt-2 text-lg font-semibold text-slate-900 dark:text-white">{activeClinic?.name ?? '-'}</p>
        </div>
        <div className="card">
          <p className="text-sm text-slate-500 dark:text-slate-400">City</p>
          <p className="mt-2 text-lg font-semibold text-slate-900 dark:text-white">{activeClinic?.city ?? '-'}</p>
        </div>
        <div className="card">
          <p className="text-sm text-slate-500 dark:text-slate-400">Branch Health</p>
          <p className="mt-2 text-lg font-semibold text-emerald-600">Operational</p>
        </div>
      </div>
    </div>
  )
}
