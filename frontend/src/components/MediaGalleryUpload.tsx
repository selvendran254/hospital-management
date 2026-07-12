import { ImagePlus, Trash2 } from 'lucide-react'
import { type ChangeEvent, useMemo, useState } from 'react'

interface MediaGalleryUploadProps {
  label: string
}

interface UploadedMedia {
  id: string
  name: string
  previewUrl: string
}

export default function MediaGalleryUpload({ label }: MediaGalleryUploadProps) {
  const [mediaItems, setMediaItems] = useState<UploadedMedia[]>([])

  const itemCountLabel = useMemo(() => `${mediaItems.length} item${mediaItems.length === 1 ? '' : 's'}`, [mediaItems.length])

  function handleFileUpload(event: ChangeEvent<HTMLInputElement>) {
    const fileList = event.target.files
    if (!fileList) {
      return
    }

    const nextItems = Array.from(fileList).map((file) => ({
      id: `${file.name}-${file.lastModified}`,
      name: file.name,
      previewUrl: URL.createObjectURL(file),
    }))
    setMediaItems((prev) => [...prev, ...nextItems])
    event.target.value = ''
  }

  function handleRemove(id: string) {
    setMediaItems((prev) => prev.filter((item) => item.id !== id))
  }

  return (
    <section className="card space-y-4">
      <div className="flex items-center justify-between">
        <h3 className="text-base font-semibold text-slate-900 dark:text-white">{label}</h3>
        <span className="text-xs text-slate-500 dark:text-slate-400">{itemCountLabel}</span>
      </div>

      <label className="flex cursor-pointer items-center justify-center gap-2 rounded-lg border border-dashed border-slate-300 px-4 py-4 text-sm text-slate-600 hover:border-primary-400 hover:text-primary-600 dark:border-slate-700 dark:text-slate-300">
        <ImagePlus className="h-4 w-4" />
        Upload images/videos
        <input type="file" className="hidden" accept="image/*,video/*" multiple onChange={handleFileUpload} />
      </label>

      {mediaItems.length > 0 && (
        <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
          {mediaItems.map((item) => (
            <article key={item.id} className="rounded-lg border border-slate-200 p-2 dark:border-slate-700">
              <div className="aspect-video overflow-hidden rounded-md bg-slate-100 dark:bg-slate-800">
                <img src={item.previewUrl} alt={item.name} className="h-full w-full object-cover" />
              </div>
              <div className="mt-2 flex items-center justify-between gap-2">
                <p className="truncate text-xs text-slate-500 dark:text-slate-400">{item.name}</p>
                <button
                  type="button"
                  onClick={() => handleRemove(item.id)}
                  className="rounded p-1 text-slate-500 hover:bg-slate-100 hover:text-rose-600 dark:hover:bg-slate-800"
                  aria-label={`Remove ${item.name}`}
                >
                  <Trash2 className="h-4 w-4" />
                </button>
              </div>
            </article>
          ))}
        </div>
      )}
    </section>
  )
}
