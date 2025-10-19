
import { Box,Typography } from '@mui/material'
import { useLoginForm } from '../../hooks/Authentication/useLoginForm';
import {FormComponent} from '../../components/common/FormComponent';

const Login = () => {
    const { formData, handleChange, handleSubmit } = useLoginForm();

   
  return (
    <div className=''>
        
    <Box display='flex' alignItems='center' justifyContent='center'flexDirection='column' gap={2}>
        <Typography variant='h4'>Login </Typography>
      
     <FormComponent
     handleSubmit={handleSubmit}
     handleChange={handleChange}
     formData={formData}
      fields={[
    { name: "username", label: "User Name",type:"text" },
   
    { name: "password", label: "Password", type: "password" },
  ]}
     
     />
    </Box>
    </div>
  )
}

export default Login
