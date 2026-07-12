import api from '../axios.ts'
import type {
  Admission,
  Bill,
  MessageResponse,
  PageResponse,
  PaginationParams,
  Payment,
} from '@/types/index.ts'

export const billingService = {
  getBills: (params?: PaginationParams) =>
    api.get<PageResponse<Bill>>('/bills', { params }).then((r) => r.data),

  getBillById: (id: string) => api.get<Bill>(`/bills/${id}`).then((r) => r.data),

  createBill: (data: Partial<Bill>) => api.post<Bill>('/bills', data).then((r) => r.data),

  getMyBills: (params?: PaginationParams) =>
    api.get<PageResponse<Bill>>('/bills/my', { params }).then((r) => r.data),

  recordPayment: (data: Partial<Payment>) =>
    api.post<Payment>('/payments', data).then((r) => r.data),

  getAdmissions: (params?: PaginationParams) =>
    api.get<PageResponse<Admission>>('/admissions', { params }).then((r) => r.data),

  admit: (data: Partial<Admission>) => api.post<Admission>('/admissions', data).then((r) => r.data),

  discharge: (id: string) =>
    api.patch<Admission>(`/admissions/${id}/discharge`).then((r) => r.data),
}

export const contactService = {
  send: (data: { name: string; email: string; phone?: string; subject: string; message: string }) =>
    api.post<MessageResponse>('/contact', data).then((r) => r.data),
}
