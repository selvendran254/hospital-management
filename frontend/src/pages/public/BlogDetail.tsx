import { blogService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useQuery } from '@tanstack/react-query'
import { ArrowLeft, Calendar, User } from 'lucide-react'
import { Link, useParams } from 'react-router-dom'

export default function BlogDetail() {
  const { id } = useParams<{ id: string }>()

  const { data: post, isLoading, isError } = useQuery({
    queryKey: ['blog', id],
    queryFn: () => blogService.getById(id!),
    enabled: !!id,
  })

  if (isLoading) {
    return (
      <div className="flex justify-center py-20">
        <LoadingSpinner size="lg" />
      </div>
    )
  }

  if (isError || !post) {
    return (
      <div className="mx-auto max-w-3xl px-4 py-12 text-center">
        <p className="text-slate-500">Article not found.</p>
        <Link to="/blog" className="mt-4 inline-block text-primary-600 hover:underline">
          Back to blog
        </Link>
      </div>
    )
  }

  return (
    <article className="mx-auto max-w-3xl px-4 py-12 sm:px-6 lg:px-8">
      <Link to="/blog" className="inline-flex items-center gap-2 text-sm text-primary-600 hover:underline">
        <ArrowLeft className="h-4 w-4" />
        Back to blog
      </Link>

      <div className="mt-6 h-56 rounded-2xl bg-gradient-to-br from-primary-100 via-sky-100 to-teal-100 dark:from-primary-950 dark:via-sky-950 dark:to-teal-950" />

      <header className="mt-8">
        <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-white sm:text-4xl">
          {post.title}
        </h1>
        <div className="mt-4 flex flex-wrap items-center gap-4 text-sm text-slate-500">
          {post.authorName && (
            <span className="flex items-center gap-1.5">
              <User className="h-4 w-4" />
              {post.authorName}
            </span>
          )}
          {post.publishedAt && (
            <span className="flex items-center gap-1.5">
              <Calendar className="h-4 w-4" />
              {new Date(post.publishedAt).toLocaleDateString('en-IN', {
                day: 'numeric',
                month: 'long',
                year: 'numeric',
              })}
            </span>
          )}
        </div>
        {post.excerpt && (
          <p className="mt-4 text-lg text-slate-600 dark:text-slate-400">{post.excerpt}</p>
        )}
      </header>

      <div className="prose prose-slate mt-8 max-w-none dark:prose-invert">
        <div className="whitespace-pre-wrap text-slate-700 leading-relaxed dark:text-slate-300">
          {post.content}
        </div>
      </div>
    </article>
  )
}
