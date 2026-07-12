import api from '../axios.ts'
import type { Bed, PageResponse, PaginationParams, Room } from '@/types/index.ts'

export const roomService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Room>>('/rooms', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Room>(`/rooms/${id}`).then((r) => r.data),

  create: (data: Partial<Room>) => api.post<Room>('/rooms', data).then((r) => r.data),

  update: (id: string, data: Partial<Room>) =>
    api.put<Room>(`/rooms/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/rooms/${id}`),

  getBeds: (params?: PaginationParams) =>
    api.get<PageResponse<Bed>>('/beds', { params }).then((r) => r.data),

  createBed: (data: Partial<Bed>) => api.post<Bed>('/beds', data).then((r) => r.data),

  updateBed: (id: string, data: Partial<Bed>) =>
    api.put<Bed>(`/beds/${id}`, data).then((r) => r.data),

  removeBed: (id: string) => api.delete(`/beds/${id}`),
}
