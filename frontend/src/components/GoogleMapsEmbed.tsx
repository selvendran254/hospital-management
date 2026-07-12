interface GoogleMapsEmbedProps {
  latitude: number
  longitude: number
  title?: string
  className?: string
}

export default function GoogleMapsEmbed({ latitude, longitude, title = 'Location', className = '' }: GoogleMapsEmbedProps) {
  const embedUrl = `https://maps.google.com/maps?q=${latitude},${longitude}&hl=en&z=15&output=embed`

  return (
    <iframe
      title={title}
      src={embedUrl}
      className={`h-full w-full rounded-xl border-0 ${className}`}
      loading="lazy"
      referrerPolicy="no-referrer-when-downgrade"
      allowFullScreen
    />
  )
}
