import { blogService } from '@/api/services/index.ts'
import CrudPage from '@/components/CrudPage.tsx'
import { FormInput, FormTextarea } from '@/components/FormInput.tsx'
import type { BlogPost } from '@/types/index.ts'
import { useMutation } from '@tanstack/react-query'
import { useForm } from 'react-hook-form'

function BlogForm({ onClose, editItem }: { onClose: () => void; editItem?: BlogPost }) {
  const { register, handleSubmit, formState: { errors } } = useForm<Partial<BlogPost>>({ defaultValues: editItem ?? {} })
  const mutation = useMutation({
    mutationFn: (data: Partial<BlogPost>) =>
      editItem ? blogService.update(editItem.id, data) : blogService.create(data),
    onSuccess: onClose,
  })
  return (
    <form onSubmit={handleSubmit((data) => mutation.mutate(data))} className="space-y-4">
      <FormInput label="Title" required registration={register('title', { required: 'Required' })} error={errors.title} />
      <FormTextarea label="Excerpt" registration={register('excerpt')} error={errors.excerpt} />
      <FormTextarea label="Content" required registration={register('content', { required: 'Required' })} error={errors.content} />
      <div className="flex justify-end gap-2 pt-2">
        <button type="button" onClick={onClose} className="btn-secondary">Cancel</button>
        <button type="submit" disabled={mutation.isPending} className="btn-primary">{mutation.isPending ? 'Saving...' : 'Save'}</button>
      </div>
    </form>
  )
}

export default function BlogPage() {
  return (
    <CrudPage<BlogPost>
      title="Blog Posts"
      queryKey="admin-blog"
      fetchFn={(params) => blogService.getAll(params)}
      deleteFn={(id) => blogService.remove(id)}
      columns={[
        { key: 'title', header: 'Title' },
        { key: 'authorName', header: 'Author' },
        { key: 'publishedAt', header: 'Published', render: (p) => <>{p.publishedAt ? new Date(p.publishedAt).toLocaleDateString() : 'Draft'}</> },
      ]}
      renderForm={({ onClose, editItem }) => <BlogForm onClose={onClose} editItem={editItem} />}
    />
  )
}
