import { Award, Building2, Target, Users } from 'lucide-react'

const values = [
  { icon: Target, title: 'Our Mission', desc: 'To deliver accessible, high-quality healthcare that improves lives and strengthens communities.' },
  { icon: Award, title: 'Excellence', desc: 'We maintain the highest standards in medical care, research, and patient safety.' },
  { icon: Users, title: 'Our Team', desc: 'Over 2,000 dedicated healthcare professionals working together for your wellbeing.' },
  { icon: Building2, title: 'Our Facility', desc: 'A 500-bed multi-specialty hospital equipped with the latest medical technology.' },
]

export default function About() {
  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <div className="max-w-3xl">
        <h1 className="page-title">About MediCare Hospital</h1>
        <p className="mt-4 text-lg text-slate-600 dark:text-slate-400">
          Founded in 1985, MediCare Hospital has been a trusted name in healthcare for nearly four decades.
          We combine clinical excellence with compassionate care to serve our community.
        </p>
      </div>

      <div className="mt-12 grid gap-6 sm:grid-cols-2">
        {values.map((v) => (
          <div key={v.title} className="card">
            <div className="flex h-12 w-12 items-center justify-center rounded-lg bg-primary-100 text-primary-600 dark:bg-primary-950">
              <v.icon className="h-6 w-6" />
            </div>
            <h3 className="mt-4 text-lg font-semibold text-slate-900 dark:text-white">{v.title}</h3>
            <p className="mt-2 text-slate-600 dark:text-slate-400">{v.desc}</p>
          </div>
        ))}
      </div>

      <div className="mt-12 card">
        <h2 className="text-xl font-bold text-slate-900 dark:text-white">Our History</h2>
        <p className="mt-4 text-slate-600 dark:text-slate-400">
          What started as a small community clinic has grown into a leading multi-specialty hospital.
          Today, we offer comprehensive services including cardiology, neurology, oncology, orthopedics,
          pediatrics, and more. Our commitment to innovation drives us to continuously upgrade our
          facilities and adopt the latest medical advancements.
        </p>
      </div>
    </div>
  )
}
