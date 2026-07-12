import { doctorService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'

const dayNames = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday']

export default function SchedulePage() {
  const { data, isLoading } = useQuery({
    queryKey: ['doctor-schedule'],
    queryFn: () => doctorService.getSchedule(),
  })

  if (isLoading) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">My Schedule</h1>
        <p className="page-subtitle">Weekly availability schedule</p>
      </div>
      <DataTable
        columns={[
          { key: 'dayOfWeek', header: 'Day', render: (s) => <>{dayNames[s.dayOfWeek] ?? s.dayOfWeek}</> },
          { key: 'startTime', header: 'Start' },
          { key: 'endTime', header: 'End' },
          { key: 'slotDurationMins', header: 'Slot (mins)' },
          { key: 'isActive', header: 'Active', render: (s) => <>{s.isActive ? 'Yes' : 'No'}</> },
        ]}
        data={data ?? []}
        keyExtractor={(s) => s.id}
        emptyMessage="No schedule configured"
      />
    </div>
  )
}
