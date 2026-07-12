import type { SidebarItem } from '@/components/Sidebar.tsx'
import { NavLink } from 'react-router-dom'

interface MobileBottomNavProps {
  items: SidebarItem[]
}

export default function MobileBottomNav({ items }: MobileBottomNavProps) {
  const visibleItems = items.slice(0, 5)

  return (
    <nav className="fixed inset-x-0 bottom-0 z-40 border-t border-slate-200 bg-white/95 backdrop-blur dark:border-slate-800 dark:bg-slate-900/95 lg:hidden">
      <ul className="grid grid-cols-5">
        {visibleItems.map((item) => (
          <li key={item.to}>
            <NavLink
              to={item.to}
              end={item.to.split('/').length <= 2}
              className={({ isActive }) =>
                `flex flex-col items-center gap-1 px-2 py-2 text-[11px] font-medium ${
                  isActive ? 'text-primary-600 dark:text-primary-400' : 'text-slate-500 dark:text-slate-400'
                }`
              }
            >
              <item.icon className="h-4 w-4" />
              <span className="truncate">{item.label}</span>
            </NavLink>
          </li>
        ))}
      </ul>
    </nav>
  )
}
