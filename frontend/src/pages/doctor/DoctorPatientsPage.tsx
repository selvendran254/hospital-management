import { doctorService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import VoiceNotes from '@/components/VoiceNotes.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'

export default function DoctorPatientsPage() {
  const { page, setPage } = usePagination()
  const [notePreview, setNotePreview] = useState('')

  const { data, isLoading } = useQuery({
    queryKey: ['doctor-patients', page],
    queryFn: () => doctorService.getPatients({ page, size: 10 }),
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">My Patients</h1>
        <p className="page-subtitle">Patients under your care</p>
      </div>
      <DataTable
        columns={[
          { key: 'fullName', header: 'Name' },
          { key: 'email', header: 'Email' },
          { key: 'phone', header: 'Phone' },
          { key: 'bloodGroup', header: 'Blood Group' },
        ]}
        data={data?.content ?? []}
        keyExtractor={(p) => p.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
      <VoiceNotes onTranscriptReady={setNotePreview} />
      {notePreview && <p className="text-xs text-slate-500">Latest note: {notePreview}</p>}
    </div>
  )
}
