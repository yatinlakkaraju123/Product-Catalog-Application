import { useEffect, useState } from "react";
import { useAuth } from "../../services/Auth";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";

interface LoginFormData {
  username: string;
  password: string;
}

export const useLoginForm = () => {
  const [formData, setFormData] = useState<LoginFormData>({ username: "", password: "" });
  const auth = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!auth.user) return;

    if (auth.user.role === "user") navigate("/");
    else navigate("/adminHome");
  }, [auth.user, navigate]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const response = await auth.login(formData);
    if (!response.success) toast.error(response.message);
  };

  return { formData, handleChange, handleSubmit };
};
