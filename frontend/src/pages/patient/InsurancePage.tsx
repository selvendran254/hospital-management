import { FormInput } from '@/components/FormInput.tsx'

export default function InsurancePage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Insurance</h1>
        <p className="page-subtitle">Manage insurer details and pre-authorization requests.</p>
      </div>
      <form className="card grid gap-4 sm:grid-cols-2">
        <FormInput label="Insurance Provider" />
        <FormInput label="Policy Number" />
        <FormInput label="Coverage Amount" type="number" />
        <FormInput label="Valid Till" type="date" />
        <div className="sm:col-span-2">
          <button type="button" className="btn-primary">Save Insurance Details</button>
        </div>
      </form>
    </div>
  )
}
