import { medicineService } from '@/api/services/index.ts'
import DataTable from '@/components/DataTable.tsx'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { AlertTriangle } from 'lucide-react'

export default function AlertsPage() {
  const { data, isLoading } = useQuery({
    queryKey: ['low-stock-medicines'],
    queryFn: () => medicineService.getLowStock(),
  })

  if (isLoading) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  const alerts = data ?? []

  return (
    <div className="space-y-6">
      <div className="flex items-center gap-3">
        <AlertTriangle className="h-6 w-6 text-amber-500" />
        <div>
          <h1 className="page-title">Stock Alerts</h1>
          <p className="page-subtitle">Medicines below reorder level</p>
        </div>
      </div>
      {alerts.length === 0 ? (
        <div className="card text-center text-slate-500">All medicines are adequately stocked.</div>
      ) : (
        <DataTable
          columns={[
            { key: 'name', header: 'Medicine' },
            { key: 'stockQuantity', header: 'Current Stock' },
            { key: 'reorderLevel', header: 'Reorder Level' },
            { key: 'category', header: 'Category' },
          ]}
          data={alerts}
          keyExtractor={(m) => m.id}
        />
      )}
    </div>
  )
}
