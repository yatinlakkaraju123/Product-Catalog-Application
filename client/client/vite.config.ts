import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'


export default defineConfig({
  plugins: [react()],
 build: {
    minify: "terser",
    terserOptions: {
      compress: {
        drop_console: true,  // 🔥 removes all console.* calls
        drop_debugger: true  // 🔥 removes `debugger;`
      },
    },
  },
})
