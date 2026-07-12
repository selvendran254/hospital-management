import { FormInput } from '@/components/FormInput.tsx'

export default function BranchesPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Branches Management</h1>
        <p className="page-subtitle">Configure hospital branches and contact details.</p>
      </div>
      <form className="card grid gap-4 sm:grid-cols-2">
        <FormInput label="Branch Name" required />
        <FormInput label="Branch Code" required />
        <FormInput label="City" required />
        <FormInput label="Emergency Hotline" required />
        <div className="sm:col-span-2">
          <button type="button" className="btn-primary">Save Branch</button>
        </div>
      </form>
    </div>
  )
}
