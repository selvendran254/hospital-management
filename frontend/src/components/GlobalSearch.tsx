import { publicService } from '@/api/services/miscService.ts'
import { useDebounce } from '@/hooks/useDebounce.ts'
import { useAuth } from '@/hooks/useAuth.ts'
import { useQuery } from '@tanstack/react-query'
import { Building2, Loader2, Pill, Search, Stethoscope, User, X } from 'lucide-react'
import { useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'

interface SearchResult {
  type: string
  id: string
  title: string
}

const typeIcons: Record<string, typeof Stethoscope> = {
  doctor: Stethoscope,
  patient: User,
  medicine: Pill,
  department: Building2,
  lab_test: Search,
}

function getResultPath(type: string, id: string, isStaff: boolean): string {
  switch (type) {
    case 'doctor':
      return `/doctors/${id}`
    case 'patient':
      return isStaff ? '/admin/patients' : '/patient/profile'
    case 'medicine':
      return isStaff ? '/pharmacist/medicines' : '/pharmacy'
    case 'department':
      return `/doctors?departmentId=${id}`
    case 'lab_test':
      return '/lab-services'
    default:
      return '/'
  }
}

interface GlobalSearchProps {
  className?: string
  placeholder?: string
}

export default function GlobalSearch({ className = '', placeholder = 'Search doctors, departments...' }: GlobalSearchProps) {
  const [open, setOpen] = useState(false)
  const [query, setQuery] = useState('')
  const debouncedQuery = useDebounce(query, 300)
  const navigate = useNavigate()
  const { isAuthenticated } = useAuth()
  const containerRef = useRef<HTMLDivElement>(null)

  const { data, isFetching } = useQuery({
    queryKey: ['global-search', debouncedQuery],
    queryFn: () => publicService.globalSearch(debouncedQuery),
    enabled: debouncedQuery.length >= 2,
  })

  const results: SearchResult[] = data?.results ?? []

  useEffect(() => {
    function handleClickOutside(e: MouseEvent) {
      if (containerRef.current && !containerRef.current.contains(e.target as Node)) {
        setOpen(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  function handleSelect(result: SearchResult) {
    navigate(getResultPath(result.type, result.id, isAuthenticated))
    setQuery('')
    setOpen(false)
  }

  return (
    <div ref={containerRef} className={`relative ${className}`}>
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
        <input
          type="search"
          value={query}
          onChange={(e) => {
            setQuery(e.target.value)
            setOpen(true)
          }}
          onFocus={() => setOpen(true)}
          placeholder={placeholder}
          className="w-full rounded-xl border border-slate-200 bg-slate-50 py-2 pl-10 pr-10 text-sm outline-none transition focus:border-primary-400 focus:ring-2 focus:ring-primary-100 dark:border-slate-700 dark:bg-slate-800 dark:focus:ring-primary-900"
        />
        {query && (
          <button
            type="button"
            onClick={() => {
              setQuery('')
              setOpen(false)
            }}
            className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
          >
            <X className="h-4 w-4" />
          </button>
        )}
      </div>

      {open && debouncedQuery.length >= 2 && (
        <div className="absolute left-0 right-0 top-full z-50 mt-2 max-h-80 overflow-y-auto rounded-xl border border-slate-200 bg-white shadow-xl dark:border-slate-700 dark:bg-slate-900">
          {isFetching ? (
            <div className="flex items-center justify-center gap-2 p-4 text-sm text-slate-500">
              <Loader2 className="h-4 w-4 animate-spin" />
              Searching...
            </div>
          ) : results.length === 0 ? (
            <p className="p-4 text-sm text-slate-500">No results for &quot;{debouncedQuery}&quot;</p>
          ) : (
            <ul>
              {results.map((result) => {
                const Icon = typeIcons[result.type] ?? Search
                return (
                  <li key={`${result.type}-${result.id}`}>
                    <button
                      type="button"
                      onClick={() => handleSelect(result)}
                      className="flex w-full items-center gap-3 px-4 py-3 text-left text-sm transition hover:bg-slate-50 dark:hover:bg-slate-800"
                    >
                      <Icon className="h-4 w-4 shrink-0 text-primary-500" />
                      <div>
                        <p className="font-medium text-slate-900 dark:text-white">{result.title}</p>
                        <p className="text-xs capitalize text-slate-500">{result.type.replace('_', ' ')}</p>
                      </div>
                    </button>
                  </li>
                )
              })}
            </ul>
          )}
        </div>
      )}
    </div>
  )
}
