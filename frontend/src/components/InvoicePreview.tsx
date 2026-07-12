interface InvoicePreviewProps {
  invoiceNumber: string
  patientName: string
  totalAmount: number
  dueDate: string
  hospitalName?: string
}

export default function InvoicePreview({
  invoiceNumber,
  patientName,
  totalAmount,
  dueDate,
  hospitalName = 'CityCare Hospital',
}: InvoicePreviewProps) {
  return (
    <article className="card overflow-hidden p-0">
      <header className="bg-primary-600 px-6 py-4 text-white">
        <p className="text-xs uppercase tracking-wide opacity-90">Official Invoice</p>
        <h3 className="mt-1 text-xl font-semibold">{hospitalName}</h3>
      </header>
      <div className="space-y-4 px-6 py-5">
        <div className="grid gap-4 sm:grid-cols-2">
          <div>
            <p className="text-xs uppercase text-slate-500 dark:text-slate-400">Invoice Number</p>
            <p className="text-sm font-medium text-slate-900 dark:text-white">{invoiceNumber}</p>
          </div>
          <div>
            <p className="text-xs uppercase text-slate-500 dark:text-slate-400">Due Date</p>
            <p className="text-sm font-medium text-slate-900 dark:text-white">{dueDate}</p>
          </div>
        </div>
        <div>
          <p className="text-xs uppercase text-slate-500 dark:text-slate-400">Patient</p>
          <p className="text-sm font-medium text-slate-900 dark:text-white">{patientName}</p>
        </div>
        <div className="rounded-lg bg-slate-50 p-4 dark:bg-slate-800/70">
          <p className="text-xs uppercase text-slate-500 dark:text-slate-400">Amount Payable</p>
          <p className="mt-1 text-2xl font-bold text-slate-900 dark:text-white">INR {totalAmount.toFixed(2)}</p>
        </div>
      </div>
    </article>
  )
}
