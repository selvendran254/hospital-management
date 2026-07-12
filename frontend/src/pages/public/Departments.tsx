import { departmentService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { Building2 } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function Departments() {
  const { data: departments, isLoading, isError } = useQuery({
    queryKey: ['departments', 'public'],
    queryFn: () => departmentService.getPublic(),
  })

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  const list = isError || !departments?.length
    ? [
        { id: '1', name: 'Cardiology', description: 'Heart and cardiovascular care' },
        { id: '2', name: 'Neurology', description: 'Brain and nervous system disorders' },
        { id: '3', name: 'Orthopedics', description: 'Bone, joint, and muscle treatment' },
        { id: '4', name: 'Pediatrics', description: 'Healthcare for children' },
        { id: '5', name: 'Oncology', description: 'Cancer diagnosis and treatment' },
        { id: '6', name: 'Emergency Medicine', description: '24/7 emergency care' },
      ]
    : departments

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Our Departments</h1>
      <p className="page-subtitle">Specialized care across multiple medical disciplines</p>
      <div className="mt-8 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        {list.map((dept) => (
          <div key={dept.id} className="card group transition hover:border-primary-300 hover:shadow-md dark:hover:border-primary-700">
            <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-primary-100 text-primary-600 dark:bg-primary-950">
              <Building2 className="h-6 w-6" />
            </div>
            <h3 className="mt-4 text-lg font-semibold text-slate-900 dark:text-white">{dept.name}</h3>
            <p className="mt-2 text-sm text-slate-500">{dept.description ?? 'Comprehensive medical services'}</p>
            <Link to={`/doctors?departmentId=${dept.id}`} className="mt-4 inline-block text-sm font-medium text-primary-600 hover:underline">
              View Doctors →
            </Link>
          </div>
        ))}
      </div>
    </div>
  )
}
