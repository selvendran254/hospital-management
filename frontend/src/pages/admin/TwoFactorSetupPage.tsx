import { ShieldCheck } from 'lucide-react'
import { useState } from 'react'

export default function TwoFactorSetupPage() {
  const [otp, setOtp] = useState('')
  const [verified, setVerified] = useState(false)
  const secret = 'HOSPITAL-2FA-SECURE'
  const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=${encodeURIComponent(secret)}`

  function handleVerify() {
    if (otp.trim().length === 6) {
      setVerified(true)
    }
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Two-Factor Authentication Setup</h1>
        <p className="page-subtitle">Enable account-level 2FA with QR onboarding and OTP verification.</p>
      </div>
      <div className="card space-y-4">
        <div className="flex flex-wrap items-center gap-6">
          <img src={qrUrl} alt="2FA QR code" className="h-44 w-44 rounded-xl border border-slate-200 p-2 dark:border-slate-700" />
          <div>
            <p className="text-sm text-slate-600 dark:text-slate-300">Scan this QR in your authenticator app.</p>
            <p className="mt-1 text-xs text-slate-500 dark:text-slate-400">Secret: {secret}</p>
          </div>
        </div>
        <div className="max-w-xs space-y-2">
          <label className="text-sm font-medium text-slate-700 dark:text-slate-300" htmlFor="otp">
            Enter 6-digit OTP
          </label>
          <input id="otp" className="input-field" value={otp} onChange={(event) => setOtp(event.target.value)} />
          <button type="button" className="btn-primary" onClick={handleVerify}>
            Verify OTP
          </button>
        </div>
        {verified && (
          <p className="inline-flex items-center gap-2 rounded-lg bg-emerald-50 px-3 py-2 text-sm text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300">
            <ShieldCheck className="h-4 w-4" />
            Two-factor authentication enabled.
          </p>
        )}
      </div>
    </div>
  )
}
