import api from '../axios.ts'

export interface FamilyMember {
  id: string
  name: string
  relation: string
  age: number
}

export interface ReminderSettings {
  smsEnabled: boolean
  leadHours: number
  provider: string
  senderId: string
}

export interface RazorpayOrderRequest {
  amount: number
  currency: string
  receipt: string
  notes?: Record<string, string>
}

export interface RazorpayOrderResponse {
  id: string
  amount: number
  currency: string
  key: string
}

export interface InsuranceClaim {
  id: string
  policyNumber: string
  patientName: string
  amount: number
  status: 'UNDER_REVIEW' | 'APPROVED' | 'REJECTED'
}

export interface BackupStatus {
  lastBackup: string
  nextBackup: string
  status: 'HEALTHY' | 'WARNING'
}

export interface PatientAccessLog {
  id: string
  accessedBy: string
  role: string
  viewedAt: string
  reason: string
}

export interface MonthlyRevenue {
  month: string
  revenue: number
}

export interface DoctorRanking {
  id: string
  doctorName: string
  score: number
  patientsHandled: number
}

export interface Clinic {
  id: string
  name: string
  city: string
}

export interface CompliancePolicy {
  id: string
  title: string
  lastUpdated: string
  status: 'ACTIVE' | 'PENDING_REVIEW'
}

export const advancedService = {
  async getReminderSettings(): Promise<ReminderSettings> {
    try {
      const response = await api.get<ReminderSettings>('/settings/sms')
      return response.data
    } catch {
      return { smsEnabled: true, leadHours: 6, provider: 'Twilio', senderId: 'HOSPMS' }
    }
  },

  async getFamilyMembers(): Promise<FamilyMember[]> {
    try {
      const response = await api.get<FamilyMember[]>('/patients/family-members')
      return response.data
    } catch {
      return [
        { id: 'fm-1', name: 'Lakshmi Selvan', relation: 'Spouse', age: 34 },
        { id: 'fm-2', name: 'Arun Selvan', relation: 'Child', age: 8 },
      ]
    }
  },

  async createRazorpayOrder(payload: RazorpayOrderRequest): Promise<RazorpayOrderResponse> {
    const response = await api.post<RazorpayOrderResponse>('/payments/razorpay/order', payload)
    return response.data
  },

  async getInsuranceClaims(): Promise<InsuranceClaim[]> {
    try {
      const response = await api.get<InsuranceClaim[]>('/insurance/claims')
      return response.data
    } catch {
      return [
        { id: 'clm-1', policyNumber: 'POL-993201', patientName: 'Arun Kumar', amount: 24000, status: 'UNDER_REVIEW' },
        { id: 'clm-2', policyNumber: 'POL-993874', patientName: 'Priya Devi', amount: 18000, status: 'APPROVED' },
      ]
    }
  },

  async getBackupStatus(): Promise<BackupStatus> {
    try {
      const response = await api.get<BackupStatus>('/admin/backup-status')
      return response.data
    } catch {
      return {
        lastBackup: new Date(Date.now() - 2 * 60 * 60 * 1000).toLocaleString(),
        nextBackup: new Date(Date.now() + 4 * 60 * 60 * 1000).toLocaleString(),
        status: 'HEALTHY',
      }
    }
  },

  async getAccessLogs(): Promise<PatientAccessLog[]> {
    try {
      const response = await api.get<PatientAccessLog[]>('/patients/access-logs')
      return response.data
    } catch {
      return [
        {
          id: 'log-1',
          accessedBy: 'Dr. Naveen Kumar',
          role: 'Doctor',
          viewedAt: new Date(Date.now() - 3 * 60 * 60 * 1000).toLocaleString(),
          reason: 'Follow-up consultation',
        },
        {
          id: 'log-2',
          accessedBy: 'Lab Staff - Asha',
          role: 'Laboratory',
          viewedAt: new Date(Date.now() - 26 * 60 * 60 * 1000).toLocaleString(),
          reason: 'Sample verification',
        },
      ]
    }
  },

  async getMonthlyRevenue(): Promise<MonthlyRevenue[]> {
    try {
      const response = await api.get<MonthlyRevenue[]>('/reports/monthly-revenue')
      return response.data
    } catch {
      return [
        { month: 'Jan', revenue: 12.5 },
        { month: 'Feb', revenue: 13.2 },
        { month: 'Mar', revenue: 14.6 },
        { month: 'Apr', revenue: 15.1 },
        { month: 'May', revenue: 16.4 },
        { month: 'Jun', revenue: 17.2 },
      ]
    }
  },

  async getDoctorRanking(): Promise<DoctorRanking[]> {
    try {
      const response = await api.get<DoctorRanking[]>('/reports/doctor-ranking')
      return response.data
    } catch {
      return [
        { id: 'doc-1', doctorName: 'Dr. Meera Nair', score: 97, patientsHandled: 213 },
        { id: 'doc-2', doctorName: 'Dr. Rahul Sen', score: 94, patientsHandled: 187 },
        { id: 'doc-3', doctorName: 'Dr. Kavya Das', score: 92, patientsHandled: 176 },
      ]
    }
  },

  async getClinics(): Promise<Clinic[]> {
    try {
      const response = await api.get<Clinic[]>('/admin/clinics')
      return response.data
    } catch {
      return [
        { id: 'clinic-1', name: 'CityCare Main', city: 'Chennai' },
        { id: 'clinic-2', name: 'CityCare East', city: 'Coimbatore' },
        { id: 'clinic-3', name: 'CityCare North', city: 'Madurai' },
      ]
    }
  },

  async getCompliancePolicies(): Promise<CompliancePolicy[]> {
    try {
      const response = await api.get<CompliancePolicy[]>('/compliance/policies')
      return response.data
    } catch {
      return [
        { id: 'cp-1', title: 'Patient Consent Retention Policy', lastUpdated: '2026-06-24', status: 'ACTIVE' },
        { id: 'cp-2', title: 'Data Masking and Encryption Policy', lastUpdated: '2026-05-15', status: 'PENDING_REVIEW' },
      ]
    }
  },
}
