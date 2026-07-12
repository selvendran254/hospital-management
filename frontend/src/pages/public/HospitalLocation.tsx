import GoogleMapsEmbed from '@/components/GoogleMapsEmbed.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { operationsService } from '@/api/services/operationsService.ts'
import { useQuery } from '@tanstack/react-query'
import { ExternalLink, MapPin, Navigation } from 'lucide-react'

export default function HospitalLocation() {
  const { data: hospital, isLoading } = useQuery({
    queryKey: ['hospital-location'],
    queryFn: () => operationsService.getHospitalLocation(),
  })

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  if (!hospital) return null

  return (
    <div className="mx-auto max-w-6xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Find Our Hospital</h1>
      <p className="page-subtitle">Google Maps directions to {hospital.name}</p>

      <div className="mt-8 grid gap-6 lg:grid-cols-3">
        <div className="card lg:col-span-2">
          <div className="h-96 overflow-hidden rounded-xl">
            <GoogleMapsEmbed latitude={hospital.latitude} longitude={hospital.longitude} title={hospital.name} />
          </div>
        </div>
        <div className="card space-y-4">
          <h2 className="text-lg font-semibold text-slate-900 dark:text-white">{hospital.name}</h2>
          <p className="flex items-start gap-2 text-sm text-slate-600 dark:text-slate-300">
            <MapPin className="mt-0.5 h-4 w-4 shrink-0 text-primary-500" />
            {hospital.address}
          </p>
          <a href={hospital.directionsUrl} target="_blank" rel="noreferrer" className="btn-primary flex items-center gap-2">
            <Navigation className="h-4 w-4" />
            Get Directions
          </a>
          <a href={hospital.googleMapsUrl} target="_blank" rel="noreferrer" className="btn-secondary flex items-center gap-2">
            <ExternalLink className="h-4 w-4" />
            Open in Google Maps
          </a>
        </div>
      </div>
    </div>
  )
}
