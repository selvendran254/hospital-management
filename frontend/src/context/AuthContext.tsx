import { createContext, useCallback, useEffect, useMemo, useState, type ReactNode } from 'react'
import { authService } from '@/api/services/index.ts'
import type { AuthResponse, LoginRequest, RegisterPatientRequest, User, UserRole } from '@/types/index.ts'
import { ROLE_DASHBOARD_PATHS } from '@/types/index.ts'

interface AuthContextValue {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (data: LoginRequest) => Promise<string>
  register: (data: RegisterPatientRequest) => Promise<string>
  logout: () => void
  hasRole: (...roles: UserRole[]) => boolean
  getDashboardPath: () => string
}

export const AuthContext = createContext<AuthContextValue | null>(null)

function mapAuthToUser(auth: AuthResponse): User {
  return {
    id: auth.userId,
    email: auth.email,
    role: auth.role,
    profileImageUrl: auth.profileImageUrl,
  }
}

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [token, setToken] = useState<string | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  useEffect(() => {
    const storedToken = localStorage.getItem('token')
    const storedUser = localStorage.getItem('user')
    if (storedToken && storedUser) {
      setToken(storedToken)
      try {
        setUser(JSON.parse(storedUser) as User)
      } catch {
        localStorage.removeItem('user')
      }
    }
    setIsLoading(false)
  }, [])

  const persistAuth = useCallback((auth: AuthResponse) => {
    const mapped = mapAuthToUser(auth)
    localStorage.setItem('token', auth.token)
    localStorage.setItem('user', JSON.stringify(mapped))
    setToken(auth.token)
    setUser(mapped)
    return ROLE_DASHBOARD_PATHS[auth.role]
  }, [])

  const login = useCallback(async (data: LoginRequest) => {
    const auth = await authService.login(data)
    return persistAuth(auth)
  }, [persistAuth])

  const register = useCallback(async (data: RegisterPatientRequest) => {
    const auth = await authService.register(data)
    return persistAuth(auth)
  }, [persistAuth])

  const logout = useCallback(() => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setToken(null)
    setUser(null)
    authService.logout().catch(() => undefined)
    window.location.href = '/login'
  }, [])

  const hasRole = useCallback(
    (...roles: UserRole[]) => (user ? roles.includes(user.role) : false),
    [user],
  )

  const getDashboardPath = useCallback(
    () => (user ? ROLE_DASHBOARD_PATHS[user.role] : '/'),
    [user],
  )

  const value = useMemo(
    () => ({
      user,
      token,
      isAuthenticated: !!token && !!user,
      isLoading,
      login,
      register,
      logout,
      hasRole,
      getDashboardPath,
    }),
    [user, token, isLoading, login, register, logout, hasRole, getDashboardPath],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
