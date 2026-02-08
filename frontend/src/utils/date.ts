export function formatDate(dateStr: string, full = false): string {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString(
    'ja-JP',
    full ? { year: 'numeric', month: 'long', day: 'numeric' } : undefined,
  )
}
