import { Star } from 'lucide-react'
import { useState } from 'react'

interface FeedbackFormProps {
  doctorName: string
  onSubmit: (payload: { rating: number; comment: string }) => void
}

export default function FeedbackForm({ doctorName, onSubmit }: FeedbackFormProps) {
  const [rating, setRating] = useState(0)
  const [comment, setComment] = useState('')

  function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault()
    if (rating < 1) {
      return
    }
    onSubmit({ rating, comment: comment.trim() })
    setRating(0)
    setComment('')
  }

  return (
    <form className="card space-y-4" onSubmit={handleSubmit}>
      <div>
        <h3 className="text-base font-semibold text-slate-900 dark:text-white">Rate Dr. {doctorName}</h3>
        <p className="text-sm text-slate-500 dark:text-slate-400">Share your post-appointment experience</p>
      </div>
      <div className="flex gap-1" role="radiogroup" aria-label="Doctor rating">
        {Array.from({ length: 5 }).map((_, index) => {
          const value = index + 1
          return (
            <button
              key={`rating-${value}`}
              type="button"
              aria-label={`Rate ${value}`}
              onClick={() => setRating(value)}
              className="rounded p-1"
            >
              <Star
                className={`h-6 w-6 ${
                  value <= rating ? 'fill-amber-400 text-amber-400' : 'text-slate-300 dark:text-slate-600'
                }`}
              />
            </button>
          )
        })}
      </div>
      <textarea
        className="input-field min-h-24"
        placeholder="Write your feedback"
        value={comment}
        onChange={(event) => setComment(event.target.value)}
      />
      <button type="submit" className="btn-primary" disabled={rating < 1}>
        Submit Feedback
      </button>
    </form>
  )
}
