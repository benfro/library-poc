import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'
import * as path from "path";

// https://vitejs.dev/config/
export default defineConfig({
  resolve: {
    alias: {
      '~bootstrap': path.resolve(__dirname, 'node_modules/bootstrap'),
      root: path.resolve(__dirname, 'src'),
    }
  },
  plugins: [react()],
  // root: path.resolve(__dirname, 'src'),
  // server: {
  //   port: 8080,
  //   hot: true
  // }

})
