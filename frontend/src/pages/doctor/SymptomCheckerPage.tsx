import { FormTextarea } from '@/components/FormInput.tsx'
import { useState } from 'react'

export default function SymptomCheckerPage() {
  const [result, setResult] = useState('')

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Symptom Checker</h1>
        <p className="page-subtitle">Decision support for initial symptom triage.</p>
      </div>
      <div className="card space-y-4">
        <FormTextarea label="Patient Symptoms" placeholder="e.g. fever, persistent cough, fatigue..." />
        <button
          type="button"
          className="btn-primary"
          onClick={() => setResult('Suggested triage: respiratory pathway, CBC + chest X-ray, tele follow-up in 24h.')}
        >
          Analyze Symptoms
        </button>
        {result && <p className="rounded-lg bg-primary-50 p-3 text-sm text-primary-700 dark:bg-primary-900/30 dark:text-primary-200">{result}</p>}
      </div>
    </div>
  )
}
