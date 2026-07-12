import { useCallback, useState } from 'react'

export function usePagination(initialPage = 0, initialSize = 10) {
  const [page, setPage] = useState(initialPage)
  const [size, setSize] = useState(initialSize)

  const nextPage = useCallback(() => setPage((p) => p + 1), [])
  const prevPage = useCallback(() => setPage((p) => Math.max(0, p - 1)), [])
  const resetPage = useCallback(() => setPage(0), [])

  return { page, size, setPage, setSize, nextPage, prevPage, resetPage }
}
