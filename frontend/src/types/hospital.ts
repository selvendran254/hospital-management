export interface LabTest {
  id: string
  name: string
  code?: string
  description?: string
  price: number
  sampleType?: string
  turnaroundHours?: number
  isActive?: boolean
}

export interface LabReport {
  id: string
  patientId: string
  patientName?: string
  labTestId: string
  testName?: string
  orderedByDoctorId?: string
  doctorName?: string
  status: string
  results?: string
  notes?: string
  reportUrl?: string
  orderedAt?: string
  completedAt?: string
}

export interface Medicine {
  id: string
  name: string
  genericName?: string
  category?: string
  manufacturer?: string
  unitPrice: number
  stockQuantity: number
  reorderLevel?: number
  expiryDate?: string
  batchNumber?: string
  isActive?: boolean
}

export interface Supplier {
  id: string
  name: string
  contactPerson?: string
  email?: string
  phone?: string
  address?: string
  isActive?: boolean
}

export interface MedicinePurchase {
  id: string
  medicineId: string
  medicineName?: string
  supplierId: string
  supplierName?: string
  quantity: number
  unitPrice: number
  totalAmount: number
  purchaseDate: string
  invoiceNumber?: string
}

export interface MedicineSale {
  id: string
  medicineId: string
  medicineName?: string
  patientId?: string
  patientName?: string
  quantity: number
  unitPrice: number
  totalAmount: number
  saleDate: string
  prescriptionId?: string
}

export interface BloodInventory {
  id: string
  bloodGroup: string
  unitsAvailable: number
  lastUpdated?: string
}

export interface BloodDonor {
  id: string
  fullName: string
  bloodGroup: string
  phone?: string
  email?: string
  lastDonationDate?: string
  isEligible?: boolean
}

export interface BloodRequest {
  id: string
  patientId?: string
  patientName?: string
  bloodGroup: string
  unitsRequired: number
  urgency?: string
  status: string
  requestedAt?: string
  fulfilledAt?: string
}

export interface Room {
  id: string
  roomNumber: string
  roomType: string
  floorNumber?: number
  departmentId?: string
  departmentName?: string
  capacity?: number
  isActive?: boolean
}

export interface Bed {
  id: string
  roomId: string
  roomNumber?: string
  bedNumber: string
  status: string
  patientId?: string
  patientName?: string
}

export interface Admission {
  id: string
  patientId: string
  patientName?: string
  bedId: string
  bedNumber?: string
  roomNumber?: string
  admittingDoctorId?: string
  doctorName?: string
  admissionDate: string
  dischargeDate?: string
  status: string
  diagnosis?: string
  notes?: string
}

export interface Bill {
  id: string
  patientId: string
  patientName?: string
  totalAmount: number
  paidAmount?: number
  status: string
  description?: string
  dueDate?: string
  createdAt?: string
}

export interface Payment {
  id: string
  billId: string
  amount: number
  paymentMethod: string
  status: string
  transactionId?: string
  paidAt?: string
}

export interface Ambulance {
  id: string
  vehicleNumber: string
  driverName?: string
  driverPhone?: string
  status: string
  currentLocation?: string
  isAvailable?: boolean
}

export interface Notification {
  id: string
  userId: string
  title: string
  message: string
  type: string
  isRead: boolean
  createdAt: string
}

export interface BlogPost {
  id: string
  title: string
  slug?: string
  content: string
  excerpt?: string
  authorName?: string
  coverImageUrl?: string
  isPublished?: boolean
  publishedAt?: string
  createdAt?: string
}

export interface HealthPackage {
  id: string
  name: string
  description?: string
  price: number
  testsIncluded?: string[]
  durationDays?: number
  isActive?: boolean
}

export interface HospitalService {
  id: string
  name: string
  description?: string
  category?: string
  price?: number
  icon?: string
  isActive?: boolean
}

export interface Career {
  id: string
  title: string
  department?: string
  description?: string
  requirements?: string
  location?: string
  employmentType?: string
  isActive?: boolean
  postedAt?: string
}

export interface ContactMessage {
  name: string
  email: string
  phone?: string
  subject: string
  message: string
}

export interface DashboardStats {
  totalPatients: number
  totalDoctors: number
  totalAppointments: number
  totalDepartments: number
  pendingAppointments: number
  todayAppointments: number
  occupiedBeds: number
  availableBeds: number
  lowStockMedicines: number
  pendingLabReports: number
}

export interface ChartDataPoint {
  name: string
  value: number
  [key: string]: string | number
}
