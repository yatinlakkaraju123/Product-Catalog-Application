
import { Box,Button,TextField,Typography } from '@mui/material'
import { useLoginForm } from '../../hooks/Authentication/useLoginForm';
import { Link } from 'react-router-dom';
import { useAuth } from '../../services/Auth';
import Loader from '../../utils/Loader';


const Login = () => {
    const { formData, handleChange, handleSubmit } = useLoginForm();
    const {loading} = useAuth()
   if(loading) return <Loader/>
  return (
    <div className=''>
      
    <Box display='flex' alignItems='center' justifyContent='center'flexDirection='column' gap={2}>
        <Typography variant='h4'>Login </Typography>
         <Box component="form" onSubmit={handleSubmit} display='flex' flexDirection='column' gap={2} sx={{

    p: { xs: 3, md: 10 },
  }} className='card'>
         <TextField
            name="username"
            label="User Name"
            type="text"
            onChange={handleChange}
            value={formData.username}
            
            />
             <TextField
            name="password"
            label="Password"
            type="password"
            onChange={handleChange}
            value={formData.password}
            
            />
            <Box>
Not Registered? <Link to="/signup">Register User</Link>
            </Box>
           
        <Button type='submit' variant='contained'>Submit</Button>
      </Box>
  
    </Box>
    </div>
  )
}

export default Login
