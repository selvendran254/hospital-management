import { CalendarDays } from 'lucide-react'

interface DoctorCalendarProps {
  slots: string[]
  selectedSlot: string
  onSelectSlot: (slot: string) => void
}

export default function DoctorCalendar({ slots, selectedSlot, onSelectSlot }: DoctorCalendarProps) {
  return (
    <div className="space-y-3">
      <div className="flex items-center gap-2 text-sm font-medium text-slate-700 dark:text-slate-300">
        <CalendarDays className="h-4 w-4" />
        Visual Slot Picker
      </div>
      <div className="grid grid-cols-2 gap-2 sm:grid-cols-4">
        {slots.map((slot) => (
          <button
            key={slot}
            type="button"
            onClick={() => onSelectSlot(slot)}
            className={`rounded-lg border px-3 py-2 text-sm transition ${
              selectedSlot === slot
                ? 'border-primary-600 bg-primary-600 text-white'
                : 'border-slate-300 bg-white text-slate-700 hover:border-primary-400 dark:border-slate-600 dark:bg-slate-800 dark:text-slate-200'
            }`}
          >
            {slot}
          </button>
        ))}
      </div>
    </div>
  )
}
