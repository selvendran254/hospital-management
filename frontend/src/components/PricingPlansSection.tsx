import { Check } from 'lucide-react'
import { Link } from 'react-router-dom'

const plans = [
  { name: 'Starter Clinic', price: 2999, features: ['Up to 5 users', 'Appointment management', 'Patient records'] },
  { name: 'Growth Hospital', price: 7999, features: ['Up to 30 users', 'Billing + pharmacy', 'Lab and analytics'] },
  { name: 'Enterprise Network', price: 14999, features: ['Unlimited branches', 'Owner analytics', 'Priority onboarding'] },
]

export default function PricingPlansSection() {
  return (
    <section className="mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8">
      <div className="text-center">
        <h2 className="text-3xl font-bold text-slate-900 dark:text-white">Pricing Plans</h2>
        <p className="mt-2 text-slate-500">Choose a subscription tailored for your clinic size.</p>
      </div>
      <div className="mt-10 grid gap-6 md:grid-cols-3">
        {plans.map((plan) => (
          <div key={plan.name} className="card flex flex-col">
            <h3 className="text-xl font-semibold text-slate-900 dark:text-white">{plan.name}</h3>
            <p className="mt-2 text-3xl font-bold text-primary-600">INR {plan.price}/mo</p>
            <ul className="mt-4 flex-1 space-y-2">
              {plan.features.map((feature) => (
                <li key={feature} className="flex items-center gap-2 text-sm text-slate-600 dark:text-slate-300">
                  <Check className="h-4 w-4 text-primary-500" />
                  {feature}
                </li>
              ))}
            </ul>
            <Link to="/subscription" className="btn-primary mt-6 text-center">Start subscription</Link>
          </div>
        ))}
      </div>
    </section>
  )
}
