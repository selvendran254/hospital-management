import { advancedService, authService } from '@/api/services/index.ts'
import LoadingSpinner from '@/components/LoadingSpinner.tsx'
import { useAuth } from '@/hooks/useAuth.ts'
import { useQuery } from '@tanstack/react-query'
import { Plus, User, X } from 'lucide-react'
import { useState } from 'react'

export default function ProfilePage() {
  const { user } = useAuth()
  const [newMember, setNewMember] = useState({ name: '', relation: '', age: '' })

  const { data: profile, isLoading } = useQuery({
    queryKey: ['profile'],
    queryFn: () => authService.getProfile(),
    enabled: !!user,
  })
  const { data: familyMembers } = useQuery({
    queryKey: ['family-members'],
    queryFn: () => advancedService.getFamilyMembers(),
  })
  const [localMembers, setLocalMembers] = useState<Array<{ id: string; name: string; relation: string; age: number }>>([])

  if (isLoading) {
    return <div className="flex justify-center py-20"><LoadingSpinner size="lg" /></div>
  }

  const display = profile ?? user

  return (
    <div className="mx-auto max-w-2xl space-y-6">
      <div>
        <h1 className="page-title">My Profile</h1>
        <p className="page-subtitle">Your account information</p>
      </div>
      <div className="card">
        <div className="flex items-center gap-4">
          <div className="flex h-20 w-20 items-center justify-center rounded-full bg-primary-100 text-primary-600 dark:bg-primary-950">
            <User className="h-10 w-10" />
          </div>
          <div>
            <h2 className="text-xl font-semibold text-slate-900 dark:text-white">{display?.fullName ?? display?.email}</h2>
            <p className="text-sm text-slate-500">{display?.role}</p>
          </div>
        </div>
        <div className="mt-6 space-y-3">
          <div>
            <label className="block text-sm font-medium text-slate-500">Email</label>
            <p className="mt-1 text-slate-900 dark:text-white">{display?.email ?? '-'}</p>
          </div>
          <div>
            <label className="block text-sm font-medium text-slate-500">Phone</label>
            <p className="mt-1 text-slate-900 dark:text-white">{display?.phone ?? '-'}</p>
          </div>
        </div>
      </div>
      <div className="card">
        <h2 className="text-lg font-semibold text-slate-900 dark:text-white">Family Members</h2>
        <div className="mt-3 space-y-2">
          {[...(familyMembers ?? []), ...localMembers].map((member) => (
            <div key={member.id} className="flex items-center justify-between rounded-lg bg-slate-100 px-3 py-2 dark:bg-slate-800">
              <p className="text-sm">
                <span className="font-medium text-slate-900 dark:text-white">{member.name}</span> - {member.relation} ({member.age})
              </p>
              <button
                type="button"
                className="text-rose-500"
                onClick={() => setLocalMembers((current) => current.filter((entry) => entry.id !== member.id))}
              >
                <X className="h-4 w-4" />
              </button>
            </div>
          ))}
        </div>
        <div className="mt-4 grid gap-2 sm:grid-cols-3">
          <input className="input-field" placeholder="Name" value={newMember.name} onChange={(event) => setNewMember((current) => ({ ...current, name: event.target.value }))} />
          <input className="input-field" placeholder="Relation" value={newMember.relation} onChange={(event) => setNewMember((current) => ({ ...current, relation: event.target.value }))} />
          <input className="input-field" placeholder="Age" type="number" value={newMember.age} onChange={(event) => setNewMember((current) => ({ ...current, age: event.target.value }))} />
        </div>
        <button
          type="button"
          className="btn-secondary mt-3"
          onClick={() => {
            if (!newMember.name || !newMember.relation || !newMember.age) {
              return
            }
            setLocalMembers((current) => [
              ...current,
              {
                id: `local-${Date.now()}`,
                name: newMember.name,
                relation: newMember.relation,
                age: Number(newMember.age),
              },
            ])
            setNewMember({ name: '', relation: '', age: '' })
          }}
        >
          <Plus className="h-4 w-4" />
          Add Family Member
        </button>
      </div>
    </div>
  )
}
