import api from '../axios.ts'
import type { Medicine, PageResponse, PaginationParams } from '@/types/index.ts'

export const medicineService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Medicine>>('/medicines', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Medicine>(`/medicines/${id}`).then((r) => r.data),

  create: (data: Partial<Medicine>) => api.post<Medicine>('/medicines', data).then((r) => r.data),

  update: (id: string, data: Partial<Medicine>) =>
    api.put<Medicine>(`/medicines/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/medicines/${id}`),

  getLowStock: () => api.get<Medicine[]>('/medicines/low-stock').then((r) => r.data),
}
