import { publicService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { Briefcase, MapPin } from 'lucide-react'

const fallbackCareers = [
  { id: '1', title: 'Staff Nurse', department: 'Nursing', location: 'On-site', employmentType: 'Full-time', description: 'Provide patient care in various departments.' },
  { id: '2', title: 'Lab Technician', department: 'Laboratory', location: 'On-site', employmentType: 'Full-time', description: 'Perform diagnostic tests and maintain lab equipment.' },
  { id: '3', title: 'Pharmacist', department: 'Pharmacy', location: 'On-site', employmentType: 'Full-time', description: 'Dispense medications and counsel patients.' },
]

export default function Careers() {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['careers'],
    queryFn: () => publicService.getCareers(),
  })

  const careers = isError || !data?.length ? fallbackCareers : data

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Careers at MediCare</h1>
      <p className="page-subtitle">Join our team of healthcare professionals</p>
      <div className="mt-8 space-y-4">
        {careers.map((job) => (
          <div key={job.id} className="card">
            <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
              <div>
                <h3 className="text-lg font-semibold text-slate-900 dark:text-white">{job.title}</h3>
                <p className="mt-1 text-sm text-slate-500">{job.department}</p>
                <p className="mt-2 text-slate-600 dark:text-slate-400">{job.description}</p>
                <div className="mt-2 flex flex-wrap gap-3 text-xs text-slate-500">
                  {job.location && (
                    <span className="flex items-center gap-1">
                      <MapPin className="h-3 w-3" />
                      {job.location}
                    </span>
                  )}
                  {job.employmentType && (
                    <span className="flex items-center gap-1">
                      <Briefcase className="h-3 w-3" />
                      {job.employmentType}
                    </span>
                  )}
                </div>
              </div>
              <a href="mailto:careers@medicare-hospital.com" className="btn-primary shrink-0">
                Apply Now
              </a>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
