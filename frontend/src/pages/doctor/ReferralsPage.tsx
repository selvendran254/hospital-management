import { FormInput, FormTextarea } from '@/components/FormInput.tsx'

export default function ReferralsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Referrals</h1>
        <p className="page-subtitle">Create specialist referrals and transfer notes.</p>
      </div>
      <form className="card space-y-4">
        <FormInput label="Patient Name" required />
        <FormInput label="Referred Department" required />
        <FormInput label="Referred Doctor" />
        <FormTextarea label="Clinical Summary" required />
        <button type="button" className="btn-primary">Create Referral</button>
      </form>
    </div>
  )
}
