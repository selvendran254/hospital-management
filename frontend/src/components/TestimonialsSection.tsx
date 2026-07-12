const testimonials = [
  { name: 'Ananya R.', role: 'Cardiac Patient', quote: 'The doctors explained every step clearly and the follow-up care was excellent.' },
  { name: 'Vikram P.', role: 'Parent', quote: 'Pediatric emergency team responded quickly and supported us throughout treatment.' },
  { name: 'Priya S.', role: 'Executive Checkup', quote: 'Online booking, diagnostics, and reports were smooth and completed the same day.' },
]

export default function TestimonialsSection() {
  return (
    <section className="bg-slate-100 py-16 dark:bg-slate-900/40">
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <div className="text-center">
          <h2 className="text-3xl font-bold text-slate-900 dark:text-white">Patient Testimonials</h2>
          <p className="mt-2 text-slate-500">Real stories from patients and families we serve every day.</p>
        </div>
        <div className="mt-10 grid gap-6 md:grid-cols-3">
          {testimonials.map((item) => (
            <div key={item.name} className="card">
              <p className="text-sm leading-6 text-slate-600 dark:text-slate-300">"{item.quote}"</p>
              <p className="mt-4 font-semibold text-slate-900 dark:text-white">{item.name}</p>
              <p className="text-xs text-slate-500">{item.role}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
