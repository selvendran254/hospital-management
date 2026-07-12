import GlobalSearch from '@/components/GlobalSearch.tsx'
import MobileBottomNav from '@/components/MobileBottomNav.tsx'
import NotificationBell from '@/components/NotificationBell.tsx'
import Sidebar, { type SidebarItem } from '@/components/Sidebar.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { AnimatePresence, motion } from 'framer-motion'
import { LogOut, Menu, X } from 'lucide-react'
import { useState } from 'react'
import { Link, Outlet, useLocation } from 'react-router-dom'

interface DashboardLayoutProps {
  sidebarItems: SidebarItem[]
  title?: string
}

export default function DashboardLayout({ sidebarItems, title }: DashboardLayoutProps) {
  const { user, logout } = useAuth()
  const [sidebarOpen, setSidebarOpen] = useState(false)
  const [collapsed, setCollapsed] = useState(false)
  const location = useLocation()

  return (
    <div className="flex h-screen overflow-hidden bg-slate-50 dark:bg-slate-950">
      <div className={`fixed inset-y-0 left-0 z-50 transform lg:relative lg:translate-x-0 ${sidebarOpen ? 'translate-x-0' : '-translate-x-full'}`}>
        <Sidebar
          items={sidebarItems}
          title={title}
          collapsed={collapsed}
          onToggleCollapse={() => setCollapsed(!collapsed)}
        />
      </div>
      {sidebarOpen && (
        <div className="fixed inset-0 z-40 bg-black/50 lg:hidden" onClick={() => setSidebarOpen(false)} aria-hidden />
      )}
      <div className="flex flex-1 flex-col overflow-hidden">
        <header className="flex items-center justify-between border-b border-slate-200 bg-white px-4 py-3 dark:border-slate-800 dark:bg-slate-900 lg:px-6">
          <button
            type="button"
            className="rounded-lg p-2 lg:hidden"
            onClick={() => setSidebarOpen(!sidebarOpen)}
            aria-label="Toggle sidebar"
          >
            {sidebarOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
          </button>
          <div className="flex flex-1 items-center justify-end gap-4">
            <div className="hidden w-64 md:block">
              <GlobalSearch placeholder="Search..." />
            </div>
            <NotificationBell />
            <span className="hidden text-sm text-slate-500 sm:inline">{user?.email}</span>
            <Link to="/" className="text-sm text-primary-600 hover:underline">
              Public Site
            </Link>
            <button type="button" onClick={logout} className="btn-secondary flex items-center gap-2 text-sm">
              <LogOut className="h-4 w-4" />
              <span className="hidden sm:inline">Logout</span>
            </button>
          </div>
        </header>
        <main className="flex-1 overflow-y-auto p-4 pb-20 lg:p-6 lg:pb-6">
          <AnimatePresence mode="wait">
            <motion.div
              key={location.pathname}
              initial={{ opacity: 0, y: 10 }}
              animate={{ opacity: 1, y: 0 }}
              exit={{ opacity: 0, y: -8 }}
              transition={{ duration: 0.2 }}
              className="page-enter"
            >
              <Outlet />
            </motion.div>
          </AnimatePresence>
        </main>
      </div>
      <MobileBottomNav items={sidebarItems} />
    </div>
  )
}
