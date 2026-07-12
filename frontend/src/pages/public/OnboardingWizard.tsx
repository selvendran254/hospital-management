import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import { useState } from 'react'

const steps = ['Clinic info', 'Branding', 'Admin owner', 'Confirmation']

export default function OnboardingWizard() {
  const [step, setStep] = useState(0)

  return (
    <div className="mx-auto max-w-3xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Clinic Onboarding Wizard</h1>
      <p className="page-subtitle">Register your clinic and go live quickly.</p>
      <div className="mt-6 flex flex-wrap gap-2">
        {steps.map((label, index) => (
          <span
            key={label}
            className={`rounded-full px-3 py-1 text-xs ${
              index <= step
                ? 'bg-primary-600 text-white'
                : 'bg-slate-200 text-slate-600 dark:bg-slate-700 dark:text-slate-300'
            }`}
          >
            {index + 1}. {label}
          </span>
        ))}
      </div>
      <div className="card mt-6 space-y-4">
        {step === 0 && (
          <>
            <FormInput label="Clinic Name" />
            <FormInput label="City" />
            <FormTextarea label="Address" />
          </>
        )}
        {step === 1 && (
          <>
            <FormInput label="Primary Color" placeholder="#14b8a6" />
            <FormInput label="Secondary Color" placeholder="#0ea5e9" />
            <FormInput label="Logo URL" />
          </>
        )}
        {step === 2 && (
          <>
            <FormInput label="Owner Name" />
            <FormInput label="Owner Email" type="email" />
            <FormInput label="Owner Phone" />
          </>
        )}
        {step === 3 && (
          <div className="rounded-lg bg-emerald-50 p-4 text-sm text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300">
            Registration details captured. Click finish to activate your subscription and invite teams.
          </div>
        )}
        <div className="flex justify-between">
          <button type="button" className="btn-secondary" onClick={() => setStep((current) => Math.max(current - 1, 0))}>
            Back
          </button>
          <button
            type="button"
            className="btn-primary"
            onClick={() => setStep((current) => Math.min(current + 1, steps.length - 1))}
          >
            {step === steps.length - 1 ? 'Finish' : 'Next'}
          </button>
        </div>
      </div>
    </div>
  )
}
