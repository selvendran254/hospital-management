interface ExportButtonsProps<T> {
  rows: T[]
  fileName: string
}

function downloadCSV(content: string, fileName: string) {
  const blob = new Blob([content], { type: 'text/csv;charset=utf-8;' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', `${fileName}.csv`)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

export default function ExportButtons<T extends Record<string, unknown>>({ rows, fileName }: ExportButtonsProps<T>) {
  const exportCSV = () => {
    if (rows.length === 0) {
      return
    }
    const headers = Object.keys(rows[0])
    const csv = [
      headers.join(','),
      ...rows.map((row) =>
        headers
          .map((header) => `"${String(row[header] ?? '').replace(/"/g, '""')}"`)
          .join(','),
      ),
    ].join('\n')
    downloadCSV(csv, fileName)
  }

  return (
    <button type="button" className="btn-secondary !py-1.5 text-xs" onClick={exportCSV}>
      Export CSV
    </button>
  )
}
