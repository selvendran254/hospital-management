# Frontend Deployment Guide

## Prerequisites
- Node.js 20+
- npm 10+
- Backend API reachable from deployed frontend

## Environment Variables
Create `.env.production` with:

```bash
VITE_API_URL=https://your-api-domain/api
VITE_RAZORPAY_KEY_ID=rzp_live_xxxxxxxxxxxx
```

## Build
```bash
npm install
npm run build
```

Artifacts are generated in `dist/`.

## Deploy Options
- Vercel: import repo and set environment variables in project settings.
- Netlify: publish directory `dist` and configure environment variables.
- Nginx/Apache: serve static `dist` files and route all paths to `index.html`.

## Health Checklist
- Login and role-based dashboard routes work.
- Razorpay checkout script loads and creates an order via API.
- Recharts pages render without console errors.
- Jitsi iframe loads for telemedicine video call pages.
