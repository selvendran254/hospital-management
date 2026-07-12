import { Heart, Mail, MapPin, Phone } from 'lucide-react'
import { useState } from 'react'
import { Link } from 'react-router-dom'

export default function Footer() {
  const [email, setEmail] = useState('')
  const [subscribed, setSubscribed] = useState(false)

  return (
    <footer className="border-t border-slate-200 bg-slate-900 text-slate-300 dark:border-slate-800">
      <div className="mx-auto grid max-w-7xl gap-8 px-4 py-12 sm:grid-cols-2 lg:grid-cols-4 sm:px-6 lg:px-8">
        <div>
          <div className="flex items-center gap-2 text-white">
            <Heart className="h-6 w-6 fill-primary-500 text-primary-500" />
            <span className="text-lg font-bold">MediCare Hospital</span>
          </div>
          <p className="mt-3 text-sm text-slate-400">
            Providing compassionate, world-class healthcare with cutting-edge technology and expert medical professionals.
          </p>
        </div>
        <div>
          <h4 className="font-semibold text-white">Quick Links</h4>
          <ul className="mt-3 space-y-2 text-sm">
            <li><Link to="/departments" className="hover:text-primary-400">Departments</Link></li>
            <li><Link to="/doctors" className="hover:text-primary-400">Find a Doctor</Link></li>
            <li><Link to="/appointment" className="hover:text-primary-400">Book Appointment</Link></li>
            <li><Link to="/emergency" className="hover:text-primary-400">Emergency</Link></li>
          </ul>
        </div>
        <div>
          <h4 className="font-semibold text-white">Services</h4>
          <ul className="mt-3 space-y-2 text-sm">
            <li><Link to="/lab-services" className="hover:text-primary-400">Laboratory</Link></li>
            <li><Link to="/pharmacy" className="hover:text-primary-400">Pharmacy</Link></li>
            <li><Link to="/blood-bank" className="hover:text-primary-400">Blood Bank</Link></li>
            <li><Link to="/health-packages" className="hover:text-primary-400">Health Packages</Link></li>
          </ul>
        </div>
        <div>
          <h4 className="font-semibold text-white">Contact</h4>
          <ul className="mt-3 space-y-3 text-sm">
            <li className="flex items-center gap-2">
              <MapPin className="h-4 w-4 shrink-0 text-primary-400" />
              123 Healthcare Ave, Medical District
            </li>
            <li className="flex items-center gap-2">
              <Phone className="h-4 w-4 shrink-0 text-primary-400" />
              +1 (555) 123-4567
            </li>
            <li className="flex items-center gap-2">
              <Mail className="h-4 w-4 shrink-0 text-primary-400" />
              info@medicare-hospital.com
            </li>
          </ul>
          <div className="mt-4">
            <h5 className="text-sm font-semibold text-white">Newsletter Signup</h5>
            <div className="mt-2 flex gap-2">
              <input
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="you@example.com"
                className="w-full rounded-lg border border-slate-700 bg-slate-800 px-3 py-2 text-xs text-white"
              />
              <button
                type="button"
                className="btn-primary !px-3 !py-2 text-xs"
                onClick={() => {
                  setSubscribed(true)
                  setEmail('')
                }}
              >
                Join
              </button>
            </div>
            {subscribed && <p className="mt-1 text-xs text-emerald-400">Subscribed successfully.</p>}
          </div>
        </div>
      </div>
      <div className="border-t border-slate-800 px-4 py-4 text-center text-sm text-slate-500 sm:px-6">
        <p>
          &copy; {new Date().getFullYear()} MediCare Hospital. All rights reserved.{' '}
          <Link to="/privacy" className="hover:text-primary-400">Privacy</Link>
          {' · '}
          <Link to="/terms" className="hover:text-primary-400">Terms</Link>
        </p>
      </div>
    </footer>
  )
}
