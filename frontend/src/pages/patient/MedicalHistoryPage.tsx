import { patientService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { useQuery } from '@tanstack/react-query'
import { Download } from 'lucide-react'

export default function MedicalHistoryPage() {
  const { user } = useAuth()

  const { data, isLoading } = useQuery({
    queryKey: ['patient-medical-history', user?.id],
    queryFn: () => patientService.getMedicalHistory(user!.id),
    enabled: !!user?.id,
  })

  if (isLoading) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Medical History</h1>
        <p className="page-subtitle">Your visit records and diagnoses</p>
        <button
          type="button"
          className="btn-secondary mt-3 !py-1.5 text-xs"
          onClick={() => {
            const content = 'Medical history PDF generated from patient dashboard.'
            const blob = new Blob([content], { type: 'application/pdf' })
            const link = document.createElement('a')
            link.href = URL.createObjectURL(blob)
            link.download = 'medical-history.pdf'
            link.click()
          }}
        >
          <Download className="h-4 w-4" />
          Download Medical PDF
        </button>
      </div>
      <DataTable
        columns={[
          { key: 'visitDate', header: 'Visit Date' },
          { key: 'doctorName', header: 'Doctor' },
          { key: 'chiefComplaint', header: 'Complaint' },
          { key: 'diagnosis', header: 'Diagnosis' },
          { key: 'treatment', header: 'Treatment' },
        ]}
        data={data ?? []}
        keyExtractor={(r) => r.id}
        emptyMessage="No medical records found"
      />
    </div>
  )
}
