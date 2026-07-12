-- Hospital Management System - PostgreSQL Schema
-- Run: psql -U postgres -d hospital_db -f schema.sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===================== ENUMS =====================
CREATE TYPE user_role AS ENUM (
  'ADMIN', 'DOCTOR', 'RECEPTIONIST', 'LABORATORY_STAFF', 'PHARMACIST', 'PATIENT'
);
CREATE TYPE appointment_status AS ENUM ('PENDING', 'APPROVED', 'COMPLETED', 'CANCELLED');
CREATE TYPE bill_status AS ENUM ('PENDING', 'PAID', 'PARTIAL', 'CANCELLED');
CREATE TYPE payment_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED', 'REFUNDED');
CREATE TYPE room_type AS ENUM ('WARD', 'ICU', 'PRIVATE', 'GENERAL');
CREATE TYPE bed_status AS ENUM ('AVAILABLE', 'OCCUPIED', 'MAINTENANCE', 'RESERVED');
CREATE TYPE blood_group AS ENUM ('A_POSITIVE','A_NEGATIVE','B_POSITIVE','B_NEGATIVE','AB_POSITIVE','AB_NEGATIVE','O_POSITIVE','O_NEGATIVE');
CREATE TYPE notification_type AS ENUM ('APPOINTMENT_BOOKED','APPOINTMENT_APPROVED','APPOINTMENT_CANCELLED','LAB_REPORT_READY','BILL_GENERATED','GENERAL');
CREATE TYPE admission_status AS ENUM ('ADMITTED', 'DISCHARGED', 'TRANSFERRED');

-- ===================== CORE TABLES =====================
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role user_role NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  profile_picture_url VARCHAR(500),
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE departments (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL UNIQUE,
  description TEXT,
  head_doctor_id UUID,
  floor_number INT,
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE doctors (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  department_id UUID REFERENCES departments(id),
  specialization VARCHAR(255) NOT NULL,
  qualification VARCHAR(255),
  experience_years INT DEFAULT 0,
  consultation_fee DECIMAL(10,2) DEFAULT 0,
  gender VARCHAR(10),
  bio TEXT,
  is_available BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

ALTER TABLE departments ADD CONSTRAINT fk_dept_head FOREIGN KEY (head_doctor_id) REFERENCES doctors(id);

CREATE TABLE patients (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  mrn VARCHAR(50) NOT NULL UNIQUE,
  date_of_birth DATE,
  gender VARCHAR(10),
  blood_group blood_group,
  address TEXT,
  emergency_contact VARCHAR(20),
  allergies TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE staff (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
  department_id UUID REFERENCES departments(id),
  designation VARCHAR(100) NOT NULL,
  employee_id VARCHAR(50) UNIQUE,
  joining_date DATE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ===================== SCHEDULING =====================
CREATE TABLE doctor_schedules (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
  day_of_week INT NOT NULL CHECK (day_of_week BETWEEN 0 AND 6),
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  slot_duration_mins INT DEFAULT 30,
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE doctor_leaves (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  doctor_id UUID NOT NULL REFERENCES doctors(id) ON DELETE CASCADE,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  reason TEXT,
  is_approved BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE appointments (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id UUID NOT NULL REFERENCES patients(id),
  doctor_id UUID NOT NULL REFERENCES doctors(id),
  appointment_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  status appointment_status DEFAULT 'PENDING',
  reason TEXT,
  notes TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ===================== MEDICAL RECORDS =====================
CREATE TABLE prescriptions (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  appointment_id UUID REFERENCES appointments(id),
  patient_id UUID NOT NULL REFERENCES patients(id),
  doctor_id UUID NOT NULL REFERENCES doctors(id),
  diagnosis TEXT,
  medications TEXT NOT NULL,
  instructions TEXT,
  follow_up_date DATE,
  pdf_url VARCHAR(500),
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE medical_records (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id UUID NOT NULL REFERENCES patients(id),
  doctor_id UUID REFERENCES doctors(id),
  visit_date DATE NOT NULL,
  chief_complaint TEXT,
  diagnosis TEXT,
  treatment TEXT,
  vitals JSONB,
  attachments JSONB,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ===================== LABORATORY =====================
CREATE TABLE lab_tests (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL,
  code VARCHAR(50) UNIQUE,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  sample_type VARCHAR(100),
  turnaround_hours INT DEFAULT 24,
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE lab_reports (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id UUID NOT NULL REFERENCES patients(id),
  lab_test_id UUID NOT NULL REFERENCES lab_tests(id),
  appointment_id UUID REFERENCES appointments(id),
  ordered_by UUID REFERENCES doctors(id),
  result TEXT,
  report_file_url VARCHAR(500),
  status VARCHAR(20) DEFAULT 'PENDING',
  completed_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ===================== PHARMACY =====================
CREATE TABLE suppliers (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL,
  contact_person VARCHAR(255),
  phone VARCHAR(20),
  email VARCHAR(255),
  address TEXT,
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE medicines (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL,
  generic_name VARCHAR(255),
  category VARCHAR(100),
  manufacturer VARCHAR(255),
  unit_price DECIMAL(10,2) NOT NULL,
  stock_quantity INT DEFAULT 0,
  reorder_level INT DEFAULT 10,
  expiry_date DATE,
  batch_number VARCHAR(100),
  is_active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE pharmacy_purchases (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  supplier_id UUID NOT NULL REFERENCES suppliers(id),
  medicine_id UUID NOT NULL REFERENCES medicines(id),
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  total_amount DECIMAL(12,2) NOT NULL,
  purchase_date DATE NOT NULL,
  invoice_number VARCHAR(100),
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE pharmacy_sales (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  medicine_id UUID NOT NULL REFERENCES medicines(id),
  patient_id UUID REFERENCES patients(id),
  quantity INT NOT NULL,
  unit_price DECIMAL(10,2) NOT NULL,
  total_amount DECIMAL(12,2) NOT NULL,
  sold_by UUID REFERENCES users(id),
  sale_date TIMESTAMPTZ DEFAULT NOW()
);

-- ===================== BILLING =====================
CREATE TABLE bills (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  bill_number VARCHAR(50) NOT NULL UNIQUE,
  patient_id UUID NOT NULL REFERENCES patients(id),
  appointment_id UUID REFERENCES appointments(id),
  total_amount DECIMAL(12,2) NOT NULL,
  paid_amount DECIMAL(12,2) DEFAULT 0,
  status bill_status DEFAULT 'PENDING',
  description TEXT,
  generated_by UUID REFERENCES users(id),
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE payments (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  bill_id UUID NOT NULL REFERENCES bills(id),
  amount DECIMAL(12,2) NOT NULL,
  payment_method VARCHAR(50) DEFAULT 'CARD',
  transaction_id VARCHAR(255),
  status payment_status DEFAULT 'COMPLETED',
  paid_at TIMESTAMPTZ DEFAULT NOW()
);

-- ===================== BLOOD BANK =====================
CREATE TABLE blood_inventory (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  blood_group blood_group NOT NULL,
  units_available INT DEFAULT 0,
  last_updated TIMESTAMPTZ DEFAULT NOW(),
  UNIQUE(blood_group)
);

CREATE TABLE blood_donors (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  full_name VARCHAR(255) NOT NULL,
  blood_group blood_group NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(255),
  last_donation_date DATE,
  is_eligible BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE blood_requests (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id UUID REFERENCES patients(id),
  blood_group blood_group NOT NULL,
  units_required INT NOT NULL,
  urgency VARCHAR(20) DEFAULT 'NORMAL',
  status VARCHAR(20) DEFAULT 'PENDING',
  requested_by UUID REFERENCES users(id),
  fulfilled_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ===================== ROOM MANAGEMENT =====================
CREATE TABLE rooms (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  room_number VARCHAR(20) NOT NULL UNIQUE,
  room_type room_type NOT NULL,
  floor_number INT,
  department_id UUID REFERENCES departments(id),
  daily_rate DECIMAL(10,2) DEFAULT 0,
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE beds (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  room_id UUID NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  bed_number VARCHAR(10) NOT NULL,
  status bed_status DEFAULT 'AVAILABLE',
  UNIQUE(room_id, bed_number)
);

CREATE TABLE admissions (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  patient_id UUID NOT NULL REFERENCES patients(id),
  bed_id UUID NOT NULL REFERENCES beds(id),
  doctor_id UUID REFERENCES doctors(id),
  admission_date TIMESTAMPTZ NOT NULL,
  discharge_date TIMESTAMPTZ,
  status admission_status DEFAULT 'ADMITTED',
  diagnosis TEXT,
  notes TEXT,
  admitted_by UUID REFERENCES users(id)
);

-- ===================== EMERGENCY & SERVICES =====================
CREATE TABLE ambulances (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  vehicle_number VARCHAR(20) NOT NULL UNIQUE,
  driver_name VARCHAR(255),
  driver_phone VARCHAR(20) NOT NULL,
  is_available BOOLEAN DEFAULT TRUE,
  current_location VARCHAR(255)
);

CREATE TABLE hospital_services (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL,
  description TEXT,
  icon VARCHAR(50),
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE health_packages (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price DECIMAL(10,2) NOT NULL,
  includes JSONB,
  duration_days INT,
  is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE blog_posts (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  title VARCHAR(500) NOT NULL,
  slug VARCHAR(500) NOT NULL UNIQUE,
  content TEXT NOT NULL,
  excerpt TEXT,
  cover_image_url VARCHAR(500),
  author_id UUID REFERENCES users(id),
  is_published BOOLEAN DEFAULT FALSE,
  published_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE notifications (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  title VARCHAR(255) NOT NULL,
  message TEXT NOT NULL,
  type notification_type DEFAULT 'GENERAL',
  is_read BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE password_reset_tokens (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  token VARCHAR(255) NOT NULL UNIQUE,
  expires_at TIMESTAMPTZ NOT NULL,
  used BOOLEAN DEFAULT FALSE
);

-- ===================== INDEXES =====================
CREATE INDEX idx_appointments_date ON appointments(appointment_date, doctor_id);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_patients_mrn ON patients(mrn);
CREATE INDEX idx_bills_patient ON bills(patient_id);
CREATE INDEX idx_notifications_user ON notifications(user_id, is_read);
CREATE INDEX idx_medicines_stock ON medicines(stock_quantity);
CREATE INDEX idx_lab_reports_status ON lab_reports(status);

-- ===================== SEED DATA =====================
INSERT INTO blood_inventory (blood_group, units_available) VALUES
  ('A_POSITIVE', 50), ('A_NEGATIVE', 20), ('B_POSITIVE', 45), ('B_NEGATIVE', 18),
  ('AB_POSITIVE', 15), ('AB_NEGATIVE', 8), ('O_POSITIVE', 60), ('O_NEGATIVE', 25);

INSERT INTO departments (name, description, floor_number) VALUES
  ('Cardiology', 'Heart and cardiovascular care', 2),
  ('Neurology', 'Brain and nervous system', 3),
  ('Orthopedics', 'Bone and joint care', 1),
  ('Pediatrics', 'Child healthcare', 2),
  ('General Medicine', 'Primary healthcare', 1),
  ('Emergency', '24/7 emergency care', 0),
  ('Laboratory', 'Diagnostic tests', 0),
  ('Pharmacy', 'Medicine dispensing', 0);

INSERT INTO lab_tests (name, code, description, price, sample_type) VALUES
  ('Complete Blood Count', 'CBC', 'Full blood cell analysis', 500.00, 'Blood'),
  ('Lipid Profile', 'LIPID', 'Cholesterol and triglycerides', 800.00, 'Blood'),
  ('Thyroid Panel', 'THYROID', 'TSH, T3, T4 levels', 1200.00, 'Blood'),
  ('Urine Analysis', 'UA', 'Complete urine test', 300.00, 'Urine'),
  ('X-Ray Chest', 'XRAY-CHEST', 'Chest radiograph', 600.00, 'Imaging');

INSERT INTO hospital_services (name, description, icon) VALUES
  ('Emergency Care', '24/7 emergency medical services', 'ambulance'),
  ('Laboratory', 'Advanced diagnostic testing', 'flask'),
  ('Pharmacy', 'In-house medicine dispensing', 'pill'),
  ('Blood Bank', 'Blood donation and transfusion', 'droplet'),
  ('ICU', 'Intensive care unit', 'heart-pulse'),
  ('Surgery', 'Advanced surgical procedures', 'scissors');

INSERT INTO health_packages (name, description, price, includes, duration_days) VALUES
  ('Basic Health Checkup', 'Essential health screening', 1999.00, '["CBC","BP Check","BMI","Doctor Consultation"]', 1),
  ('Executive Health Package', 'Comprehensive executive screening', 4999.00, '["CBC","Lipid Profile","Thyroid","ECG","X-Ray","Doctor Consultation"]', 1),
  ('Senior Citizen Package', 'Health checkup for seniors', 3499.00, '["CBC","Lipid Profile","Bone Density","Eye Check","Doctor Consultation"]', 1);

INSERT INTO rooms (room_number, room_type, floor_number, daily_rate) VALUES
  ('W-101', 'WARD', 1, 1500.00),
  ('W-102', 'WARD', 1, 1500.00),
  ('ICU-01', 'ICU', 0, 5000.00),
  ('ICU-02', 'ICU', 0, 5000.00),
  ('P-201', 'PRIVATE', 2, 3500.00),
  ('G-301', 'GENERAL', 3, 1000.00);

INSERT INTO beds (room_id, bed_number, status)
SELECT r.id, 'B' || generate_series(1, 4), 'AVAILABLE' FROM rooms r WHERE r.room_type IN ('WARD', 'GENERAL');

INSERT INTO beds (room_id, bed_number, status)
SELECT r.id, 'B1', 'AVAILABLE' FROM rooms r WHERE r.room_type IN ('ICU', 'PRIVATE');

INSERT INTO ambulances (vehicle_number, driver_name, driver_phone, is_available) VALUES
  ('AMB-001', 'Ramesh Kumar', '+919876543210', TRUE),
  ('AMB-002', 'Suresh Patel', '+919876543211', TRUE),
  ('AMB-003', 'Vijay Singh', '+919876543212', FALSE);

-- ===================== SAAS + ADVANCED EXTENSIONS =====================
CREATE TABLE IF NOT EXISTS clinics (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  code VARCHAR(120) NOT NULL UNIQUE,
  name VARCHAR(180) NOT NULL,
  address TEXT,
  city VARCHAR(100),
  phone VARCHAR(20),
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

INSERT INTO clinics (code, name, address, city, phone)
VALUES ('MAIN', 'City Care Main Clinic', 'Anna Nagar, Chennai', 'Chennai', '+914400000001')
ON CONFLICT (code) DO NOTHING;

ALTER TABLE users ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE departments ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE doctors ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE patients ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE staff ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE appointments ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE bills ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE payments ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);
ALTER TABLE medical_records ADD COLUMN IF NOT EXISTS clinic_id UUID REFERENCES clinics(id);

CREATE TABLE IF NOT EXISTS tenant_branding (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID REFERENCES clinics(id),
  hospital_name VARCHAR(200) NOT NULL,
  logo_url VARCHAR(500),
  primary_color VARCHAR(20),
  secondary_color VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS media_assets (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  category VARCHAR(20) NOT NULL,
  file_type VARCHAR(20) NOT NULL,
  file_path VARCHAR(500) NOT NULL,
  public_url VARCHAR(500) NOT NULL,
  uploaded_by UUID REFERENCES users(id),
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS staff_attendance (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  staff_id UUID NOT NULL REFERENCES staff(id),
  biometric_id VARCHAR(64),
  check_in TIMESTAMPTZ NOT NULL,
  check_out TIMESTAMPTZ,
  method VARCHAR(20) NOT NULL,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS insurance_claims (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  patient_id UUID NOT NULL REFERENCES patients(id),
  bill_id UUID REFERENCES bills(id),
  insurance_provider VARCHAR(150) NOT NULL,
  policy_number VARCHAR(100) NOT NULL,
  claim_amount DECIMAL(12,2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  review_notes TEXT,
  processed_at TIMESTAMPTZ,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS patient_feedback (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  patient_id UUID NOT NULL REFERENCES patients(id),
  doctor_id UUID REFERENCES doctors(id),
  rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
  review TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS medical_images (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  patient_id UUID NOT NULL REFERENCES patients(id),
  image_url VARCHAR(500) NOT NULL,
  image_type VARCHAR(60) NOT NULL,
  notes TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS operation_theatres (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  theatre_code VARCHAR(50) NOT NULL,
  name VARCHAR(150) NOT NULL,
  floor_number INT,
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS surgery_schedules (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  patient_id UUID NOT NULL REFERENCES patients(id),
  doctor_id UUID NOT NULL REFERENCES doctors(id),
  operation_theatre_id UUID NOT NULL REFERENCES operation_theatres(id),
  surgery_name VARCHAR(200) NOT NULL,
  surgery_date DATE NOT NULL,
  start_time TIME NOT NULL,
  end_time TIME NOT NULL,
  status VARCHAR(20) NOT NULL,
  notes TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS organ_transplant_registry (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  donor_name VARCHAR(180) NOT NULL,
  recipient_name VARCHAR(180) NOT NULL,
  organ_type VARCHAR(80) NOT NULL,
  status VARCHAR(20) NOT NULL,
  notes TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS two_factor_auth_secrets (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID NOT NULL UNIQUE REFERENCES users(id),
  secret VARCHAR(128) NOT NULL,
  enabled BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMPTZ DEFAULT NOW(),
  updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS consent_records (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  patient_id UUID NOT NULL REFERENCES patients(id),
  consent_type VARCHAR(80) NOT NULL,
  granted BOOLEAN NOT NULL,
  metadata_json TEXT,
  created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS patient_access_logs (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  clinic_id UUID NOT NULL REFERENCES clinics(id),
  patient_id UUID NOT NULL REFERENCES patients(id),
  viewer_user_id UUID NOT NULL REFERENCES users(id),
  viewer_role VARCHAR(60),
  accessed_at TIMESTAMPTZ NOT NULL,
  action VARCHAR(100),
  created_at TIMESTAMPTZ DEFAULT NOW()
);
