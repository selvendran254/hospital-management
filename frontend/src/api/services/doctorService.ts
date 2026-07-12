import api from '../axios.ts'
import type { Doctor, DoctorDashboard, DoctorFilterParams, DoctorLeave, DoctorSchedule, PageResponse, PaginationParams } from '@/types/index.ts'

export const doctorService = {
  getAll: (params?: DoctorFilterParams) =>
    api.get<PageResponse<Doctor>>('/doctors', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Doctor>(`/doctors/${id}`).then((r) => r.data),

  create: (data: Partial<Doctor>) => api.post<Doctor>('/doctors', data).then((r) => r.data),

  update: (id: string, data: Partial<Doctor>) =>
    api.put<Doctor>(`/doctors/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/doctors/${id}`),

  getDashboard: () => api.get<DoctorDashboard>('/doctors/dashboard').then((r) => r.data),

  getSchedule: (doctorId?: string) =>
    api.get<DoctorSchedule[]>('/doctors/schedule', { params: { doctorId } }).then((r) => r.data),

  updateSchedule: (data: Partial<DoctorSchedule>[]) =>
    api.put<DoctorSchedule[]>('/doctors/schedule', data).then((r) => r.data),

  getLeaves: (params?: PaginationParams) =>
    api.get<PageResponse<DoctorLeave>>('/doctors/leaves', { params }).then((r) => r.data),

  requestLeave: (data: Partial<DoctorLeave>) =>
    api.post<DoctorLeave>('/doctors/leaves', data).then((r) => r.data),

  getPatients: (params?: PaginationParams) =>
    api.get<PageResponse<import('@/types/index.ts').Patient>>('/doctors/patients', { params }).then((r) => r.data),
}
