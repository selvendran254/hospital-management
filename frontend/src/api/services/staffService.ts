import api from '../axios.ts'
import type { PageResponse, PaginationParams, Staff } from '@/types/index.ts'

export const staffService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Staff>>('/staff', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Staff>(`/staff/${id}`).then((r) => r.data),

  create: (data: Partial<Staff>) => api.post<Staff>('/staff', data).then((r) => r.data),

  update: (id: string, data: Partial<Staff>) =>
    api.put<Staff>(`/staff/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/staff/${id}`),
}
