import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import { useState } from 'react'

export default function DrugInteractionCheckerPage() {
  const [result, setResult] = useState('')

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Drug Interaction Checker</h1>
        <p className="page-subtitle">Assess risk combinations before dispensing medicines.</p>
      </div>
      <div className="card space-y-4">
        <FormInput label="Primary Drug" />
        <FormInput label="Secondary Drug" />
        <FormTextarea label="Patient Allergies / Notes" />
        <button
          type="button"
          className="btn-primary"
          onClick={() => setResult('Moderate interaction risk detected. Recommend dose adjustment and counseling.')}
        >
          Check Interaction
        </button>
        {result && <p className="rounded-lg bg-amber-50 p-3 text-sm text-amber-700 dark:bg-amber-900/30 dark:text-amber-300">{result}</p>}
      </div>
    </div>
  )
}
