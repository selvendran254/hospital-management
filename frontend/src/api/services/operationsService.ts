import api from '../axios.ts'

export interface MapLocation {
  name: string
  address: string
  latitude: number
  longitude: number
  googleMapsUrl: string
  directionsUrl: string
}

export interface AmbulanceRoute {
  id: string
  vehicleNumber: string
  driverName: string
  driverPhone: string
  location: string
  latitude: number
  longitude: number
  destinationLat: number
  destinationLng: number
  routeUrl: string
  available: boolean
}

export interface CanteenMenuItem {
  id: string
  name: string
  description?: string
  price: number
  category?: string
  veg: boolean
  available: boolean
  imageUrl?: string
}

export interface FoodOrder {
  id: string
  patientId?: string
  menuItemId: string
  itemName: string
  quantity: number
  totalAmount: number
  roomNumber?: string
  notes?: string
  status: 'PLACED' | 'PREPARING' | 'DELIVERED' | 'CANCELLED'
  createdAt?: string
}

export interface ParkingSlot {
  id: string
  slotNumber: string
  floorLevel?: string
  slotType: 'VISITOR' | 'STAFF' | 'DISABLED' | 'EMERGENCY'
  status: 'AVAILABLE' | 'OCCUPIED' | 'RESERVED' | 'MAINTENANCE'
  vehicleNumber?: string
  visitorName?: string
  visitorPhone?: string
}

export const operationsService = {
  getHospitalLocation: () => api.get<MapLocation>('/public/maps/hospital').then((r) => r.data),
  getAmbulanceRoutes: () => api.get<AmbulanceRoute[]>('/public/maps/ambulances').then((r) => r.data),
  getCanteenMenu: () => api.get<CanteenMenuItem[]>('/public/canteen/menu').then((r) => r.data),
  getAllMenu: () => api.get<CanteenMenuItem[]>('/canteen/menu').then((r) => r.data),
  createMenuItem: (data: Partial<CanteenMenuItem>) => api.post<CanteenMenuItem>('/canteen/menu', data).then((r) => r.data),
  updateMenuItem: (id: string, data: Partial<CanteenMenuItem>) => api.put<CanteenMenuItem>(`/canteen/menu/${id}`, data).then((r) => r.data),
  deleteMenuItem: (id: string) => api.delete(`/canteen/menu/${id}`),
  placeFoodOrder: (data: { menuItemId: string; quantity: number; roomNumber?: string; notes?: string; patientId?: string }) =>
    api.post<FoodOrder>('/canteen/orders', data).then((r) => r.data),
  getFoodOrders: () => api.get<FoodOrder[]>('/canteen/orders').then((r) => r.data),
  updateOrderStatus: (id: string, status: string) =>
    api.patch<FoodOrder>(`/canteen/orders/${id}/status`, { status }).then((r) => r.data),
  getAvailableParking: () => api.get<ParkingSlot[]>('/public/parking').then((r) => r.data),
  getAllParking: () => api.get<ParkingSlot[]>('/parking').then((r) => r.data),
  getParkingStats: () => api.get<{ available: number }>('/parking/stats').then((r) => r.data),
  createParkingSlot: (data: { slotNumber: string; floorLevel?: string; slotType?: string }) =>
    api.post<ParkingSlot>('/parking/slots', data).then((r) => r.data),
  reserveParking: (data: { visitorName: string; visitorPhone: string; vehicleNumber: string; slotId?: string }) =>
    api.post<ParkingSlot>('/public/parking/reserve', data).then((r) => r.data),
  releaseParking: (id: string) => api.patch<ParkingSlot>(`/parking/${id}/release`).then((r) => r.data),
}
