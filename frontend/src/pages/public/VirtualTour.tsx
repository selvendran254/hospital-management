const tourImages = [
  'https://images.unsplash.com/photo-1516549655169-df83a0774514?auto=format&fit=crop&w=900&q=80',
  'https://images.unsplash.com/photo-1584982751601-97dcc096659c?auto=format&fit=crop&w=900&q=80',
  'https://images.unsplash.com/photo-1631217874962-46fd0f7db6ea?auto=format&fit=crop&w=900&q=80',
  'https://images.unsplash.com/photo-1512678080530-7760d81faba6?auto=format&fit=crop&w=900&q=80',
]

export default function VirtualTour() {
  return (
    <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
      <h1 className="page-title">Virtual Tour</h1>
      <p className="page-subtitle">Explore our facilities before your visit.</p>
      <div className="mt-8 grid gap-4 sm:grid-cols-2">
        {tourImages.map((image, index) => (
          <img
            key={image}
            src={image}
            alt={`Hospital virtual tour view ${index + 1}`}
            className="h-64 w-full rounded-xl object-cover"
            loading="lazy"
          />
        ))}
      </div>
    </div>
  )
}
