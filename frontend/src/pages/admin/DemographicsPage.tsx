import ChartCard from '@/components/ChartCard.tsx'
import { Bar, BarChart, Cell, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from 'recharts'

const genderData = [
  { name: 'Female', value: 48 },
  { name: 'Male', value: 46 },
  { name: 'Other', value: 6 },
]

const ageGroupData = [
  { group: '0-18', count: 210 },
  { group: '19-35', count: 530 },
  { group: '36-50', count: 410 },
  { group: '51-65', count: 290 },
  { group: '65+', count: 190 },
]

const pieColors = ['#14b8a6', '#0ea5e9', '#a855f7']

export default function DemographicsPage() {
  return (
    <div className="space-y-6">
      <div>
        <h1 className="page-title">Patient Demographics</h1>
        <p className="page-subtitle">Population distribution across age and gender cohorts.</p>
      </div>
      <div className="grid gap-6 lg:grid-cols-2">
        <ChartCard title="Gender Distribution">
          <ResponsiveContainer width="100%" height="100%">
            <PieChart>
              <Pie data={genderData} dataKey="value" nameKey="name" outerRadius={96} label>
                {genderData.map((entry, index) => (
                  <Cell key={`${entry.name}-${index}`} fill={pieColors[index % pieColors.length]} />
                ))}
              </Pie>
              <Tooltip />
            </PieChart>
          </ResponsiveContainer>
        </ChartCard>
        <ChartCard title="Age Group Distribution">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={ageGroupData}>
              <XAxis dataKey="group" className="text-xs" />
              <YAxis className="text-xs" />
              <Tooltip />
              <Bar dataKey="count" fill="#14b8a6" radius={[4, 4, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </ChartCard>
      </div>
    </div>
  )
}
