import GlobalSearch from '@/components/GlobalSearch.tsx'
import LanguageSwitcher from '@/components/LanguageSwitcher.tsx'
import NotificationBell from '@/components/NotificationBell.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { useTheme } from '@/hooks/useTheme.ts'
import { ChevronDown, Heart, Menu, Moon, Sun, X } from 'lucide-react'
import { useState } from 'react'
import { useTranslation } from 'react-i18next'
import { Link, NavLink } from 'react-router-dom'

export default function Navbar() {
  const { isAuthenticated, logout, getDashboardPath } = useAuth()
  const { isDark, toggleTheme } = useTheme()
  const { t } = useTranslation()
  const [mobileOpen, setMobileOpen] = useState(false)
  const [moreOpen, setMoreOpen] = useState(false)
  const navLinks = [
    { to: '/', label: t('nav.home') },
    { to: '/about', label: t('nav.about') },
    { to: '/departments', label: t('nav.departments') },
    { to: '/doctors', label: t('nav.doctors') },
    { to: '/services', label: t('nav.services') },
    { to: '/appointment', label: t('nav.appointment') },
  ]

  const moreLinks = [
    { to: '/health-packages', label: 'Health Packages' },
    { to: '/virtual-tour', label: 'Virtual Tour' },
    { to: '/testimonials', label: 'Testimonials' },
    { to: '/faq', label: 'FAQ' },
    { to: '/ambulance-tracking', label: 'Ambulance Tracking' },
    { to: '/hospital-location', label: 'Hospital Location' },
    { to: '/canteen', label: 'Canteen' },
    { to: '/parking', label: 'Parking' },
    { to: '/medicine-order', label: 'Medicine Order' },
    { to: '/subscription', label: 'Subscription' },
    { to: '/onboarding', label: 'Onboarding Wizard' },
    { to: '/queue-display', label: 'Queue Display' },
    { to: '/emergency', label: 'Emergency' },
    { to: '/blood-bank', label: 'Blood Bank' },
    { to: '/lab-services', label: 'Laboratory' },
    { to: '/pharmacy', label: 'Pharmacy' },
    { to: '/careers', label: 'Careers' },
    { to: '/blog', label: 'Blog' },
    { to: '/contact', label: 'Contact' },
  ]

  return (
    <header className="sticky top-0 z-40 border-b border-slate-200 bg-white/95 backdrop-blur dark:border-slate-800 dark:bg-slate-950/95">
      <div className="mx-auto flex max-w-7xl items-center gap-4 px-4 py-3 sm:px-6 lg:px-8">
        <Link to="/" className="flex shrink-0 items-center gap-2 text-primary-700 dark:text-primary-400">
          <Heart className="h-7 w-7 fill-current" />
          <span className="text-xl font-bold">{t('brand')}</span>
        </Link>

        <nav className="hidden items-center gap-5 xl:flex">
          {navLinks.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className={({ isActive }) =>
                `text-sm font-medium transition hover:text-primary-600 ${
                  isActive ? 'text-primary-600' : 'text-slate-600 dark:text-slate-300'
                }`
              }
            >
              {link.label}
            </NavLink>
          ))}
          <div className="relative">
            <button
              type="button"
              onClick={() => setMoreOpen(!moreOpen)}
              className="flex items-center gap-1 text-sm font-medium text-slate-600 transition hover:text-primary-600 dark:text-slate-300"
            >
              {t('nav.more')}
              <ChevronDown className={`h-4 w-4 transition ${moreOpen ? 'rotate-180' : ''}`} />
            </button>
            {moreOpen && (
              <>
                <div className="fixed inset-0 z-10" onClick={() => setMoreOpen(false)} aria-hidden />
                <div className="absolute right-0 top-full z-20 mt-2 w-48 rounded-xl border border-slate-200 bg-white py-2 shadow-lg dark:border-slate-700 dark:bg-slate-900">
                  {moreLinks.map((link) => (
                    <NavLink
                      key={link.to}
                      to={link.to}
                      onClick={() => setMoreOpen(false)}
                      className="block px-4 py-2 text-sm text-slate-600 hover:bg-slate-50 hover:text-primary-600 dark:text-slate-300 dark:hover:bg-slate-800"
                    >
                      {link.label}
                    </NavLink>
                  ))}
                </div>
              </>
            )}
          </div>
        </nav>

        <div className="hidden flex-1 max-w-xs lg:block xl:max-w-sm">
          <GlobalSearch />
        </div>

        <div className="ml-auto flex items-center gap-2">
          <LanguageSwitcher />
          <button
            type="button"
            onClick={toggleTheme}
            className="rounded-lg p-2 text-slate-500 hover:bg-slate-100 dark:hover:bg-slate-800"
            aria-label="Toggle theme"
          >
            {isDark ? <Sun className="h-5 w-5" /> : <Moon className="h-5 w-5" />}
          </button>
          {isAuthenticated && <NotificationBell />}
          {isAuthenticated ? (
            <div className="hidden items-center gap-3 sm:flex">
              <Link to={getDashboardPath()} className="text-sm font-medium text-slate-600 dark:text-slate-300">
                {t('nav.dashboard')}
              </Link>
              <button type="button" onClick={logout} className="btn-secondary text-sm">
                {t('nav.logout')}
              </button>
            </div>
          ) : (
            <div className="hidden items-center gap-2 sm:flex">
              <Link to="/login" className="btn-secondary text-sm">
                {t('nav.login')}
              </Link>
              <Link to="/register" className="btn-primary text-sm">
                {t('nav.register')}
              </Link>
            </div>
          )}
          <button
            type="button"
            className="rounded-lg p-2 xl:hidden"
            onClick={() => setMobileOpen(!mobileOpen)}
            aria-label="Toggle menu"
          >
            {mobileOpen ? <X className="h-6 w-6" /> : <Menu className="h-6 w-6" />}
          </button>
        </div>
      </div>

      {mobileOpen && (
        <nav className="border-t border-slate-200 px-4 py-4 xl:hidden dark:border-slate-800">
          <div className="mb-4">
            <GlobalSearch />
          </div>
          <div className="flex flex-col gap-3">
            {[...navLinks, ...moreLinks].map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                onClick={() => setMobileOpen(false)}
                className="text-sm font-medium text-slate-600 dark:text-slate-300"
              >
                {link.label}
              </NavLink>
            ))}
            {!isAuthenticated && (
              <>
                <Link to="/login" onClick={() => setMobileOpen(false)} className="btn-secondary">
                  {t('nav.login')}
                </Link>
                <Link to="/register" onClick={() => setMobileOpen(false)} className="btn-primary">
                  {t('nav.register')}
                </Link>
              </>
            )}
          </div>
        </nav>
      )}
    </header>
  )
}
