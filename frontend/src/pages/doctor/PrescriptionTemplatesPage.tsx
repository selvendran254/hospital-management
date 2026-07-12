const templates = [
  { name: 'Diabetes Follow-up', medicines: 'Metformin, Glimepiride', notes: 'Monitor HbA1c in 3 months' },
  { name: 'Hypertension Review', medicines: 'Amlodipine, Telmisartan', notes: 'Reduce salt and monitor BP' },
]

export default function PrescriptionTemplatesPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Prescription Templates</h1>
        <p className="page-subtitle">Reusable templates for faster prescribing.</p>
      </div>
      <div className="grid gap-4">
        {templates.map((template) => (
          <div key={template.name} className="card">
            <h2 className="font-semibold text-slate-900 dark:text-white">{template.name}</h2>
            <p className="mt-1 text-sm text-slate-500">{template.medicines}</p>
            <p className="mt-2 text-sm text-slate-600 dark:text-slate-300">{template.notes}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
