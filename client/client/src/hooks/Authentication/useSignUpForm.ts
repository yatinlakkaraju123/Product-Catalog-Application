import { useEffect, useState } from "react";
import type { signUpPage } from "../../types/types";
import { useAuth } from "../../services/Auth";
import { useNavigate } from "react-router-dom";

import { toast } from "react-toastify";

export const useSignUpForm = () => {
  const [formData, setFormData] = useState<signUpPage>({ username: "", password: "",repeatPassword:"",phoneNumber:"",email:"" });
  const auth = useAuth()
    const [isAuthenticated,setIsAuthenticated] = useState(false)

   const navigate = useNavigate()
 useEffect(() => {
    if (!auth.user) return;

    if (auth.user.role === "user") navigate("/");
    else navigate("/adminHome");
  }, [auth.user, navigate]);

   
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const regExp = /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])\S{8,20}$/;
      if(formData.password===formData.repeatPassword){
          if(regExp.test(formData.password)){
               if(await auth.signUp(formData)){
         setIsAuthenticated(true)
    }
          }
          else{
            toast.error("The password do not meet the required complexity")
          }
      }
      else{
        toast.error("The Passwords do not match")
      }
     
    } catch (error) {
     toast.error(error.response.data.title)
    }
    
  };

  return { formData, handleChange, handleSubmit };
};
