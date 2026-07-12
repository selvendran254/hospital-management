import { billingService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import InvoicePreview from '@/components/InvoicePreview.tsx'
import RazorpayCheckoutModal from '@/components/RazorpayCheckoutModal.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'

export default function BillsPage() {
  const { page, setPage } = usePagination()
  const [selectedBill, setSelectedBill] = useState<{ id: string; amount: number } | null>(null)

  const { data, isLoading } = useQuery({
    queryKey: ['patient-bills', page],
    queryFn: () => billingService.getMyBills({ page, size: 10 }),
  })

  if (isLoading && !data) {
    return <SkeletonLoader rows={8} className="card" />
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Bills & Payments</h1>
        <p className="page-subtitle">View your billing history</p>
      </div>
      <DataTable
        columns={[
          { key: 'description', header: 'Description' },
          { key: 'totalAmount', header: 'Total', render: (b) => <>${b.totalAmount}</> },
          { key: 'paidAmount', header: 'Paid', render: (b) => <>${b.paidAmount ?? 0}</> },
          { key: 'status', header: 'Status' },
          { key: 'createdAt', header: 'Date', render: (b) => <>{b.createdAt ? new Date(b.createdAt).toLocaleDateString() : '-'}</> },
        ]}
        data={data?.content ?? []}
        keyExtractor={(b) => b.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
        actions={(bill) => (
          <button
            type="button"
            className="btn-secondary !py-1.5 text-xs"
            onClick={() => setSelectedBill({ id: bill.id, amount: Number(bill.totalAmount) })}
          >
            Pay
          </button>
        )}
      />
      {selectedBill && (
        <InvoicePreview
          invoiceNumber={selectedBill.id}
          patientName="Current Patient"
          totalAmount={selectedBill.amount}
          dueDate={new Date().toLocaleDateString()}
        />
      )}
      <RazorpayCheckoutModal
        isOpen={!!selectedBill}
        amount={selectedBill?.amount ?? 0}
        itemLabel={`Bill ${selectedBill?.id ?? ''}`}
        onClose={() => setSelectedBill(null)}
        onSuccess={() => setSelectedBill(null)}
      />
    </div>
  )
}
