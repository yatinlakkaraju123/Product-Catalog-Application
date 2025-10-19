import {Box, ThemeProvider }from '@mui/material'
import { theme } from './themes/EcommerceTheme'
import "./assets/styles/global.scss";
import AppRoutes from './services/AppRoutes';
import './App.css'
import { ToastContainer } from 'react-toastify';
function App() {


  return (
   <>
   <Box p={2}>
      <ThemeProvider theme={theme}>
        <ToastContainer/>
        <AppRoutes/>
      </ThemeProvider>
   </Box>
   </>
  )
}

export default App
