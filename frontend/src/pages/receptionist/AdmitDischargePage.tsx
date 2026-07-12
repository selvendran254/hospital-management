import { billingService, patientService } from '@/api/services/index.ts'
import { FormInput, FormSelect } from '@/components/FormInput.tsx'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

interface AdmitForm {
  patientId: string
  bedId: string
  diagnosis: string
}

export default function AdmitDischargePage() {
  const queryClient = useQueryClient()
  const { register, handleSubmit, reset, formState: { errors } } = useForm<AdmitForm>()

  const { data: patients, isLoading: loadingPatients } = useQuery({
    queryKey: ['patients-admit'],
    queryFn: () => patientService.getAll({ page: 0, size: 100 }),
  })

  const { data: admissions, isLoading: loadingAdmissions } = useQuery({
    queryKey: ['admissions'],
    queryFn: () => billingService.getAdmissions({ page: 0, size: 10 }),
  })

  const admitMutation = useMutation({
    mutationFn: (data: AdmitForm) =>
      billingService.admit({ ...data, admissionDate: new Date().toISOString().split('T')[0], status: 'ADMITTED' }),
    onSuccess: () => {
      reset()
      queryClient.invalidateQueries({ queryKey: ['admissions'] })
    },
  })

  const dischargeMutation = useMutation({
    mutationFn: (id: string) => billingService.discharge(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admissions'] }),
  })

  if (loadingPatients || loadingAdmissions) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  const patientOptions = (patients?.content ?? []).map((p) => ({
    value: p.id,
    label: p.fullName ?? p.email ?? p.id,
  }))

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Admit / Discharge</h1>
        <p className="page-subtitle">Manage inpatient admissions</p>
      </div>
      <form onSubmit={handleSubmit((data) => admitMutation.mutate(data))} className="card space-y-4">
        <FormSelect label="Patient" required options={patientOptions} registration={register('patientId', { required: 'Required' })} error={errors.patientId} />
        <FormInput label="Bed ID" required registration={register('bedId', { required: 'Required' })} error={errors.bedId} />
        <FormInput label="Diagnosis" registration={register('diagnosis')} error={errors.diagnosis} />
        <button type="submit" disabled={admitMutation.isPending} className="btn-primary">Admit Patient</button>
      </form>
      <DataTable
        columns={[
          { key: 'patientName', header: 'Patient' },
          { key: 'bedNumber', header: 'Bed' },
          { key: 'roomNumber', header: 'Room' },
          { key: 'admissionDate', header: 'Admitted' },
          { key: 'status', header: 'Status' },
        ]}
        data={admissions?.content ?? []}
        keyExtractor={(a) => a.id}
        actions={(a) =>
          a.status === 'ADMITTED' ? (
            <button type="button" onClick={() => dischargeMutation.mutate(a.id)} className="btn-secondary text-xs">
              Discharge
            </button>
          ) : null
        }
      />
    </div>
  )
}
