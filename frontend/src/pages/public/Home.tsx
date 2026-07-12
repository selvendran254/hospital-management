import PricingPlansSection from '@/components/PricingPlansSection.tsx'
import TestimonialsSection from '@/components/TestimonialsSection.tsx'
import { motion } from 'framer-motion'
import {
  Activity,
  Ambulance,
  ArrowRight,
  Calendar,
  Heart,
  Shield,
  Stethoscope,
} from 'lucide-react'
import { Link } from 'react-router-dom'

const features = [
  { icon: Stethoscope, title: 'Expert Doctors', desc: 'Board-certified specialists across all departments' },
  { icon: Activity, title: 'Advanced Diagnostics', desc: 'State-of-the-art laboratory and imaging facilities' },
  { icon: Shield, title: '24/7 Emergency', desc: 'Round-the-clock emergency care with rapid response' },
  { icon: Heart, title: 'Patient-Centered', desc: 'Compassionate care tailored to your needs' },
]

const stats = [
  { value: '500+', label: 'Expert Doctors' },
  { value: '50+', label: 'Departments' },
  { value: '100K+', label: 'Patients Served' },
  { value: '24/7', label: 'Emergency Care' },
]

export default function Home() {
  return (
    <div>
      <section className="relative overflow-hidden bg-gradient-to-br from-primary-700 via-primary-600 to-sky-600 text-white">
        <div className="absolute inset-0 bg-[url('data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNjAiIGhlaWdodD0iNjAiIHZpZXdCb3g9IjAgMCA2MCA2MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPjxnIGZpbGw9IiNmZmYiIGZpbGwtb3BhY2l0eT0iMC4wNSI+PHBhdGggIGQ9Ik0zNiAzNGg0djRoLTR6TTAgMzRoNHY0SDB6TTAgMzRoNHY0SDB6Ii8+PC9nPjwvZz48L3N2Zz4=')] opacity-30" />
        <div className="relative mx-auto max-w-7xl px-4 py-20 sm:px-6 lg:px-8 lg:py-28">
          <div className="max-w-2xl">
            <h1 className="text-4xl font-bold tracking-tight sm:text-5xl lg:text-6xl">
              Your Health, Our Priority
            </h1>
            <p className="mt-6 text-lg text-primary-100">
              Experience world-class healthcare with compassionate doctors, cutting-edge technology,
              and personalized treatment plans at MediCare Hospital.
            </p>
            <div className="mt-8 flex flex-wrap gap-4">
              <Link to="/appointment" className="inline-flex items-center gap-2 rounded-lg bg-white px-6 py-3 font-semibold text-primary-700 transition hover:bg-primary-50">
                <Calendar className="h-5 w-5" />
                Book Appointment
              </Link>
              <Link to="/emergency" className="inline-flex items-center gap-2 rounded-lg border-2 border-white/30 px-6 py-3 font-semibold transition hover:bg-white/10">
                <Ambulance className="h-5 w-5" />
                Emergency
              </Link>
            </div>
          </div>
        </div>
      </section>

      <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
          {stats.map((stat) => (
            <div key={stat.label} className="card text-center">
              <p className="text-3xl font-bold text-primary-600">{stat.value}</p>
              <p className="mt-1 text-sm text-slate-500">{stat.label}</p>
            </div>
          ))}
        </div>
      </section>

      <section className="bg-slate-100 py-16 dark:bg-slate-900/50">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-slate-900 dark:text-white">Why Choose MediCare?</h2>
            <p className="mt-2 text-slate-500">Comprehensive healthcare services under one roof</p>
          </div>
          <div className="mt-12 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {features.map((f) => (
              <motion.div
                key={f.title}
                whileHover={{ y: -4 }}
                transition={{ duration: 0.2 }}
                className="card text-center"
              >
                <div className="mx-auto flex h-14 w-14 items-center justify-center rounded-full bg-primary-100 text-primary-600 dark:bg-primary-950">
                  <f.icon className="h-7 w-7" />
                </div>
                <h3 className="mt-4 font-semibold text-slate-900 dark:text-white">{f.title}</h3>
                <p className="mt-2 text-sm text-slate-500">{f.desc}</p>
              </motion.div>
            ))}
          </div>
        </div>
      </section>

      <TestimonialsSection />
      <PricingPlansSection />

      <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="card flex flex-col items-center justify-between gap-6 bg-gradient-to-r from-primary-600 to-sky-600 p-8 text-white sm:flex-row">
          <div>
            <h2 className="text-2xl font-bold">Ready to take care of your health?</h2>
            <p className="mt-2 text-primary-100">Book an appointment with our specialists today.</p>
          </div>
          <Link to="/doctors" className="inline-flex items-center gap-2 rounded-lg bg-white px-6 py-3 font-semibold text-primary-700 transition hover:bg-primary-50">
            Find a Doctor
            <ArrowRight className="h-5 w-5" />
          </Link>
        </div>
      </section>
    </div>
  )
}
