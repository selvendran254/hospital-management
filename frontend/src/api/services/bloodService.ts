import api from '../axios.ts'
import type {
  BloodDonor,
  BloodInventory,
  BloodRequest,
  PageResponse,
  PaginationParams,
} from '@/types/index.ts'

export const bloodService = {
  getInventory: () => api.get<BloodInventory[]>('/blood/inventory').then((r) => r.data),

  updateInventory: (id: string, data: Partial<BloodInventory>) =>
    api.put<BloodInventory>(`/blood/inventory/${id}`, data).then((r) => r.data),

  getDonors: (params?: PaginationParams) =>
    api.get<PageResponse<BloodDonor>>('/blood/donors', { params }).then((r) => r.data),

  createDonor: (data: Partial<BloodDonor>) =>
    api.post<BloodDonor>('/blood/donors', data).then((r) => r.data),

  getRequests: (params?: PaginationParams) =>
    api.get<PageResponse<BloodRequest>>('/blood/requests', { params }).then((r) => r.data),

  createRequest: (data: Partial<BloodRequest>) =>
    api.post<BloodRequest>('/blood/requests', data).then((r) => r.data),

  fulfillRequest: (id: string) =>
    api.patch<BloodRequest>(`/blood/requests/${id}/fulfill`).then((r) => r.data),
}
