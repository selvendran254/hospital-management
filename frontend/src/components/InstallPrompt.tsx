import { Download } from 'lucide-react'
import { useRegisterSW } from 'virtual:pwa-register/react'

export default function InstallPrompt() {
  const {
    needRefresh: [needRefresh, setNeedRefresh],
    offlineReady: [offlineReady, setOfflineReady],
    updateServiceWorker,
  } = useRegisterSW()

  if (!needRefresh && !offlineReady) {
    return null
  }

  return (
    <div className="fixed bottom-5 left-5 z-50 max-w-sm rounded-xl border border-slate-200 bg-white p-4 shadow-xl dark:border-slate-700 dark:bg-slate-900">
      <h3 className="text-sm font-semibold text-slate-900 dark:text-white">App update available</h3>
      <p className="mt-1 text-xs text-slate-500">
        {offlineReady
          ? 'MediCare is ready for offline use.'
          : 'A new version is available. Refresh to update.'}
      </p>
      <div className="mt-3 flex gap-2">
        {needRefresh && (
          <button type="button" className="btn-primary !py-1.5 text-xs" onClick={() => void updateServiceWorker(true)}>
            <Download className="h-3.5 w-3.5" />
            Refresh
          </button>
        )}
        <button
          type="button"
          className="btn-secondary !py-1.5 text-xs"
          onClick={() => {
            setNeedRefresh(false)
            setOfflineReady(false)
          }}
        >
          Dismiss
        </button>
      </div>
    </div>
  )
}
