import ProtectedRoute from '@/components/ProtectedRoute.tsx'
import {
  adminSidebar,
  doctorSidebar,
  laboratorySidebar,
  patientSidebar,
  pharmacistSidebar,
  receptionistSidebar,
} from '@/config/sidebarConfig.ts'
import DashboardLayout from '@/layouts/DashboardLayout.tsx'
import PublicLayout from '@/layouts/PublicLayout.tsx'
import { lazy, Suspense, type ReactNode } from 'react'
import AdminDashboard from '@/pages/admin/AdminDashboard.tsx'
import AuditLogsPage from '@/pages/admin/AuditLogsPage.tsx'
import AmbulancesPage from '@/pages/admin/AmbulancesPage.tsx'
import AppointmentsPage from '@/pages/admin/AppointmentsPage.tsx'
import BedsPage from '@/pages/admin/BedsPage.tsx'
import BlogPage from '@/pages/admin/BlogPage.tsx'
import BranchesPage from '@/pages/admin/BranchesPage.tsx'
import CanteenAdminPage from '@/pages/admin/CanteenAdminPage.tsx'
import ParkingAdminPage from '@/pages/admin/ParkingAdminPage.tsx'
import BrandingSettingsPage from '@/pages/admin/BrandingSettingsPage.tsx'
import ClinicManagementPage from '@/pages/admin/ClinicManagementPage.tsx'
import CompliancePage from '@/pages/admin/CompliancePage.tsx'
import DemographicsPage from '@/pages/admin/DemographicsPage.tsx'
import DoctorRankingPage from '@/pages/admin/DoctorRankingPage.tsx'
import FinancialYearReportPage from '@/pages/admin/FinancialYearReportPage.tsx'
import BloodPage from '@/pages/admin/BloodPage.tsx'
import DepartmentsPage from '@/pages/admin/DepartmentsPage.tsx'
import DoctorsPage from '@/pages/admin/DoctorsPage.tsx'
import InsuranceClaimsPage from '@/pages/admin/InsuranceClaimsPage.tsx'
import InventoryAlertsConfigPage from '@/pages/admin/InventoryAlertsConfigPage.tsx'
import LabTestsPage from '@/pages/admin/LabTestsPage.tsx'
import MedicinesPage from '@/pages/admin/MedicinesPage.tsx'
import MonthlyReportsPage from '@/pages/admin/MonthlyReportsPage.tsx'
import OperationTheatrePage from '@/pages/admin/OperationTheatrePage.tsx'
import OrganTransplantPage from '@/pages/admin/OrganTransplantPage.tsx'
import OwnerAnalyticsPage from '@/pages/admin/OwnerAnalyticsPage.tsx'
import PatientsPage from '@/pages/admin/PatientsPage.tsx'
import PayrollPage from '@/pages/admin/PayrollPage.tsx'
import PermissionsPage from '@/pages/admin/PermissionsPage.tsx'
import RoomsPage from '@/pages/admin/RoomsPage.tsx'
import StaffAttendancePage from '@/pages/admin/StaffAttendancePage.tsx'
import StaffPage from '@/pages/admin/StaffPage.tsx'
import TraumaAlertPage from '@/pages/admin/TraumaAlertPage.tsx'
import TwoFactorSetupPage from '@/pages/admin/TwoFactorSetupPage.tsx'
import AnalyticsPage from '@/pages/doctor/AnalyticsPage.tsx'
import DicomViewerPage from '@/pages/doctor/DicomViewerPage.tsx'
import DoctorDashboard from '@/pages/doctor/DoctorDashboard.tsx'
import DoctorPatientsPage from '@/pages/doctor/DoctorPatientsPage.tsx'
import LeavePage from '@/pages/doctor/LeavePage.tsx'
import PrescriptionsPage from '@/pages/doctor/PrescriptionsPage.tsx'
import PrescriptionTemplatesPage from '@/pages/doctor/PrescriptionTemplatesPage.tsx'
import ReferralsPage from '@/pages/doctor/ReferralsPage.tsx'
import SchedulePage from '@/pages/doctor/SchedulePage.tsx'
import SymptomCheckerPage from '@/pages/doctor/SymptomCheckerPage.tsx'
import TelemedicinePage from '@/pages/doctor/TelemedicinePage.tsx'
import DoctorVideoCallPage from '@/pages/doctor/VideoCallPage.tsx'
import CompletedReportsPage from '@/pages/laboratory/CompletedReportsPage.tsx'
import LabStaffTestsPage from '@/pages/laboratory/LabTestsPage.tsx'
import LaboratoryDashboard from '@/pages/laboratory/LaboratoryDashboard.tsx'
import PendingReportsPage from '@/pages/laboratory/PendingReportsPage.tsx'
import BillsPage from '@/pages/patient/BillsPage.tsx'
import HealthTrackerPage from '@/pages/patient/HealthTrackerPage.tsx'
import InsurancePage from '@/pages/patient/InsurancePage.tsx'
import InsuranceClaimsPatientPage from '@/pages/patient/InsuranceClaimsPage.tsx'
import LabReportsPage from '@/pages/patient/LabReportsPage.tsx'
import MedicalHistoryPage from '@/pages/patient/MedicalHistoryPage.tsx'
import MedicineRemindersPage from '@/pages/patient/MedicineRemindersPage.tsx'
import NotificationsPage from '@/pages/patient/NotificationsPage.tsx'
import PatientAppointmentsPage from '@/pages/patient/PatientAppointmentsPage.tsx'
import PatientDashboard from '@/pages/patient/PatientDashboard.tsx'
import ProfilePage from '@/pages/patient/ProfilePage.tsx'
import PatientAccessLogPage from '@/pages/patient/PatientAccessLogPage.tsx'
import PatientFeedbackPage from '@/pages/patient/PatientFeedbackPage.tsx'
import PatientVideoCallPage from '@/pages/patient/VideoCallPage.tsx'
import VaccinationsPage from '@/pages/patient/VaccinationsPage.tsx'
import AlertsPage from '@/pages/pharmacist/AlertsPage.tsx'
import DrugInteractionCheckerPage from '@/pages/pharmacist/DrugInteractionCheckerPage.tsx'
import PharmacistDashboard from '@/pages/pharmacist/PharmacistDashboard.tsx'
import PharmacistMedicinesPage from '@/pages/pharmacist/PharmacistMedicinesPage.tsx'
import PurchasesPage from '@/pages/pharmacist/PurchasesPage.tsx'
import SalesPage from '@/pages/pharmacist/SalesPage.tsx'
import SuppliersPage from '@/pages/pharmacist/SuppliersPage.tsx'
import About from '@/pages/public/About.tsx'
import AppointmentBooking from '@/pages/public/AppointmentBooking.tsx'
import AmbulanceTracking from '@/pages/public/AmbulanceTracking.tsx'
import Blog from '@/pages/public/Blog.tsx'
import BlogDetail from '@/pages/public/BlogDetail.tsx'
import BloodBank from '@/pages/public/BloodBank.tsx'
import Careers from '@/pages/public/Careers.tsx'
import Contact from '@/pages/public/Contact.tsx'
import Departments from '@/pages/public/Departments.tsx'
import DoctorDetails from '@/pages/public/DoctorDetails.tsx'
import Doctors from '@/pages/public/Doctors.tsx'
import Emergency from '@/pages/public/Emergency.tsx'
import FAQ from '@/pages/public/FAQ.tsx'
import ForgotPassword from '@/pages/public/ForgotPassword.tsx'
import HealthPackages from '@/pages/public/HealthPackages.tsx'
import CanteenPage from '@/pages/public/CanteenPage.tsx'
import ParkingPage from '@/pages/public/ParkingPage.tsx'
import HospitalLocation from '@/pages/public/HospitalLocation.tsx'
import Home from '@/pages/public/Home.tsx'
import LaboratoryPublic from '@/pages/public/Laboratory.tsx'
import Login from '@/pages/public/Login.tsx'
import OnboardingWizard from '@/pages/public/OnboardingWizard.tsx'
import OnlineMedicineOrder from '@/pages/public/OnlineMedicineOrder.tsx'
import PharmacyPublic from '@/pages/public/Pharmacy.tsx'
import PrivacyPolicy from '@/pages/public/PrivacyPolicy.tsx'
import QueueDisplayPage from '@/pages/public/QueueDisplay.tsx'
import Register from '@/pages/public/Register.tsx'
import Services from '@/pages/public/Services.tsx'
import SubscriptionPage from '@/pages/public/Subscription.tsx'
import TestimonialsPage from '@/pages/public/Testimonials.tsx'
import Terms from '@/pages/public/Terms.tsx'
import VirtualTour from '@/pages/public/VirtualTour.tsx'
import AdmitDischargePage from '@/pages/receptionist/AdmitDischargePage.tsx'
import BillingPage from '@/pages/receptionist/BillingPage.tsx'
import BookAppointmentPage from '@/pages/receptionist/BookAppointmentPage.tsx'
import CheckInPage from '@/pages/receptionist/CheckInPage.tsx'
import QueueTokenPage from '@/pages/receptionist/QueueTokenPage.tsx'
import ReceptionistDashboard from '@/pages/receptionist/ReceptionistDashboard.tsx'
import RegisterPatientPage from '@/pages/receptionist/RegisterPatientPage.tsx'
import { Navigate, Route, Routes } from 'react-router-dom'

const LazyAdminDashboard = lazy(async () => ({ default: AdminDashboard }))
const LazyDoctorDashboard = lazy(async () => ({ default: DoctorDashboard }))
const LazyPatientDashboard = lazy(async () => ({ default: PatientDashboard }))
const LazyReceptionDashboard = lazy(async () => ({ default: ReceptionistDashboard }))
const LazyLabDashboard = lazy(async () => ({ default: LaboratoryDashboard }))
const LazyPharmacistDashboard = lazy(async () => ({ default: PharmacistDashboard }))

function LazyWrapper({ children }: { children: ReactNode }) {
  return <Suspense fallback={<div className="py-10 text-center text-sm text-slate-500">Loading...</div>}>{children}</Suspense>
}

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<PublicLayout />}>
        <Route index element={<Home />} />
        <Route path="about" element={<About />} />
        <Route path="departments" element={<Departments />} />
        <Route path="doctors" element={<Doctors />} />
        <Route path="doctors/:id" element={<DoctorDetails />} />
        <Route path="services" element={<Services />} />
        <Route path="health-packages" element={<HealthPackages />} />
        <Route path="virtual-tour" element={<VirtualTour />} />
        <Route path="testimonials" element={<TestimonialsPage />} />
        <Route path="faq" element={<FAQ />} />
        <Route path="ambulance-tracking" element={<AmbulanceTracking />} />
        <Route path="hospital-location" element={<HospitalLocation />} />
        <Route path="canteen" element={<CanteenPage />} />
        <Route path="parking" element={<ParkingPage />} />
        <Route path="medicine-order" element={<OnlineMedicineOrder />} />
        <Route path="subscription" element={<SubscriptionPage />} />
        <Route path="onboarding" element={<OnboardingWizard />} />
        <Route path="queue-display" element={<QueueDisplayPage />} />
        <Route path="appointment" element={<AppointmentBooking />} />
        <Route path="emergency" element={<Emergency />} />
        <Route path="blood-bank" element={<BloodBank />} />
        <Route path="lab-services" element={<LaboratoryPublic />} />
        <Route path="pharmacy" element={<PharmacyPublic />} />
        <Route path="careers" element={<Careers />} />
        <Route path="blog" element={<Blog />} />
        <Route path="blog/:id" element={<BlogDetail />} />
        <Route path="contact" element={<Contact />} />
        <Route path="login" element={<Login />} />
        <Route path="register" element={<Register />} />
        <Route path="forgot-password" element={<ForgotPassword />} />
        <Route path="privacy" element={<PrivacyPolicy />} />
        <Route path="terms" element={<Terms />} />
      </Route>

      <Route
        path="admin"
        element={
          <ProtectedRoute roles={['ADMIN']}>
            <DashboardLayout sidebarItems={adminSidebar} title="Admin" />
          </ProtectedRoute>
        }
      >
        <Route index element={<LazyWrapper><LazyAdminDashboard /></LazyWrapper>} />
        <Route path="doctors" element={<DoctorsPage />} />
        <Route path="patients" element={<PatientsPage />} />
        <Route path="departments" element={<DepartmentsPage />} />
        <Route path="staff" element={<StaffPage />} />
        <Route path="appointments" element={<AppointmentsPage />} />
        <Route path="medicines" element={<MedicinesPage />} />
        <Route path="lab-tests" element={<LabTestsPage />} />
        <Route path="blood" element={<BloodPage />} />
        <Route path="rooms" element={<RoomsPage />} />
        <Route path="beds" element={<BedsPage />} />
        <Route path="ambulances" element={<AmbulancesPage />} />
        <Route path="blog" element={<BlogPage />} />
        <Route path="payroll" element={<PayrollPage />} />
        <Route path="inventory-alerts" element={<InventoryAlertsConfigPage />} />
        <Route path="audit-logs" element={<AuditLogsPage />} />
        <Route path="branches" element={<BranchesPage />} />
        <Route path="permissions" element={<PermissionsPage />} />
        <Route path="trauma-alerts" element={<TraumaAlertPage />} />
        <Route path="branding-settings" element={<BrandingSettingsPage />} />
        <Route path="owner-analytics" element={<OwnerAnalyticsPage />} />
        <Route path="clinic-management" element={<ClinicManagementPage />} />
        <Route path="staff-attendance" element={<StaffAttendancePage />} />
        <Route path="insurance-claims" element={<InsuranceClaimsPage />} />
        <Route path="operation-theatre" element={<OperationTheatrePage />} />
        <Route path="organ-transplant" element={<OrganTransplantPage />} />
        <Route path="monthly-reports" element={<MonthlyReportsPage />} />
        <Route path="doctor-ranking" element={<DoctorRankingPage />} />
        <Route path="demographics" element={<DemographicsPage />} />
        <Route path="financial-year-report" element={<FinancialYearReportPage />} />
        <Route path="two-factor" element={<TwoFactorSetupPage />} />
        <Route path="compliance" element={<CompliancePage />} />
        <Route path="canteen" element={<CanteenAdminPage />} />
        <Route path="parking" element={<ParkingAdminPage />} />
      </Route>

      <Route
        path="doctor"
        element={
          <ProtectedRoute roles={['DOCTOR']}>
            <DashboardLayout sidebarItems={doctorSidebar} title="Doctor" />
          </ProtectedRoute>
        }
      >
        <Route index element={<LazyWrapper><LazyDoctorDashboard /></LazyWrapper>} />
        <Route path="patients" element={<DoctorPatientsPage />} />
        <Route path="prescriptions" element={<PrescriptionsPage />} />
        <Route path="schedule" element={<SchedulePage />} />
        <Route path="leave" element={<LeavePage />} />
        <Route path="analytics" element={<AnalyticsPage />} />
        <Route path="prescription-templates" element={<PrescriptionTemplatesPage />} />
        <Route path="symptom-checker" element={<SymptomCheckerPage />} />
        <Route path="telemedicine" element={<TelemedicinePage />} />
        <Route path="referrals" element={<ReferralsPage />} />
        <Route path="dicom-viewer" element={<DicomViewerPage />} />
        <Route path="video-call" element={<DoctorVideoCallPage />} />
      </Route>

      <Route
        path="patient"
        element={
          <ProtectedRoute roles={['PATIENT']}>
            <DashboardLayout sidebarItems={patientSidebar} title="Patient" />
          </ProtectedRoute>
        }
      >
        <Route index element={<LazyWrapper><LazyPatientDashboard /></LazyWrapper>} />
        <Route path="appointments" element={<PatientAppointmentsPage />} />
        <Route path="medical-history" element={<MedicalHistoryPage />} />
        <Route path="lab-reports" element={<LabReportsPage />} />
        <Route path="bills" element={<BillsPage />} />
        <Route path="profile" element={<ProfilePage />} />
        <Route path="notifications" element={<NotificationsPage />} />
        <Route path="health-tracker" element={<HealthTrackerPage />} />
        <Route path="medicine-reminders" element={<MedicineRemindersPage />} />
        <Route path="insurance" element={<InsurancePage />} />
        <Route path="vaccinations" element={<VaccinationsPage />} />
        <Route path="insurance-claims" element={<InsuranceClaimsPatientPage />} />
        <Route path="feedback" element={<PatientFeedbackPage />} />
        <Route path="video-call" element={<PatientVideoCallPage />} />
        <Route path="access-logs" element={<PatientAccessLogPage />} />
        <Route path="canteen" element={<CanteenPage />} />
      </Route>

      <Route
        path="receptionist"
        element={
          <ProtectedRoute roles={['RECEPTIONIST']}>
            <DashboardLayout sidebarItems={receptionistSidebar} title="Reception" />
          </ProtectedRoute>
        }
      >
        <Route index element={<LazyWrapper><LazyReceptionDashboard /></LazyWrapper>} />
        <Route path="register-patient" element={<RegisterPatientPage />} />
        <Route path="book-appointment" element={<BookAppointmentPage />} />
        <Route path="check-in" element={<CheckInPage />} />
        <Route path="billing" element={<BillingPage />} />
        <Route path="admit-discharge" element={<AdmitDischargePage />} />
        <Route path="queue-tokens" element={<QueueTokenPage />} />
      </Route>

      <Route
        path="laboratory"
        element={
          <ProtectedRoute roles={['LABORATORY_STAFF']}>
            <DashboardLayout sidebarItems={laboratorySidebar} title="Laboratory" />
          </ProtectedRoute>
        }
      >
        <Route index element={<LazyWrapper><LazyLabDashboard /></LazyWrapper>} />
        <Route path="tests" element={<LabStaffTestsPage />} />
        <Route path="pending" element={<PendingReportsPage />} />
        <Route path="completed" element={<CompletedReportsPage />} />
      </Route>

      <Route
        path="pharmacist"
        element={
          <ProtectedRoute roles={['PHARMACIST']}>
            <DashboardLayout sidebarItems={pharmacistSidebar} title="Pharmacy" />
          </ProtectedRoute>
        }
      >
        <Route index element={<LazyWrapper><LazyPharmacistDashboard /></LazyWrapper>} />
        <Route path="medicines" element={<PharmacistMedicinesPage />} />
        <Route path="suppliers" element={<SuppliersPage />} />
        <Route path="purchases" element={<PurchasesPage />} />
        <Route path="sales" element={<SalesPage />} />
        <Route path="alerts" element={<AlertsPage />} />
        <Route path="interactions" element={<DrugInteractionCheckerPage />} />
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
