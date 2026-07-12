import { advancedService } from '@/api/services/index.ts'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useQuery } from '@tanstack/react-query'
import { Crown, Medal, Trophy } from 'lucide-react'

const rankIcon = [Crown, Trophy, Medal]

export default function DoctorRankingPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['reports', 'doctor-ranking'],
    queryFn: () => advancedService.getDoctorRanking(),
  })

  if (isLoading) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Doctor Ranking</h1>
        <p className="page-subtitle">Leaderboard based on outcomes, patient satisfaction, and throughput.</p>
      </div>
      <div className="space-y-3">
        {(data ?? []).map((doctor, index) => {
          const Icon = rankIcon[index] ?? Medal
          return (
            <article key={doctor.id} className="card flex items-center justify-between">
              <div className="flex items-center gap-3">
                <div className="rounded-full bg-amber-100 p-2 text-amber-700 dark:bg-amber-900/30 dark:text-amber-300">
                  <Icon className="h-4 w-4" />
                </div>
                <div>
                  <p className="font-semibold text-slate-900 dark:text-white">{doctor.doctorName}</p>
                  <p className="text-sm text-slate-500 dark:text-slate-400">{doctor.patientsHandled} patients handled</p>
                </div>
              </div>
              <p className="text-xl font-bold text-primary-600 dark:text-primary-400">{doctor.score}</p>
            </article>
          )
        })}
      </div>
    </div>
  )
}
