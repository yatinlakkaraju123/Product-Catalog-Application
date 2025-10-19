import { Box, Typography } from "@mui/material"
import { useSignUpForm } from "../../hooks/Authentication/useSignUpForm"
import { FormComponent } from "../../components/common/FormComponent"

const SignUp = () => {
    const {handleSubmit,handleChange,formData
    } = useSignUpForm()
  return (
    <div>
          <Box display='flex' alignItems='center' justifyContent='center'  flexDirection='column' gap={2}>
        <Typography variant='h4'>SignUp</Typography>
        <FormComponent
     handleSubmit={handleSubmit}
     handleChange={handleChange}
     formData={formData}
      fields={[
    { name: "username", label: "User Name",type:"text" },
   
    { name: "password", label: "Password", type: "password" },
     { name: "phoneNumber", label: "Phone Number", type: "text" },
      { name: "email", label: "Email", type: "email" },
  ]}
     
     />
        </Box>
    </div>
  )
}

export default SignUp
