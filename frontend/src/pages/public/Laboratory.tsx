import { labService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { Clock, Microscope } from 'lucide-react'

export default function LaboratoryPublic() {
  const { data, isLoading } = useQuery({
    queryKey: ['lab', 'tests', 'public'],
    queryFn: () => labService.getTests({ page: 0, size: 50 }),
  })

  const tests = data?.content ?? []

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Laboratory Services</h1>
      <p className="page-subtitle">Advanced diagnostic testing with accurate, timely results</p>

      <div className="mt-8 grid gap-6 sm:grid-cols-3">
        <div className="card text-center">
          <Microscope className="mx-auto h-10 w-10 text-primary-600" />
          <h3 className="mt-3 font-semibold">500+ Tests</h3>
          <p className="text-sm text-slate-500">Comprehensive test menu</p>
        </div>
        <div className="card text-center">
          <Clock className="mx-auto h-10 w-10 text-primary-600" />
          <h3 className="mt-3 font-semibold">Fast Turnaround</h3>
          <p className="text-sm text-slate-500">Most results within 24 hours</p>
        </div>
        <div className="card text-center">
          <Microscope className="mx-auto h-10 w-10 text-primary-600" />
          <h3 className="mt-3 font-semibold">NABL Accredited</h3>
          <p className="text-sm text-slate-500">Highest quality standards</p>
        </div>
      </div>

      <h2 className="mt-12 text-lg font-semibold text-slate-900 dark:text-white">Available Tests</h2>
      {isLoading ? (
        <div className="flex justify-center py-12">
          <LoadingSpinner />
        </div>
      ) : tests.length > 0 ? (
        <div className="mt-4 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {tests.map((test) => (
            <div key={test.id} className="card">
              <h3 className="font-medium text-slate-900 dark:text-white">{test.name}</h3>
              <p className="mt-1 text-sm text-slate-500">{test.description}</p>
              <div className="mt-3 flex items-center justify-between text-sm">
                <span className="font-semibold text-primary-600">${test.price}</span>
                {test.turnaroundHours && (
                  <span className="text-slate-400">{test.turnaroundHours}h turnaround</span>
                )}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="mt-4 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {['Complete Blood Count', 'Lipid Profile', 'Liver Function Test', 'Thyroid Panel', 'HbA1c', 'Urinalysis'].map((name) => (
            <div key={name} className="card">
              <h3 className="font-medium text-slate-900 dark:text-white">{name}</h3>
              <p className="mt-1 text-sm text-slate-500">Standard diagnostic test</p>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
