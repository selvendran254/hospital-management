import { Languages } from 'lucide-react'
import { useTranslation } from 'react-i18next'

const languageOptions = [
  { code: 'en', label: 'EN' },
  { code: 'ta', label: 'TA' },
]

export default function LanguageSwitcher() {
  const { i18n } = useTranslation()

  return (
    <div className="relative">
      <label className="sr-only" htmlFor="language-switcher">Language</label>
      <div className="pointer-events-none absolute inset-y-0 left-2 flex items-center">
        <Languages className="h-4 w-4 text-slate-500" />
      </div>
      <select
        id="language-switcher"
        value={i18n.language}
        onChange={(event) => {
          void i18n.changeLanguage(event.target.value)
        }}
        className="rounded-lg border border-slate-300 bg-white py-1 pl-8 pr-2 text-sm text-slate-700 dark:border-slate-600 dark:bg-slate-800 dark:text-slate-200"
      >
        {languageOptions.map((option) => (
          <option key={option.code} value={option.code}>
            {option.label}
          </option>
        ))}
      </select>
    </div>
  )
}
