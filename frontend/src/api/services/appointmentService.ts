import api from '../axios.ts'
import type { Appointment, PageResponse, PaginationParams } from '@/types/index.ts'

export const appointmentService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Appointment>>('/appointments', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Appointment>(`/appointments/${id}`).then((r) => r.data),

  create: (data: Partial<Appointment>) =>
    api.post<Appointment>('/appointments', data).then((r) => r.data),

  update: (id: string, data: Partial<Appointment>) =>
    api.put<Appointment>(`/appointments/${id}`, data).then((r) => r.data),

  cancel: (id: string) => api.patch<Appointment>(`/appointments/${id}/cancel`).then((r) => r.data),

  approve: (id: string) => api.patch<Appointment>(`/appointments/${id}/approve`).then((r) => r.data),

  complete: (id: string) => api.patch<Appointment>(`/appointments/${id}/complete`).then((r) => r.data),

  checkIn: (id: string) => api.patch<Appointment>(`/appointments/${id}/check-in`).then((r) => r.data),

  getMyAppointments: (params?: PaginationParams) =>
    api.get<PageResponse<Appointment>>('/appointments/my', { params }).then((r) => r.data),
}
