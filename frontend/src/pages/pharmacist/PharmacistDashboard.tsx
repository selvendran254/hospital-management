import StatCard from '@/components/StatCard.tsx'
import { AlertTriangle, Package, Pill, ShoppingCart } from 'lucide-react'
import { Link } from 'react-router-dom'

export default function PharmacistDashboard() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Pharmacy Dashboard</h1>
        <p className="page-subtitle">Pharmacy inventory and sales</p>
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-4">
        <StatCard title="Total Medicines" value={342} icon={Pill} color="teal" />
        <StatCard title="Low Stock Alerts" value={12} icon={AlertTriangle} color="amber" />
        <StatCard title="Today's Sales" value={45} icon={Package} color="blue" />
        <StatCard title="Pending Purchases" value={3} icon={ShoppingCart} color="violet" />
      </div>
      <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {[
          { to: '/pharmacist/medicines', title: 'Medicines', desc: 'Manage medicine inventory' },
          { to: '/pharmacist/suppliers', title: 'Suppliers', desc: 'Supplier management' },
          { to: '/pharmacist/purchases', title: 'Purchases', desc: 'Record stock purchases' },
          { to: '/pharmacist/sales', title: 'Sales', desc: 'Dispense and sell medicines' },
          { to: '/pharmacist/alerts', title: 'Stock Alerts', desc: 'Low stock notifications' },
          { to: '/pharmacist/interactions', title: 'Drug Interactions', desc: 'Check medicine compatibility' },
        ].map((item) => (
          <Link key={item.to} to={item.to} className="card hover:border-primary-300">
            <h3 className="font-semibold">{item.title}</h3>
            <p className="mt-1 text-sm text-slate-500">{item.desc}</p>
          </Link>
        ))}
      </div>
    </div>
  )
}
