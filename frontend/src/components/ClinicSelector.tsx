interface Clinic {
  id: string
  name: string
  city: string
}

interface ClinicSelectorProps {
  clinics: Clinic[]
  selectedClinicId: string
  onSelect: (clinicId: string) => void
}

export default function ClinicSelector({ clinics, selectedClinicId, onSelect }: ClinicSelectorProps) {
  return (
    <div className="card">
      <label className="text-sm font-medium text-slate-700 dark:text-slate-300" htmlFor="clinic-selector">
        Active Clinic
      </label>
      <select
        id="clinic-selector"
        className="input-field mt-2"
        value={selectedClinicId}
        onChange={(event) => onSelect(event.target.value)}
      >
        {clinics.map((clinic) => (
          <option key={clinic.id} value={clinic.id}>
            {clinic.name} - {clinic.city}
          </option>
        ))}
      </select>
    </div>
  )
}
