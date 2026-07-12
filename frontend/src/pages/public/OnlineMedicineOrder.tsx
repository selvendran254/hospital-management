import { FormInput, FormTextarea } from '@/components/FormInput.tsx'

export default function OnlineMedicineOrder() {
  return (
    <div className="mx-auto max-w-3xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Online Medicine Order</h1>
      <p className="page-subtitle">Upload your prescription and request doorstep delivery.</p>
      <form className="card mt-8 space-y-4">
        <FormInput label="Patient Name" required />
        <FormInput label="Phone Number" required />
        <FormInput label="Delivery Address" required />
        <FormTextarea label="Medicines / Instructions" required />
        <div className="space-y-1">
          <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Prescription Upload</label>
          <input type="file" className="input-field" />
        </div>
        <button type="button" className="btn-primary w-full">Place Order</button>
      </form>
    </div>
  )
}
