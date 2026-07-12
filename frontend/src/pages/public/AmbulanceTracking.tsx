import GoogleMapsEmbed from '@/components/GoogleMapsEmbed.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { operationsService } from '@/api/services/operationsService.ts'
import { useQuery } from '@tanstack/react-query'
import { Ambulance, ExternalLink, MapPin, Navigation, Phone } from 'lucide-react'

export default function AmbulanceTracking() {
  const { data: hospital, isLoading: hospitalLoading } = useQuery({
    queryKey: ['hospital-location'],
    queryFn: () => operationsService.getHospitalLocation(),
  })

  const { data: ambulances = [], isLoading: ambLoading } = useQuery({
    queryKey: ['ambulance-routes'],
    queryFn: () => operationsService.getAmbulanceRoutes(),
  })

  if (hospitalLoading || ambLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  const activeAmbulance = ambulances.find((a) => a.available) ?? ambulances[0]

  return (
    <div className="mx-auto max-w-6xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Ambulance Tracking</h1>
      <p className="page-subtitle">Live ambulance routes to hospital via Google Maps</p>

      <div className="mt-8 grid gap-6 lg:grid-cols-3">
        <div className="card lg:col-span-2">
          <div className="h-96 overflow-hidden rounded-xl">
            {activeAmbulance ? (
              <GoogleMapsEmbed
                latitude={activeAmbulance.latitude}
                longitude={activeAmbulance.longitude}
                title={`Ambulance ${activeAmbulance.vehicleNumber}`}
              />
            ) : hospital ? (
              <GoogleMapsEmbed latitude={hospital.latitude} longitude={hospital.longitude} title="Hospital" />
            ) : null}
          </div>
        </div>

        <div className="space-y-4">
          {ambulances.map((vehicle) => (
            <div key={vehicle.id} className="card">
              <div className="flex items-center justify-between">
                <div className="flex items-center gap-2 font-semibold text-slate-900 dark:text-white">
                  <Ambulance className="h-4 w-4 text-rose-500" />
                  {vehicle.vehicleNumber}
                </div>
                <span className={`rounded-full px-2 py-0.5 text-xs ${vehicle.available ? 'bg-emerald-100 text-emerald-700' : 'bg-slate-100 text-slate-500'}`}>
                  {vehicle.available ? 'Available' : 'Busy'}
                </span>
              </div>
              <p className="mt-2 text-sm text-slate-500">{vehicle.driverName}</p>
              <p className="mt-1 flex items-center gap-2 text-sm text-slate-600 dark:text-slate-300">
                <MapPin className="h-4 w-4 text-primary-500" />
                {vehicle.location}
              </p>
              <a href={`tel:${vehicle.driverPhone}`} className="mt-2 flex items-center gap-2 text-sm text-primary-600">
                <Phone className="h-3.5 w-3.5" />
                {vehicle.driverPhone}
              </a>
              <a
                href={vehicle.routeUrl}
                target="_blank"
                rel="noreferrer"
                className="mt-3 inline-flex items-center gap-1 text-sm font-medium text-primary-600 hover:underline"
              >
                <Navigation className="h-3.5 w-3.5" />
                View route to hospital
                <ExternalLink className="h-3 w-3" />
              </a>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
