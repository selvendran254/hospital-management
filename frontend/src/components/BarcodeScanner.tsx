import { ScanLine } from 'lucide-react'
import { useState } from 'react'

interface BarcodeScannerProps {
  onScan: (code: string) => void
}

export default function BarcodeScanner({ onScan }: BarcodeScannerProps) {
  const [cameraOn, setCameraOn] = useState(false)

  return (
    <div className="card space-y-3">
      <h3 className="text-lg font-semibold text-slate-900 dark:text-white">Barcode Scanner</h3>
      <div className="rounded-lg border border-dashed border-slate-300 bg-slate-100 p-4 text-center dark:border-slate-600 dark:bg-slate-800">
        <ScanLine className="mx-auto h-8 w-8 text-primary-500" />
        <p className="mt-2 text-sm text-slate-500">
          {cameraOn ? 'Camera simulation active. Click a sample code below.' : 'Enable camera simulation to scan medicine barcodes.'}
        </p>
      </div>
      <button type="button" className="btn-secondary w-full" onClick={() => setCameraOn((current) => !current)}>
        {cameraOn ? 'Disable Camera' : 'Enable Camera'}
      </button>
      {cameraOn && (
        <div className="grid grid-cols-2 gap-2">
          {['MED-2026001', 'MED-2026002', 'MED-2026003', 'MED-2026004'].map((code) => (
            <button key={code} type="button" className="rounded-lg border border-slate-300 px-3 py-2 text-sm hover:border-primary-500 dark:border-slate-600" onClick={() => onScan(code)}>
              {code}
            </button>
          ))}
        </div>
      )}
    </div>
  )
}
