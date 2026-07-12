import type { UserRole } from './common.ts'

export interface AuthResponse {
  token: string
  userId: string
  email: string
  role: UserRole
  profileImageUrl?: string
}

export interface User {
  id: string
  email: string
  role: UserRole
  fullName?: string
  phone?: string
  profileImageUrl?: string
  enabled?: boolean
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterPatientRequest {
  email: string
  password: string
  firstName: string
  lastName: string
  phone?: string
  dateOfBirth?: string
  gender?: string
  bloodGroup?: string
  address?: string
  emergencyContact?: string
}

export interface ForgotPasswordRequest {
  email: string
}

export interface AuthState {
  user: User | null
  token: string | null
  isAuthenticated: boolean
  isLoading: boolean
}
