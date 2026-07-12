import api from '../axios.ts'
import type { Ambulance, PageResponse, PaginationParams } from '@/types/index.ts'

export const ambulanceService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Ambulance>>('/ambulances', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Ambulance>(`/ambulances/${id}`).then((r) => r.data),

  create: (data: Partial<Ambulance>) => api.post<Ambulance>('/ambulances', data).then((r) => r.data),

  update: (id: string, data: Partial<Ambulance>) =>
    api.put<Ambulance>(`/ambulances/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/ambulances/${id}`),
}
