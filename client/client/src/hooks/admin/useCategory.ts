import { useState } from "react";
import type { addCategoryPage } from "../../types/types";
import { addCategory } from "../../services/api/CategoryApiService";
import { toast } from "react-toastify";
export const useCategory = () =>{
const [formData, setFormData] = useState<addCategoryPage>({ name:""});
 const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
     console.log(formData);
     try {
      await addCategory(formData.name)
     } catch (error) {
      toast.error(error.response.data.title)
     }
     
    
   
  };
  return { formData, handleChange, handleSubmit };
}
