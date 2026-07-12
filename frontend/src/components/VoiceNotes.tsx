import { Mic, Square, Trash2 } from 'lucide-react'
import { useRef, useState } from 'react'

interface VoiceNotesProps {
  onTranscriptReady?: (value: string) => void
}

export default function VoiceNotes({ onTranscriptReady }: VoiceNotesProps) {
  const recorderRef = useRef<MediaRecorder | null>(null)
  const [isRecording, setIsRecording] = useState(false)
  const [notes, setNotes] = useState<string[]>([])

  const startRecording = async () => {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
    const recorder = new MediaRecorder(stream)

    recorder.ondataavailable = () => {
      const timestamp = new Date().toLocaleTimeString()
      const generated = `Voice note captured at ${timestamp}.`
      setNotes((current) => [generated, ...current].slice(0, 5))
      onTranscriptReady?.(generated)
    }

    recorderRef.current = recorder
    recorder.start()
    setIsRecording(true)
  }

  const stopRecording = () => {
    recorderRef.current?.stop()
    recorderRef.current = null
    setIsRecording(false)
  }

  return (
    <div className="rounded-xl border border-slate-200 p-4 dark:border-slate-700">
      <div className="flex items-center justify-between">
        <h3 className="text-sm font-semibold text-slate-900 dark:text-white">Voice Notes</h3>
        {!isRecording ? (
          <button type="button" className="btn-primary !py-1.5 text-xs" onClick={() => void startRecording()}>
            <Mic className="h-3.5 w-3.5" />
            Start
          </button>
        ) : (
          <button type="button" className="btn-secondary !py-1.5 text-xs" onClick={stopRecording}>
            <Square className="h-3.5 w-3.5" />
            Stop
          </button>
        )}
      </div>
      <div className="mt-3 space-y-2">
        {notes.length === 0 ? (
          <p className="text-xs text-slate-500">No voice notes captured yet.</p>
        ) : (
          notes.map((entry) => (
            <div key={entry} className="rounded-lg bg-slate-100 px-3 py-2 text-xs dark:bg-slate-800">
              {entry}
            </div>
          ))
        )}
      </div>
      {notes.length > 0 && (
        <button type="button" className="mt-2 inline-flex items-center gap-1 text-xs text-rose-500" onClick={() => setNotes([])}>
          <Trash2 className="h-3.5 w-3.5" />
          Clear notes
        </button>
      )}
    </div>
  )
}
