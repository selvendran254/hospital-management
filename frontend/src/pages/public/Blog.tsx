import { blogService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { Calendar } from 'lucide-react'
import { Link } from 'react-router-dom'

const fallbackPosts = [
  { id: '1', title: 'Heart Health Tips for a Healthier Life', excerpt: 'Learn essential habits to maintain cardiovascular health.', authorName: 'Dr. Smith', publishedAt: '2026-01-15', content: '' },
  { id: '2', title: 'Understanding Diabetes Management', excerpt: 'A comprehensive guide to managing diabetes effectively.', authorName: 'Dr. Johnson', publishedAt: '2026-01-10', content: '' },
  { id: '3', title: 'The Importance of Regular Health Checkups', excerpt: 'Why preventive care matters for long-term wellness.', authorName: 'MediCare Team', publishedAt: '2026-01-05', content: '' },
]

export default function Blog() {
  const { data, isLoading, isError } = useQuery({
    queryKey: ['blog', 'public'],
    queryFn: () => blogService.getPublic({ page: 0, size: 20 }),
  })

  const posts = isError || !data?.content?.length ? fallbackPosts : data.content

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Health Blog</h1>
      <p className="page-subtitle">Health tips, medical news, and wellness advice</p>
      <div className="mt-8 grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
        {posts.map((post) => (
          <article key={post.id} className="card flex flex-col">
            <div className="h-40 rounded-lg bg-gradient-to-br from-primary-100 to-sky-100 dark:from-primary-950 dark:to-sky-950" />
            <h2 className="mt-4 text-lg font-semibold text-slate-900 dark:text-white">{post.title}</h2>
            <p className="mt-2 flex-1 text-sm text-slate-500">{post.excerpt}</p>
            <div className="mt-4 flex items-center justify-between text-xs text-slate-400">
              <span>{post.authorName}</span>
              {post.publishedAt && (
                <span className="flex items-center gap-1">
                  <Calendar className="h-3 w-3" />
                  {new Date(post.publishedAt).toLocaleDateString()}
                </span>
              )}
            </div>
            <Link to={`/blog/${post.id}`} className="mt-3 text-sm font-medium text-primary-600 hover:underline">
              Read more →
            </Link>
          </article>
        ))}
      </div>
    </div>
  )
}
