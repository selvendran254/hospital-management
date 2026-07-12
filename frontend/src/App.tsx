import AppRoutes from '@/routes/index.tsx'
import InstallPrompt from '@/components/InstallPrompt.tsx'

export default function App() {
  return (
    <>
      <AppRoutes />
      <InstallPrompt />
    </>
  )
}
