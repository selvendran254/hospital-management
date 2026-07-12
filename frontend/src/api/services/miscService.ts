import api from '../axios.ts'
import type {
  BlogPost,
  Career,
  HealthPackage,
  HospitalService,
  MedicinePurchase,
  MedicineSale,
  Notification,
  PageResponse,
  PaginationParams,
  Prescription,
  Supplier,
} from '@/types/index.ts'
import type { DashboardStats } from '@/types/index.ts'

export const prescriptionService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Prescription>>('/prescriptions', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Prescription>(`/prescriptions/${id}`).then((r) => r.data),

  create: (data: Partial<Prescription>) =>
    api.post<Prescription>('/prescriptions', data).then((r) => r.data),

  getMyPrescriptions: (params?: PaginationParams) =>
    api.get<PageResponse<Prescription>>('/prescriptions/my', { params }).then((r) => r.data),
}

export const supplierService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Supplier>>('/suppliers', { params }).then((r) => r.data),

  create: (data: Partial<Supplier>) => api.post<Supplier>('/suppliers', data).then((r) => r.data),

  update: (id: string, data: Partial<Supplier>) =>
    api.put<Supplier>(`/suppliers/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/suppliers/${id}`),
}

export const pharmacyService = {
  getPurchases: (params?: PaginationParams) =>
    api.get<PageResponse<MedicinePurchase>>('/pharmacy/purchases', { params }).then((r) => r.data),

  createPurchase: (data: Partial<MedicinePurchase>) =>
    api.post<MedicinePurchase>('/pharmacy/purchases', data).then((r) => r.data),

  getSales: (params?: PaginationParams) =>
    api.get<PageResponse<MedicineSale>>('/pharmacy/sales', { params }).then((r) => r.data),

  createSale: (data: Partial<MedicineSale>) =>
    api.post<MedicineSale>('/pharmacy/sales', data).then((r) => r.data),
}

export const notificationService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Notification>>('/notifications', { params }).then((r) => r.data),

  markAsRead: (id: string) => api.patch(`/notifications/${id}/read`),

  markAllAsRead: () => api.patch('/notifications/read-all'),

  getUnreadCount: () => api.get<{ count: number }>('/notifications/unread-count').then((r) => r.data),
}

export const blogService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<BlogPost>>('/blog', { params }).then((r) => r.data),

  getPublic: (params?: PaginationParams) =>
    api.get<PageResponse<BlogPost>>('/blog/public', { params }).then((r) => r.data),

  getById: (id: string) => api.get<BlogPost>(`/blog/${id}`).then((r) => r.data),

  create: (data: Partial<BlogPost>) => api.post<BlogPost>('/blog', data).then((r) => r.data),

  update: (id: string, data: Partial<BlogPost>) =>
    api.put<BlogPost>(`/blog/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/blog/${id}`),
}

export const publicService = {
  getServices: () => api.get<HospitalService[]>('/public/services').then((r) => r.data),

  getHealthPackages: () => api.get<HealthPackage[]>('/public/health-packages').then((r) => r.data),

  getCareers: () => api.get<Career[]>('/public/careers').then((r) => r.data),

  getDashboardStats: () => api.get<DashboardStats>('/admin/dashboard/stats').then((r) => r.data),

  globalSearch: (query: string) =>
    api.get<{ results: Array<{ type: string; id: string; title: string }> }>('/search', {
      params: { q: query },
    }).then((r) => r.data),
}
