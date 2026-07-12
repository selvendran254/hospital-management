import { Fingerprint, Timer } from 'lucide-react'
import { useState } from 'react'

export default function StaffAttendancePage() {
  const [scanStatus, setScanStatus] = useState<'IDLE' | 'SCANNING' | 'SUCCESS'>('IDLE')
  const [lastPunch, setLastPunch] = useState<string>('Not punched in yet')

  function simulateBiometric() {
    setScanStatus('SCANNING')
    window.setTimeout(() => {
      setScanStatus('SUCCESS')
      setLastPunch(new Date().toLocaleString())
    }, 1200)
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Staff Attendance</h1>
        <p className="page-subtitle">Biometric simulation for punch-in and shift monitoring.</p>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <section className="card space-y-4">
          <div className="flex items-center gap-2">
            <Fingerprint className="h-5 w-5 text-primary-600" />
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white">Biometric Scanner</h2>
          </div>
          <div className="rounded-xl border border-dashed border-slate-300 p-8 text-center dark:border-slate-700">
            <p className="text-sm text-slate-500 dark:text-slate-400">
              {scanStatus === 'IDLE' && 'Ready to scan fingerprint'}
              {scanStatus === 'SCANNING' && 'Scanning...'}
              {scanStatus === 'SUCCESS' && 'Attendance recorded successfully'}
            </p>
            <button type="button" className="btn-primary mt-4" onClick={simulateBiometric}>
              Simulate Finger Scan
            </button>
          </div>
        </section>
        <section className="card space-y-3">
          <h2 className="flex items-center gap-2 text-lg font-semibold text-slate-900 dark:text-white">
            <Timer className="h-5 w-5 text-primary-600" />
            Shift Snapshot
          </h2>
          <p className="text-sm text-slate-600 dark:text-slate-300">Last punch: {lastPunch}</p>
          <ul className="space-y-2 text-sm text-slate-600 dark:text-slate-300">
            <li>Morning shift attendance: 94%</li>
            <li>Night shift attendance: 89%</li>
            <li>Late arrivals today: 6</li>
          </ul>
        </section>
      </div>
    </div>
  )
}
