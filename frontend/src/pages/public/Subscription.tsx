import PricingPlansSection from '@/components/PricingPlansSection.tsx'

export default function SubscriptionPage() {
  return (
    <div className="py-6">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <h1 className="page-title">Subscription Plans</h1>
        <p className="page-subtitle">Transparent pricing for clinics and hospital networks.</p>
      </div>
      <PricingPlansSection />
    </div>
  )
}
