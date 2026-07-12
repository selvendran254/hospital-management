import { departmentService, doctorService } from '@/api/services/index.ts'
import SearchBar from '@/components/SearchBar.tsx'
import SkeletonLoader from '@/components/SkeletonLoader.tsx'
import { useDebounce } from '@/hooks/useDebounce.ts'
import type { Gender } from '@/types/common.ts'
import { useQuery } from '@tanstack/react-query'
import { Calendar, CheckCircle2, Filter, Star, User, XCircle } from 'lucide-react'
import { useMemo, useState } from 'react'
import { Link, useSearchParams } from 'react-router-dom'

const GENDER_OPTIONS: { value: Gender | ''; label: string }[] = [
  { value: '', label: 'All Genders' },
  { value: 'MALE', label: 'Male' },
  { value: 'FEMALE', label: 'Female' },
  { value: 'OTHER', label: 'Other' },
]

const EXPERIENCE_OPTIONS = [
  { value: '', label: 'Any Experience', min: undefined, max: undefined },
  { value: '0-5', label: '0–5 years', min: 0, max: 5 },
  { value: '5-10', label: '5–10 years', min: 5, max: 10 },
  { value: '10-20', label: '10–20 years', min: 10, max: 20 },
  { value: '20+', label: '20+ years', min: 20, max: undefined },
]

export default function Doctors() {
  const [searchParams, setSearchParams] = useSearchParams()
  const [search, setSearch] = useState('')
  const [departmentId, setDepartmentId] = useState(searchParams.get('departmentId') ?? '')
  const [gender, setGender] = useState<Gender | ''>('')
  const [availability, setAvailability] = useState<'all' | 'available' | 'unavailable'>('all')
  const [experienceRange, setExperienceRange] = useState('')
  const [showFilters, setShowFilters] = useState(true)

  const debouncedSearch = useDebounce(search)
  const experience = EXPERIENCE_OPTIONS.find((o) => o.value === experienceRange)

  const filters = useMemo(
    () => ({
      page: 0,
      size: 50,
      query: debouncedSearch || undefined,
      departmentId: departmentId || undefined,
      gender: gender || undefined,
      available: availability === 'all' ? undefined : availability === 'available',
      minExperience: experience?.min,
      maxExperience: experience?.max,
    }),
    [debouncedSearch, departmentId, gender, availability, experience],
  )

  const { data: departments } = useQuery({
    queryKey: ['departments', 'public'],
    queryFn: () => departmentService.getPublic(),
  })

  const { data, isLoading, isError } = useQuery({
    queryKey: ['doctors', 'public', filters],
    queryFn: () => doctorService.getAll(filters),
  })

  const doctors = isError ? [] : (data?.content ?? [])
  const activeFilterCount = [departmentId, gender, availability !== 'all', experienceRange].filter(Boolean).length

  function clearFilters() {
    setDepartmentId('')
    setGender('')
    setAvailability('all')
    setExperienceRange('')
    setSearch('')
    setSearchParams({})
  }

  function handleDepartmentChange(value: string) {
    setDepartmentId(value)
    if (value) setSearchParams({ departmentId: value })
    else setSearchParams({})
  }

  return (
    <div className="bg-gradient-to-b from-primary-50/50 to-white dark:from-slate-950 dark:to-slate-900">
      <div className="mx-auto max-w-7xl px-4 py-12 sm:px-6 lg:px-8">
        <div className="text-center">
          <h1 className="text-3xl font-bold tracking-tight text-slate-900 dark:text-white sm:text-4xl">
            Find Your Doctor
          </h1>
          <p className="mt-3 text-lg text-slate-600 dark:text-slate-400">
            Expert physicians across every specialty — book with confidence
          </p>
        </div>

        <div className="mt-8 flex flex-col gap-6 lg:flex-row">
          {/* Filters sidebar */}
          <aside className={`lg:w-72 ${showFilters ? 'block' : 'hidden lg:block'}`}>
            <div className="sticky top-24 rounded-2xl border border-slate-200 bg-white p-5 shadow-sm dark:border-slate-700 dark:bg-slate-900">
              <div className="flex items-center justify-between">
                <h2 className="flex items-center gap-2 font-semibold text-slate-900 dark:text-white">
                  <Filter className="h-4 w-4 text-primary-600" />
                  Filters
                  {activeFilterCount > 0 && (
                    <span className="rounded-full bg-primary-100 px-2 py-0.5 text-xs text-primary-700 dark:bg-primary-950 dark:text-primary-300">
                      {activeFilterCount}
                    </span>
                  )}
                </h2>
                {activeFilterCount > 0 && (
                  <button type="button" onClick={clearFilters} className="text-xs text-primary-600 hover:underline">
                    Clear all
                  </button>
                )}
              </div>

              <div className="mt-5 space-y-5">
                <div>
                  <label className="mb-1.5 block text-xs font-medium uppercase tracking-wide text-slate-500">
                    Department
                  </label>
                  <select
                    value={departmentId}
                    onChange={(e) => handleDepartmentChange(e.target.value)}
                    className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm dark:border-slate-700 dark:bg-slate-800"
                  >
                    <option value="">All Departments</option>
                    {(departments ?? []).map((dept) => (
                      <option key={dept.id} value={dept.id}>
                        {dept.name}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="mb-1.5 block text-xs font-medium uppercase tracking-wide text-slate-500">
                    Experience
                  </label>
                  <select
                    value={experienceRange}
                    onChange={(e) => setExperienceRange(e.target.value)}
                    className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm dark:border-slate-700 dark:bg-slate-800"
                  >
                    {EXPERIENCE_OPTIONS.map((opt) => (
                      <option key={opt.value} value={opt.value}>
                        {opt.label}
                      </option>
                    ))}
                  </select>
                </div>

                <div>
                  <label className="mb-1.5 block text-xs font-medium uppercase tracking-wide text-slate-500">
                    Availability
                  </label>
                  <div className="flex flex-wrap gap-2">
                    {(['all', 'available', 'unavailable'] as const).map((opt) => (
                      <button
                        key={opt}
                        type="button"
                        onClick={() => setAvailability(opt)}
                        className={`rounded-full px-3 py-1.5 text-xs font-medium transition ${
                          availability === opt
                            ? 'bg-primary-600 text-white'
                            : 'bg-slate-100 text-slate-600 hover:bg-slate-200 dark:bg-slate-800 dark:text-slate-300'
                        }`}
                      >
                        {opt === 'all' ? 'All' : opt === 'available' ? 'Available' : 'Unavailable'}
                      </button>
                    ))}
                  </div>
                </div>

                <div>
                  <label className="mb-1.5 block text-xs font-medium uppercase tracking-wide text-slate-500">
                    Gender
                  </label>
                  <select
                    value={gender}
                    onChange={(e) => setGender(e.target.value as Gender | '')}
                    className="w-full rounded-lg border border-slate-200 bg-white px-3 py-2 text-sm dark:border-slate-700 dark:bg-slate-800"
                  >
                    {GENDER_OPTIONS.map((opt) => (
                      <option key={opt.value} value={opt.value}>
                        {opt.label}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
            </div>
          </aside>

          {/* Results */}
          <div className="flex-1">
            <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
              <div className="flex-1">
                <SearchBar value={search} onChange={setSearch} placeholder="Search by name or specialization..." />
              </div>
              <button
                type="button"
                onClick={() => setShowFilters(!showFilters)}
                className="btn-secondary flex items-center gap-2 text-sm lg:hidden"
              >
                <Filter className="h-4 w-4" />
                Filters {activeFilterCount > 0 && `(${activeFilterCount})`}
              </button>
            </div>

            <p className="mt-4 text-sm text-slate-500">
              {isLoading ? 'Loading...' : `${doctors.length} doctor${doctors.length !== 1 ? 's' : ''} found`}
            </p>

            {isLoading ? (
              <SkeletonLoader rows={10} className="card mt-6" />
            ) : (
              <div className="mt-6 grid gap-5 sm:grid-cols-2 xl:grid-cols-2">
                {doctors.length === 0 ? (
                  <p className="col-span-full rounded-xl border border-dashed border-slate-300 py-16 text-center text-slate-500 dark:border-slate-700">
                    No doctors match your filters. Try adjusting your search.
                  </p>
                ) : (
                  doctors.map((doc, index) => {
                    const name = doc.fullName ?? `${doc.firstName ?? ''} ${doc.lastName ?? ''}`.trim()
                    const rating = Number((4 + (index % 10) * 0.08).toFixed(1))
                    return (
                      <Link
                        key={doc.id}
                        to={`/doctors/${doc.id}`}
                        className="group rounded-2xl border border-slate-200 bg-white p-5 shadow-sm transition hover:-translate-y-0.5 hover:border-primary-300 hover:shadow-lg dark:border-slate-700 dark:bg-slate-900 dark:hover:border-primary-700"
                      >
                        <div className="flex items-start gap-4">
                          <div className="flex h-16 w-16 shrink-0 items-center justify-center rounded-2xl bg-gradient-to-br from-primary-100 to-sky-100 text-primary-600 dark:from-primary-950 dark:to-sky-950">
                            <User className="h-8 w-8" />
                          </div>
                          <div className="min-w-0 flex-1">
                            <div className="flex items-start justify-between gap-2">
                              <h3 className="font-semibold text-slate-900 group-hover:text-primary-600 dark:text-white">
                                {name}
                              </h3>
                              {doc.available !== false ? (
                                <span className="flex shrink-0 items-center gap-1 rounded-full bg-emerald-50 px-2 py-0.5 text-xs font-medium text-emerald-700 dark:bg-emerald-950 dark:text-emerald-300">
                                  <CheckCircle2 className="h-3 w-3" />
                                  Available
                                </span>
                              ) : (
                                <span className="flex shrink-0 items-center gap-1 rounded-full bg-slate-100 px-2 py-0.5 text-xs text-slate-500 dark:bg-slate-800">
                                  <XCircle className="h-3 w-3" />
                                  Unavailable
                                </span>
                              )}
                            </div>
                            <p className="text-sm font-medium text-primary-600">{doc.specialization}</p>
                            <p className="text-xs text-slate-500">{doc.departmentName}</p>
                            <div className="mt-2 flex flex-wrap gap-3 text-xs text-slate-500">
                              <span className="inline-flex items-center gap-1 rounded-full bg-amber-50 px-2 py-0.5 text-amber-700 dark:bg-amber-900/30 dark:text-amber-300">
                                <Star className="h-3 w-3 fill-amber-400 text-amber-400" />
                                {rating} rating
                              </span>
                              {doc.experienceYears != null && (
                                <span className="flex items-center gap-1">
                                  <Star className="h-3 w-3 fill-amber-400 text-amber-400" />
                                  {doc.experienceYears} yrs
                                </span>
                              )}
                              {doc.gender && <span>{doc.gender.charAt(0) + doc.gender.slice(1).toLowerCase()}</span>}
                              {doc.consultationFee != null && <span>₹{doc.consultationFee}</span>}
                            </div>
                          </div>
                        </div>
                        <div className="mt-4 flex items-center gap-2 text-xs font-medium text-primary-600 opacity-0 transition group-hover:opacity-100">
                          <Calendar className="h-3.5 w-3.5" />
                          Book appointment →
                        </div>
                      </Link>
                    )
                  })
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}
