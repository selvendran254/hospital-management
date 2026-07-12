import api from '../axios.ts'
import type {
  AuthResponse,
  ForgotPasswordRequest,
  LoginRequest,
  MessageResponse,
  RegisterPatientRequest,
  User,
} from '@/types/index.ts'

export const authService = {
  login: (data: LoginRequest) =>
    api.post<AuthResponse>('/auth/login', data).then((r) => r.data),

  register: (data: RegisterPatientRequest) =>
    api.post<AuthResponse>('/auth/register', data).then((r) => r.data),

  forgotPassword: (data: ForgotPasswordRequest) =>
    api.post<MessageResponse>('/auth/forgot-password', data).then((r) => r.data),

  getProfile: () => api.get<User>('/auth/me').then((r) => r.data),

  logout: () => api.post<MessageResponse>('/auth/logout').then((r) => r.data),
}
