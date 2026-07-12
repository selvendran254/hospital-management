import { Ambulance, Clock, Phone, MapPin } from 'lucide-react'
import { useState } from 'react'

export default function Emergency() {
  const [sosSent, setSosSent] = useState(false)

  return (
    <div>
      <section className="bg-red-600 py-16 text-white">
        <div className="mx-auto max-w-7xl px-4 text-center sm:px-6 lg:px-8">
          <Ambulance className="mx-auto h-16 w-16" />
          <h1 className="mt-4 text-4xl font-bold">Emergency Services</h1>
          <p className="mt-4 text-xl text-red-100">24/7 Emergency Care — Call immediately for life-threatening situations</p>
          <a href="tel:911" className="mt-8 inline-flex items-center gap-3 rounded-xl bg-white px-8 py-4 text-2xl font-bold text-red-600 transition hover:bg-red-50">
            <Phone className="h-8 w-8" />
            Call 911
          </a>
          <button
            type="button"
            className="mt-4 inline-flex items-center gap-2 rounded-xl border border-white/40 px-6 py-3 text-lg font-semibold transition hover:bg-white/10"
            onClick={() => setSosSent(true)}
          >
            Emergency SOS
          </button>
          {sosSent && <p className="mt-2 text-sm text-red-100">SOS alert sent to emergency command desk.</p>}
        </div>
      </section>

      <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div className="grid gap-6 md:grid-cols-3">
          <div className="card text-center">
            <Clock className="mx-auto h-10 w-10 text-red-500" />
            <h3 className="mt-4 font-semibold text-slate-900 dark:text-white">24/7 Availability</h3>
            <p className="mt-2 text-sm text-slate-500">Our emergency department is always open and staffed with experienced trauma specialists.</p>
          </div>
          <div className="card text-center">
            <Ambulance className="mx-auto h-10 w-10 text-red-500" />
            <h3 className="mt-4 font-semibold text-slate-900 dark:text-white">Ambulance Service</h3>
            <p className="mt-2 text-sm text-slate-500">Fleet of advanced life support ambulances ready for rapid response.</p>
            <p className="mt-2 font-medium text-primary-600">+1 (555) 123-4567</p>
          </div>
          <div className="card text-center">
            <MapPin className="mx-auto h-10 w-10 text-red-500" />
            <h3 className="mt-4 font-semibold text-slate-900 dark:text-white">Location</h3>
            <p className="mt-2 text-sm text-slate-500">Emergency entrance on the west side of the hospital building.</p>
            <p className="mt-2 text-sm">123 Healthcare Ave, Medical District</p>
          </div>
        </div>

        <div className="mt-12 card">
          <h2 className="text-xl font-bold text-slate-900 dark:text-white">When to Visit Emergency</h2>
          <ul className="mt-4 list-inside list-disc space-y-2 text-slate-600 dark:text-slate-400">
            <li>Chest pain or difficulty breathing</li>
            <li>Severe bleeding or trauma</li>
            <li>Signs of stroke (facial drooping, arm weakness, speech difficulty)</li>
            <li>Loss of consciousness</li>
            <li>Severe allergic reactions</li>
            <li>High fever with confusion or stiff neck</li>
          </ul>
        </div>
      </div>
    </div>
  )
}
