import api from '../axios.ts'
import type { Department, PageResponse, PaginationParams } from '@/types/index.ts'

export const departmentService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Department>>('/departments', { params }).then((r) => r.data),

  getPublic: () => api.get<Department[]>('/departments/public').then((r) => r.data),

  getById: (id: string) => api.get<Department>(`/departments/${id}`).then((r) => r.data),

  create: (data: Partial<Department>) => api.post<Department>('/departments', data).then((r) => r.data),

  update: (id: string, data: Partial<Department>) =>
    api.put<Department>(`/departments/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/departments/${id}`),
}
