import { useEffect, useState } from "react";
import type { signUpPage } from "../../types/types";
import { useAuth } from "../../services/Auth";
import { useNavigate } from "react-router-dom";
import ToastComponent from "../../components/common/ToastComponent";
import { toast } from "react-toastify";

export const useSignUpForm = () => {
  const [formData, setFormData] = useState<signUpPage>({ username: "", password: "",phoneNumber:"",email:"" });
  const {signUp,role} = useAuth()
    const [isAuthenticated,setIsAuthenticated] = useState(false)

   const navigate = useNavigate()
 useEffect(() => {
  if (!isAuthenticated || !role) return;

  if (role === "user") navigate("/");
  else navigate("/adminHome");
}, [role, isAuthenticated, navigate]);

   
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if(await signUp(formData.username,formData.password,formData.phoneNumber,formData.email)){
         setIsAuthenticated(true)
    }
    } catch (error) {
     toast.error(error.response.data.title)
    }
    
  };

  return { formData, handleChange, handleSubmit };
};
