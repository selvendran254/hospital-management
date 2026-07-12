import api from '../axios.ts'
import type { LabReport, LabTest, PageResponse, PaginationParams } from '@/types/index.ts'

export const labService = {
  getTests: (params?: PaginationParams) =>
    api.get<PageResponse<LabTest>>('/lab/tests', { params }).then((r) => r.data),

  getTestById: (id: string) => api.get<LabTest>(`/lab/tests/${id}`).then((r) => r.data),

  createTest: (data: Partial<LabTest>) => api.post<LabTest>('/lab/tests', data).then((r) => r.data),

  updateTest: (id: string, data: Partial<LabTest>) =>
    api.put<LabTest>(`/lab/tests/${id}`, data).then((r) => r.data),

  removeTest: (id: string) => api.delete(`/lab/tests/${id}`),

  getReports: (params?: PaginationParams & { status?: string }) =>
    api.get<PageResponse<LabReport>>('/lab/reports', { params }).then((r) => r.data),

  getReportById: (id: string) => api.get<LabReport>(`/lab/reports/${id}`).then((r) => r.data),

  updateReport: (id: string, data: Partial<LabReport>) =>
    api.put<LabReport>(`/lab/reports/${id}`, data).then((r) => r.data),

  getMyReports: (params?: PaginationParams) =>
    api.get<PageResponse<LabReport>>('/lab/reports/my', { params }).then((r) => r.data),
}
