import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'

const resources = {
  en: {
    translation: {
      brand: 'MediCare',
      nav: {
        home: 'Home',
        about: 'About',
        departments: 'Departments',
        doctors: 'Doctors',
        services: 'Services',
        appointment: 'Book Appointment',
        more: 'More',
        dashboard: 'Dashboard',
        login: 'Login',
        register: 'Register',
        logout: 'Logout',
      },
    },
  },
  ta: {
    translation: {
      brand: 'மெடிகேர்',
      nav: {
        home: 'முகப்பு',
        about: 'எங்களைப் பற்றி',
        departments: 'துறைகள்',
        doctors: 'மருத்துவர்கள்',
        services: 'சேவைகள்',
        appointment: 'நியமனம் பதிவு',
        more: 'மேலும்',
        dashboard: 'டாஷ்போர்டு',
        login: 'உள்நுழை',
        register: 'பதிவு செய்',
        logout: 'வெளியேறு',
      },
    },
  },
}

const savedLanguage = localStorage.getItem('language')

i18n.use(initReactI18next).init({
  resources,
  lng: savedLanguage ?? 'en',
  fallbackLng: 'en',
  interpolation: {
    escapeValue: false,
  },
})

i18n.on('languageChanged', (language) => {
  localStorage.setItem('language', language)
})

export default i18n
