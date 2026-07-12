import type { BloodGroup, Gender } from './common.ts'

export interface Department {
  id: string
  name: string
  description?: string
  headDoctorId?: string
  headDoctorName?: string
  floorNumber?: number
  isActive?: boolean
  createdAt?: string
}

export interface Doctor {
  id: string
  userId: string
  email?: string
  departmentId?: string
  departmentName?: string
  firstName?: string
  lastName?: string
  fullName?: string
  specialization: string
  qualification?: string
  experienceYears?: number
  gender?: Gender
  phone?: string
  bio?: string
  consultationFee?: number
  available?: boolean
  createdAt?: string
}

export interface Patient {
  id: string
  userId: string
  email?: string
  firstName?: string
  lastName?: string
  fullName?: string
  phone?: string
  dateOfBirth?: string
  gender?: Gender
  bloodGroup?: BloodGroup
  address?: string
  emergencyContact?: string
  medicalHistory?: string
  mrn?: string
  createdAt?: string
}

export interface Staff {
  id: string
  userId: string
  email?: string
  fullName?: string
  departmentId?: string
  departmentName?: string
  designation: string
  employeeId?: string
  joiningDate?: string
  phone?: string
  createdAt?: string
}

export interface Appointment {
  id: string
  patientId: string
  patientName?: string
  doctorId: string
  doctorName?: string
  appointmentDate: string
  startTime: string
  endTime: string
  status: string
  reason?: string
  notes?: string
  createdAt?: string
}

export interface Prescription {
  id: string
  appointmentId?: string
  patientId: string
  patientName?: string
  doctorId: string
  doctorName?: string
  diagnosis?: string
  medications: string
  instructions?: string
  followUpDate?: string
  pdfUrl?: string
  createdAt?: string
}

export interface DoctorSchedule {
  id: string
  doctorId: string
  dayOfWeek: number
  startTime: string
  endTime: string
  slotDurationMins?: number
  isActive?: boolean
}

export interface DoctorLeave {
  id: string
  doctorId: string
  startDate: string
  endDate: string
  reason?: string
  isApproved?: boolean
  createdAt?: string
}

export interface DoctorDashboard {
  todayAppointments: number
  pendingAppointments: number
  completedAppointments: number
  totalPatients: number
  upcomingLeaves: number
}

export interface MedicalRecord {
  id: string
  patientId: string
  doctorId?: string
  doctorName?: string
  visitDate: string
  chiefComplaint?: string
  diagnosis?: string
  treatment?: string
  vitals?: Record<string, unknown>
  createdAt?: string
}
