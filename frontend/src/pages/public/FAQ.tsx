const faqs = [
  { q: 'How do I book an appointment?', a: 'Use the Book Appointment page and choose doctor, date, and slot.' },
  { q: 'Do you offer video consultations?', a: 'Yes, select video consultation while booking and receive a secure link.' },
  { q: 'Can I pay bills online?', a: 'Yes, bills support online UPI/card payment through the Razorpay demo flow.' },
  { q: 'How can I access reports?', a: 'Patient dashboard includes lab reports, health records, and PDF downloads.' },
]

export default function FAQ() {
  return (
    <div className="mx-auto max-w-4xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Frequently Asked Questions</h1>
      <p className="page-subtitle">Common questions from patients and caregivers.</p>
      <div className="mt-8 space-y-4">
        {faqs.map((item) => (
          <div key={item.q} className="card">
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white">{item.q}</h2>
            <p className="mt-2 text-sm text-slate-600 dark:text-slate-300">{item.a}</p>
          </div>
        ))}
      </div>
    </div>
  )
}
