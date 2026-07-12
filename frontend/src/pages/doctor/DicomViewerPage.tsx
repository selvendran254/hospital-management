import { ZoomIn, ZoomOut } from 'lucide-react'
import { useState } from 'react'

const XRAY_IMAGE_URL = 'https://images.unsplash.com/photo-1582719478250-c89cae4dc85b?auto=format&fit=crop&w=1200&q=80'

export default function DicomViewerPage() {
  const [zoom, setZoom] = useState(1)
  const [offset, setOffset] = useState({ x: 0, y: 0 })
  const [dragStart, setDragStart] = useState<{ x: number; y: number } | null>(null)

  function handleWheel(event: React.WheelEvent<HTMLDivElement>) {
    event.preventDefault()
    setZoom((prev) => Math.min(3, Math.max(0.6, prev + (event.deltaY < 0 ? 0.1 : -0.1))))
  }

  function handleMouseMove(event: React.MouseEvent<HTMLDivElement>) {
    if (!dragStart) {
      return
    }
    setOffset((prev) => ({
      x: prev.x + event.clientX - dragStart.x,
      y: prev.y + event.clientY - dragStart.y,
    }))
    setDragStart({ x: event.clientX, y: event.clientY })
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">DICOM Viewer</h1>
        <p className="page-subtitle">Review X-ray images with zoom and pan controls.</p>
      </div>
      <div className="card space-y-4">
        <div className="flex gap-2">
          <button type="button" className="btn-secondary !py-1.5" onClick={() => setZoom((z) => Math.min(3, z + 0.1))}>
            <ZoomIn className="h-4 w-4" />
          </button>
          <button type="button" className="btn-secondary !py-1.5" onClick={() => setZoom((z) => Math.max(0.6, z - 0.1))}>
            <ZoomOut className="h-4 w-4" />
          </button>
          <button type="button" className="btn-secondary !py-1.5" onClick={() => { setZoom(1); setOffset({ x: 0, y: 0 }) }}>
            Reset
          </button>
        </div>
        <div
          className="relative h-[420px] overflow-hidden rounded-xl border border-slate-200 bg-black dark:border-slate-700"
          onWheel={handleWheel}
          onMouseMove={handleMouseMove}
          onMouseDown={(event) => setDragStart({ x: event.clientX, y: event.clientY })}
          onMouseUp={() => setDragStart(null)}
          onMouseLeave={() => setDragStart(null)}
        >
          <img
            src={XRAY_IMAGE_URL}
            alt="X-ray"
            draggable={false}
            className="absolute left-1/2 top-1/2 max-h-none max-w-none select-none"
            style={{
              transform: `translate(calc(-50% + ${offset.x}px), calc(-50% + ${offset.y}px)) scale(${zoom})`,
              transformOrigin: 'center',
            }}
          />
        </div>
      </div>
    </div>
  )
}
