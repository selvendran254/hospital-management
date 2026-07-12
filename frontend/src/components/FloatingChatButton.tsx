import { MessageCircle, MessageSquareText } from 'lucide-react'
import { useState } from 'react'

export default function FloatingChatButton() {
  const [open, setOpen] = useState(false)

  return (
    <div className="fixed bottom-5 right-5 z-50 flex flex-col items-end gap-3">
      {open && (
        <div className="w-72 rounded-xl border border-slate-200 bg-white p-4 shadow-xl dark:border-slate-700 dark:bg-slate-900">
          <h3 className="text-sm font-semibold text-slate-900 dark:text-white">Need help?</h3>
          <p className="mt-1 text-xs text-slate-500">Our care team usually responds within 5 minutes.</p>
          <div className="mt-3 flex gap-2">
            <a
              href="https://wa.me/15551234567"
              target="_blank"
              rel="noreferrer"
              className="btn-primary flex-1 !py-1.5 text-xs"
            >
              WhatsApp
            </a>
            <button type="button" className="btn-secondary flex-1 !py-1.5 text-xs">
              Live Chat
            </button>
          </div>
        </div>
      )}
      <button
        type="button"
        onClick={() => setOpen((current) => !current)}
        className="inline-flex h-14 w-14 items-center justify-center rounded-full bg-primary-600 text-white shadow-lg transition hover:bg-primary-700"
        aria-label="Open support chat"
      >
        {open ? <MessageSquareText className="h-6 w-6" /> : <MessageCircle className="h-6 w-6" />}
      </button>
    </div>
  )
}
