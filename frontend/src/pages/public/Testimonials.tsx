import TestimonialsSection from '@/components/TestimonialsSection.tsx'

export default function TestimonialsPage() {
  return (
    <div className="py-6">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <h1 className="page-title">Testimonials</h1>
        <p className="page-subtitle">Patient feedback and treatment journeys.</p>
      </div>
      <TestimonialsSection />
    </div>
  )
}
