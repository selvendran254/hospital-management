import FeedbackForm from '@/components/FeedbackForm.tsx'
import { useState } from 'react'

export default function PatientFeedbackPage() {
  const [recentFeedback, setRecentFeedback] = useState<{ rating: number; comment: string } | null>(null)

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Patient Feedback</h1>
        <p className="page-subtitle">Submit post-appointment feedback to improve care quality.</p>
      </div>
      <FeedbackForm doctorName="Meera Nair" onSubmit={(payload) => setRecentFeedback(payload)} />
      {recentFeedback && (
        <div className="card">
          <p className="text-sm text-slate-500 dark:text-slate-400">Latest feedback received</p>
          <p className="mt-2 text-sm font-medium text-slate-900 dark:text-white">
            Rating: {recentFeedback.rating}/5
          </p>
          <p className="mt-1 text-sm text-slate-600 dark:text-slate-300">{recentFeedback.comment || 'No comment'}</p>
        </div>
      )}
    </div>
  )
}
