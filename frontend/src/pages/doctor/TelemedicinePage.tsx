import { FormInput } from '@/components/FormInput.tsx'

export default function TelemedicinePage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Telemedicine</h1>
        <p className="page-subtitle">Manage consultation links and remote session settings.</p>
      </div>
      <div className="card space-y-4">
        <FormInput label="Meeting Link" placeholder="https://meet.example.com/room-123" />
        <FormInput label="Session Date" type="date" />
        <FormInput label="Session Time" type="time" />
        <div className="flex gap-2">
          <button type="button" className="btn-primary">Save Link</button>
          <button type="button" className="btn-secondary">Copy Invite</button>
        </div>
      </div>
    </div>
  )
}
