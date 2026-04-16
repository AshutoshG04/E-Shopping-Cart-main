import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

// Vite config with proxy to backend (useful for /api and /images)
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      // Proxy all API calls to the Spring Boot backend
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      // Proxy static images to backend so that "/images/..." resolves to Spring Boot static folder
      '/images': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        // no rewrite needed; keep same path
      }
    }
  }
});
