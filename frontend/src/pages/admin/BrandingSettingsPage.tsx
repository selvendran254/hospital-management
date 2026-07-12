import MediaGalleryUpload from '@/components/MediaGalleryUpload.tsx'
import { type ChangeEvent, useState } from 'react'

export default function BrandingSettingsPage() {
  const [logoPreview, setLogoPreview] = useState<string | null>(null)
  const [primaryColor, setPrimaryColor] = useState('#14b8a6')
  const [secondaryColor, setSecondaryColor] = useState('#0ea5e9')

  function handleLogoChange(event: ChangeEvent<HTMLInputElement>) {
    const file = event.target.files?.[0]
    if (!file) {
      return
    }
    setLogoPreview(URL.createObjectURL(file))
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Branding Settings</h1>
        <p className="page-subtitle">Customize logo, theme palette, and media assets for each clinic.</p>
      </div>
      <form className="card space-y-4">
        <div className="space-y-1">
          <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Upload Logo</label>
          <input type="file" className="input-field" accept="image/*" onChange={handleLogoChange} />
        </div>
        <div className="grid gap-4 md:grid-cols-2">
          <div className="space-y-1">
            <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Primary Color</label>
            <input
              type="color"
              className="h-10 w-full rounded-lg border border-slate-300 bg-white p-1 dark:border-slate-600 dark:bg-slate-800"
              value={primaryColor}
              onChange={(event) => setPrimaryColor(event.target.value)}
            />
            <input className="input-field" value={primaryColor} onChange={(event) => setPrimaryColor(event.target.value)} />
          </div>
          <div className="space-y-1">
            <label className="text-sm font-medium text-slate-700 dark:text-slate-300">Secondary Color</label>
            <input
              type="color"
              className="h-10 w-full rounded-lg border border-slate-300 bg-white p-1 dark:border-slate-600 dark:bg-slate-800"
              value={secondaryColor}
              onChange={(event) => setSecondaryColor(event.target.value)}
            />
            <input className="input-field" value={secondaryColor} onChange={(event) => setSecondaryColor(event.target.value)} />
          </div>
        </div>
        <div className="rounded-xl border border-slate-200 p-4 dark:border-slate-700">
          <p className="text-sm font-medium text-slate-700 dark:text-slate-300">Live Preview</p>
          <div className="mt-3 flex items-center gap-4 rounded-lg p-4" style={{ backgroundColor: secondaryColor }}>
            <div
              className="flex h-12 w-12 items-center justify-center overflow-hidden rounded-full bg-white"
              style={{ border: `2px solid ${primaryColor}` }}
            >
              {logoPreview ? (
                <img src={logoPreview} alt="Logo preview" className="h-full w-full object-cover" />
              ) : (
                <span className="text-xs font-semibold" style={{ color: primaryColor }}>LOGO</span>
              )}
            </div>
            <div>
              <p className="text-sm font-semibold text-white">CityCare Hospital</p>
              <p className="text-xs text-white/80">Patient-first care experience</p>
            </div>
          </div>
        </div>
        <button type="button" className="btn-primary">Save Branding</button>
      </form>
      <MediaGalleryUpload label="Hospital Media Gallery" />
      <MediaGalleryUpload label="Doctors Media Gallery" />
    </div>
  )
}
