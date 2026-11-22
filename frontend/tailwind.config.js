/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        retail: {
          500: '#3b82f6',
          600: '#2563eb',
        }
      }
    },
  },
  plugins: [],
}
