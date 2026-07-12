import { FormInput } from '@/components/FormInput.tsx'

export default function InventoryAlertsConfigPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Inventory Alerts Configuration</h1>
        <p className="page-subtitle">Define low-stock thresholds and escalation rules.</p>
      </div>
      <form className="card grid gap-4 sm:grid-cols-2">
        <FormInput label="Default Reorder Level" type="number" />
        <FormInput label="Critical Alert Threshold" type="number" />
        <FormInput label="Email Recipient Group" />
        <FormInput label="SMS Recipient Group" />
        <div className="sm:col-span-2">
          <button type="button" className="btn-primary">Save Alert Settings</button>
        </div>
      </form>
    </div>
  )
}
