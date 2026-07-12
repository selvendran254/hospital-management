import DataTable from '@/components/DataTable.tsx'

const payrollData = [
  { id: '1', employee: 'Dr. Arjun', role: 'Doctor', month: 'Jul 2026', amount: 180000, status: 'Processed' },
  { id: '2', employee: 'Nurse Priya', role: 'Nursing', month: 'Jul 2026', amount: 68000, status: 'Pending' },
]

export default function PayrollPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Payroll</h1>
        <p className="page-subtitle">Monthly payroll processing and payout tracking.</p>
      </div>
      <DataTable
        columns={[
          { key: 'employee', header: 'Employee' },
          { key: 'role', header: 'Role' },
          { key: 'month', header: 'Month' },
          { key: 'amount', header: 'Amount (INR)' },
          { key: 'status', header: 'Status' },
        ]}
        data={payrollData}
        keyExtractor={(row) => row.id}
      />
    </div>
  )
}
