import Footer from '@/components/Footer.tsx'
import FloatingChatButton from '@/components/FloatingChatButton.tsx'
import Navbar from '@/components/Navbar.tsx'
import { Outlet } from 'react-router-dom'

export default function PublicLayout() {
  return (
    <div className="flex min-h-screen flex-col">
      <Navbar />
      <main className="flex-1">
        <Outlet />
      </main>
      <Footer />
      <FloatingChatButton />
    </div>
  )
}
