import type { InputHTMLAttributes, SelectHTMLAttributes, TextareaHTMLAttributes } from 'react'
import type { FieldError, UseFormRegisterReturn } from 'react-hook-form'

interface BaseProps {
  label: string
  error?: FieldError
  required?: boolean
}

interface InputProps extends BaseProps, InputHTMLAttributes<HTMLInputElement> {
  registration?: UseFormRegisterReturn
}

interface SelectProps extends BaseProps, SelectHTMLAttributes<HTMLSelectElement> {
  registration?: UseFormRegisterReturn
  options: Array<{ value: string; label: string }>
}

interface TextareaProps extends BaseProps, TextareaHTMLAttributes<HTMLTextAreaElement> {
  registration?: UseFormRegisterReturn
}

function FieldWrapper({
  label,
  error,
  required,
  children,
}: {
  label: string
  error?: FieldError
  required?: boolean
  children: React.ReactNode
}) {
  return (
    <div className="space-y-1">
      <label className="block text-sm font-medium text-slate-700 dark:text-slate-300">
        {label}
        {required && <span className="ml-1 text-red-500">*</span>}
      </label>
      {children}
      {error && <p className="text-xs text-red-500">{error.message}</p>}
    </div>
  )
}

export function FormInput({ label, error, required, registration, className = '', ...props }: InputProps) {
  return (
    <FieldWrapper label={label} error={error} required={required}>
      <input className={`input-field ${className}`} {...registration} {...props} />
    </FieldWrapper>
  )
}

export function FormSelect({ label, error, required, registration, options, className = '', ...props }: SelectProps) {
  return (
    <FieldWrapper label={label} error={error} required={required}>
      <select className={`input-field ${className}`} {...registration} {...props}>
        <option value="">Select...</option>
        {options.map((opt) => (
          <option key={opt.value} value={opt.value}>
            {opt.label}
          </option>
        ))}
      </select>
    </FieldWrapper>
  )
}

export function FormTextarea({ label, error, required, registration, className = '', ...props }: TextareaProps) {
  return (
    <FieldWrapper label={label} error={error} required={required}>
      <textarea className={`input-field min-h-[100px] resize-y ${className}`} {...registration} {...props} />
    </FieldWrapper>
  )
}
