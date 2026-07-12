import { medicineService } from '@/api/services/index.ts'
import BarcodeScanner from '@/components/BarcodeScanner.tsx'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { usePagination } from '@/hooks/usePagination.ts'
import { useQuery } from '@tanstack/react-query'
import { useState } from 'react'

export default function PharmacistMedicinesPage() {
  const { page, setPage } = usePagination()
  const [scannedCode, setScannedCode] = useState('')

  const { data, isLoading } = useQuery({
    queryKey: ['pharmacist-medicines', page],
    queryFn: () => medicineService.getAll({ page, size: 10 }),
  })

  if (isLoading && !data) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Medicines</h1>
        <p className="page-subtitle">Pharmacy inventory</p>
      </div>
      <DataTable
        columns={[
          { key: 'name', header: 'Name' },
          { key: 'genericName', header: 'Generic' },
          { key: 'stockQuantity', header: 'Stock' },
          { key: 'unitPrice', header: 'Price', render: (m) => <>${m.unitPrice}</> },
          { key: 'expiryDate', header: 'Expiry' },
        ]}
        data={data?.content ?? []}
        keyExtractor={(m) => m.id}
        page={page}
        totalPages={data?.totalPages ?? 1}
        onPageChange={setPage}
      />
      <BarcodeScanner onScan={setScannedCode} />
      {scannedCode && (
        <p className="text-sm text-primary-600">Last scanned barcode: {scannedCode}</p>
      )}
    </div>
  )
}
