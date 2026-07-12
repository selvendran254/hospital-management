import { advancedService } from '@/api/services/index.ts'
import { CreditCard, ShieldCheck, X } from 'lucide-react'
import { useEffect, useState } from 'react'

interface RazorpayCheckoutModalProps {
  isOpen: boolean
  amount: number
  itemLabel: string
  onClose: () => void
  onSuccess: (paymentId?: string) => void
}

export default function RazorpayCheckoutModal({
  isOpen,
  amount,
  itemLabel,
  onClose,
  onSuccess,
}: RazorpayCheckoutModalProps) {
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (!isOpen || document.getElementById('razorpay-checkout-script')) {
      return
    }

    const script = document.createElement('script')
    script.id = 'razorpay-checkout-script'
    script.src = 'https://checkout.razorpay.com/v1/checkout.js'
    script.async = true
    document.body.appendChild(script)
  }, [isOpen])

  async function handleCheckout() {
    setError(null)
    setIsLoading(true)
    try {
      if (!window.Razorpay) {
        throw new Error('Razorpay SDK did not load. Please retry in a moment.')
      }

      const order = await advancedService.createRazorpayOrder({
        amount: Math.round(amount * 100),
        currency: 'INR',
        receipt: `bill-${Date.now()}`,
        notes: {
          itemLabel,
        },
      })

      const checkout = new window.Razorpay({
        key: order.key || import.meta.env.VITE_RAZORPAY_KEY_ID,
        amount: order.amount,
        currency: order.currency,
        name: 'Hospital Management System',
        description: itemLabel,
        order_id: order.id,
        handler: (response) => {
          onSuccess(response.razorpay_payment_id)
        },
        prefill: {
          email: localStorage.getItem('userEmail') ?? '',
        },
        theme: {
          color: '#14b8a6',
        },
      })

      checkout.on('payment.failed', () => {
        setError('Payment failed. Please verify payment details and try again.')
      })
      checkout.open()
    } catch (checkoutError) {
      setError(checkoutError instanceof Error ? checkoutError.message : 'Unable to start checkout.')
    } finally {
      setIsLoading(false)
    }
  }

  if (!isOpen) {
    return null
  }

  return (
    <div className="fixed inset-0 z-[60] flex items-center justify-center bg-slate-900/60 px-4">
      <div className="w-full max-w-md rounded-2xl bg-white p-6 shadow-2xl dark:bg-slate-900">
        <div className="flex items-start justify-between">
          <div>
            <h2 className="text-lg font-semibold text-slate-900 dark:text-white">Razorpay Checkout</h2>
            <p className="mt-1 text-sm text-slate-500">Secure checkout for {itemLabel}</p>
          </div>
          <button type="button" onClick={onClose} className="rounded-lg p-1 hover:bg-slate-100 dark:hover:bg-slate-800">
            <X className="h-5 w-5" />
          </button>
        </div>

        <div className="mt-4 space-y-3 rounded-xl border border-slate-200 p-4 dark:border-slate-700">
          <div className="flex items-center justify-between text-sm">
            <span className="text-slate-500">Order amount</span>
            <span className="font-semibold text-slate-900 dark:text-white">INR {amount.toFixed(2)}</span>
          </div>
          <div className="flex items-center justify-between text-sm">
            <span className="text-slate-500">Gateway fee</span>
            <span className="font-semibold text-slate-900 dark:text-white">INR 0.00</span>
          </div>
        </div>

        <div className="mt-4 flex items-center gap-2 rounded-lg bg-emerald-50 px-3 py-2 text-xs text-emerald-700 dark:bg-emerald-900/30 dark:text-emerald-300">
          <ShieldCheck className="h-4 w-4" />
          Protected by Razorpay checkout encryption.
        </div>
        {error && (
          <p className="mt-3 rounded-lg bg-rose-50 px-3 py-2 text-xs text-rose-700 dark:bg-rose-900/30 dark:text-rose-300">
            {error}
          </p>
        )}

        <button
          type="button"
          onClick={handleCheckout}
          disabled={isLoading}
          className="btn-primary mt-5 flex w-full items-center gap-2"
        >
          <CreditCard className="h-4 w-4" />
          {isLoading ? 'Initializing checkout...' : 'Pay with Razorpay'}
        </button>
      </div>
    </div>
  )
}
