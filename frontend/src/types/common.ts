export type UserRole =
  | 'ADMIN'
  | 'DOCTOR'
  | 'RECEPTIONIST'
  | 'LABORATORY_STAFF'
  | 'PHARMACIST'
  | 'PATIENT'

export type Gender = 'MALE' | 'FEMALE' | 'OTHER'

export type BloodGroup =
  | 'A_POSITIVE'
  | 'A_NEGATIVE'
  | 'B_POSITIVE'
  | 'B_NEGATIVE'
  | 'AB_POSITIVE'
  | 'AB_NEGATIVE'
  | 'O_POSITIVE'
  | 'O_NEGATIVE'

export type AppointmentStatus = 'PENDING' | 'APPROVED' | 'COMPLETED' | 'CANCELLED'

export type BillStatus = 'PENDING' | 'PAID' | 'PARTIAL' | 'CANCELLED'

export type PaymentMethod = 'CASH' | 'CARD' | 'UPI' | 'INSURANCE' | 'ONLINE'

export type PaymentStatus = 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED'

export type RoomType = 'WARD' | 'ICU' | 'PRIVATE' | 'GENERAL'

export type BedStatus = 'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE' | 'RESERVED'

export type AdmissionStatus = 'ADMITTED' | 'DISCHARGED' | 'TRANSFERRED'

export type NotificationType =
  | 'APPOINTMENT_BOOKED'
  | 'APPOINTMENT_APPROVED'
  | 'APPOINTMENT_CANCELLED'
  | 'LAB_REPORT_READY'
  | 'BILL_GENERATED'
  | 'GENERAL'

export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  last: boolean
}

export interface MessageResponse {
  message: string
}

export interface PaginationParams {
  page?: number
  size?: number
  sort?: string
  search?: string
  query?: string
}

export interface DoctorFilterParams extends PaginationParams {
  departmentId?: string
  gender?: Gender
  available?: boolean
  minExperience?: number
  maxExperience?: number
}

export const ROLE_DASHBOARD_PATHS: Record<UserRole, string> = {
  ADMIN: '/admin',
  DOCTOR: '/doctor',
  RECEPTIONIST: '/receptionist',
  LABORATORY_STAFF: '/laboratory',
  PHARMACIST: '/pharmacist',
  PATIENT: '/patient',
}

export const ROLE_LABELS: Record<UserRole, string> = {
  ADMIN: 'Administrator',
  DOCTOR: 'Doctor',
  RECEPTIONIST: 'Receptionist',
  LABORATORY_STAFF: 'Laboratory Staff',
  PHARMACIST: 'Pharmacist',
  PATIENT: 'Patient',
}
