import { publicService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { Activity, Brain, Bone, Heart, Microscope, Pill } from 'lucide-react'

const fallbackServices: Array<{ id: string; name: string; description: string; category: string; icon: string; price?: number }> = [
  { id: '1', name: 'Cardiology', description: 'Comprehensive heart care and surgery', category: 'Specialty', icon: 'heart' },
  { id: '2', name: 'Neurology', description: 'Brain and nervous system treatment', category: 'Specialty', icon: 'brain' },
  { id: '3', name: 'Laboratory', description: 'Advanced diagnostic testing', category: 'Diagnostics', icon: 'microscope' },
  { id: '4', name: 'Pharmacy', description: '24/7 in-house pharmacy', category: 'Support', icon: 'pill' },
  { id: '5', name: 'Orthopedics', description: 'Bone and joint care', category: 'Specialty', icon: 'bone' },
  { id: '6', name: 'Emergency Care', description: 'Round-the-clock emergency services', category: 'Emergency', icon: 'activity' },
]

const iconMap: Record<string, typeof Heart> = {
  heart: Heart,
  brain: Brain,
  microscope: Microscope,
  pill: Pill,
  bone: Bone,
  activity: Activity,
}

export default function Services() {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['services', 'public'],
    queryFn: () => publicService.getServices(),
  })

  const services = isError || !data?.length ? fallbackServices : data

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Our Services</h1>
      <p className="page-subtitle">Comprehensive healthcare services for every need</p>
      <div className="mt-8 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        {services.map((service) => {
          const Icon = iconMap[service.icon ?? 'activity'] ?? Activity
          return (
            <div key={service.id} className="card">
              <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-primary-100 text-primary-600 dark:bg-primary-950">
                <Icon className="h-6 w-6" />
              </div>
              <span className="mt-4 inline-block rounded-full bg-slate-100 px-2 py-0.5 text-xs text-slate-600 dark:bg-slate-800 dark:text-slate-400">
                {service.category}
              </span>
              <h3 className="mt-2 text-lg font-semibold text-slate-900 dark:text-white">{service.name}</h3>
              <p className="mt-2 text-sm text-slate-500">{service.description}</p>
              {service.price != null && (
                <p className="mt-3 text-sm font-medium text-primary-600">From ${service.price}</p>
              )}
            </div>
          )
        })}
      </div>
    </div>
  )
}
