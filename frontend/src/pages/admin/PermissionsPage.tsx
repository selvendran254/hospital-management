const permissions = [
  { role: 'Receptionist', access: ['Queue', 'Billing', 'Appointments'] },
  { role: 'Doctor', access: ['Patients', 'Prescriptions', 'Telemedicine'] },
  { role: 'Lab Staff', access: ['Tests', 'Reports', 'Auto email'] },
]

export default function PermissionsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Permissions Management</h1>
        <p className="page-subtitle">Role-based access control settings.</p>
      </div>
      <div className="card space-y-4">
        {permissions.map((item) => (
          <div key={item.role} className="rounded-lg border border-slate-200 p-4 dark:border-slate-700">
            <h2 className="font-semibold text-slate-900 dark:text-white">{item.role}</h2>
            <p className="mt-2 text-sm text-slate-500">{item.access.join(', ')}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
