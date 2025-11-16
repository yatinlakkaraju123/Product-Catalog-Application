import { Box, Button, TextField, Typography } from "@mui/material"
import { useSignUpForm } from "../../hooks/Authentication/useSignUpForm"
import { useAuth } from "../../services/Auth"
import Loader from "../../utils/Loader"


const SignUp = () => {
    const {handleSubmit,handleChange,formData
    } = useSignUpForm()
        const {loading} = useAuth()
   if(loading) return <Loader/>
  return (
    <div>
          <Box display='flex' alignItems='center' justifyContent='center'  flexDirection='column' gap={2}>
        <Typography variant='h4'>SignUp</Typography>
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
              <TextField
            name="repeatPassword"
            label="Repeat Password"
            type="password"
            onChange={handleChange}
            value={formData.repeatPassword}
            
            />
             <TextField
            name="phoneNumber"
            label="Phone Number"
            type="number"
            onChange={handleChange}
            value={formData.phoneNumber}
            
            />
             <TextField
            name="email"
            label="Email"
            type="email"
            onChange={handleChange}
            value={formData.email}
            
            />
       
           
        <Button type='submit' variant='contained'>Submit</Button>
      </Box>
        
        </Box>
    </div>
  )
}

export default SignUp
