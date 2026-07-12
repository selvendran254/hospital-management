export default function VideoCallPage() {
  const roomName = 'hospital-consultation-room'
  const jitsiUrl = `https://meet.jit.si/${roomName}`

  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Telemedicine Video Call</h1>
        <p className="page-subtitle">Join your secure appointment room via Jitsi.</p>
      </div>
      <div className="card p-3">
        <iframe
          title="Jitsi Appointment Room"
          src={jitsiUrl}
          className="h-[560px] w-full rounded-lg border-0"
          allow="camera; microphone; fullscreen; display-capture; autoplay"
        />
      </div>
    </div>
  )
}
