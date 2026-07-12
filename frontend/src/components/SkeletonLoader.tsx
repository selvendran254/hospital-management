interface SkeletonLoaderProps {
  rows?: number
  className?: string
}

export default function SkeletonLoader({ rows = 4, className = '' }: SkeletonLoaderProps) {
  return (
    <div className={`space-y-3 ${className}`} role="status" aria-live="polite" aria-label="Loading content">
      {Array.from({ length: rows }).map((_, index) => (
        <div
          key={`skeleton-row-${index + 1}`}
          className="h-5 w-full animate-pulse rounded-md bg-slate-200 dark:bg-slate-700"
        />
      ))}
    </div>
  )
}
