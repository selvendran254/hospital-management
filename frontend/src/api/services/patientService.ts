import api from '../axios.ts'
import type { MedicalRecord, PageResponse, PaginationParams, Patient } from '@/types/index.ts'

export const patientService = {
  getAll: (params?: PaginationParams) =>
    api.get<PageResponse<Patient>>('/patients', { params }).then((r) => r.data),

  getById: (id: string) => api.get<Patient>(`/patients/${id}`).then((r) => r.data),

  create: (data: Partial<Patient>) => api.post<Patient>('/patients', data).then((r) => r.data),

  update: (id: string, data: Partial<Patient>) =>
    api.put<Patient>(`/patients/${id}`, data).then((r) => r.data),

  remove: (id: string) => api.delete(`/patients/${id}`),

  getMedicalHistory: (patientId: string) =>
    api.get<MedicalRecord[]>(`/patients/${patientId}/medical-records`).then((r) => r.data),

  register: (data: Partial<Patient>) => api.post<Patient>('/patients/register', data).then((r) => r.data),
}
