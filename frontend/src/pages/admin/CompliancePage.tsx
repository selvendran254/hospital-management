import { advancedService } from '@/api/services/index.ts'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'

export default function CompliancePage() {
  const { data, isLoading } = useQuery({
    queryKey: ['compliance', 'policies'],
    queryFn: () => advancedService.getCompliancePolicies(),
  })
  const [consentRequired, setConsentRequired] = useState(true)

  if (isLoading) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Compliance & Consent Management</h1>
        <p className="page-subtitle">Configure data policies, consent capture, and audit readiness.</p>
      </div>
      <div className="card flex items-center justify-between">
        <div>
          <p className="font-semibold text-slate-900 dark:text-white">Require digital consent before treatment</p>
          <p className="text-sm text-slate-500 dark:text-slate-400">Applies to OP, IP, telemedicine, and surgery workflows.</p>
        </div>
        <button
          type="button"
          className={`rounded-full px-4 py-2 text-sm font-medium ${
            consentRequired ? 'bg-primary-600 text-white' : 'bg-slate-200 text-slate-700 dark:bg-slate-700 dark:text-slate-300'
          }`}
          onClick={() => setConsentRequired((prev) => !prev)}
        >
          {consentRequired ? 'Enabled' : 'Disabled'}
        </button>
      </div>
      <div className="space-y-3">
        {(data ?? []).map((policy) => (
          <article key={policy.id} className="card">
            <h3 className="text-lg font-semibold text-slate-900 dark:text-white">{policy.title}</h3>
            <p className="mt-1 text-sm text-slate-500 dark:text-slate-400">Last updated: {policy.lastUpdated}</p>
            <p className="mt-2 text-sm text-primary-600 dark:text-primary-400">Status: {policy.status}</p>
          </article>
        ))}
      </div>
    </div>
  )
}
