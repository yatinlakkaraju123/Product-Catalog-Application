import React, { createContext, useContext, useState, useEffect } from "react";
import ApiService from "../services/api/ApiClient";
import {jwtDecode} from "jwt-decode";

interface User {
  username: string;
  token: string;
  role: string;
}
interface CustomJwtPayload {
  sub: string;        // or username/email depending on what you put in 'sub'
  roles: string[];
  exp: number;
  iat?: number;
}
interface AuthContextType {
  user: User | null;
  login: (credentials: { username: string; password: string }) => Promise<{ success: boolean; message?: string }>;
  logout: () => void;
  loading: boolean;
}

interface AuthProviderProps {
  children: React.ReactNode;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    const role = localStorage.getItem("role") as "user" | "admin" | null;
    const username = localStorage.getItem("username");

    if (token && role && username) {
      setUser({ username, token, role });
    }
    setLoading(false);
  }, []);

  const login = async (credentials: { username: string; password: string }) => {
    try {
      const response = await ApiService.login(credentials.username, credentials.password);
      console.log("response data:",response.data)
      const { accessToken, token } = response.data;
      const decodedToken:CustomJwtPayload = jwtDecode(accessToken);
      const rolesArray = decodedToken.roles;
      let role;
      if(rolesArray.includes('ROLE_ADMIN')){
          role = "admin"
      }
      else{
        role = "user"
      }
      console.log("Decoded token:",decodedToken)
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", token);
      localStorage.setItem("role", role);
      localStorage.setItem("username", credentials.username);

      setUser({ username:credentials.username, token: accessToken, role:role });
      return { success: true };
    } catch (error: any) {
      return { success: false, message: error.response?.data?.message || "Login failed" };
    }
  };

  const logout = () => {
    ApiService.logout();
    setUser(null);
    localStorage.removeItem("role");
    localStorage.removeItem("username");
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  };

  return <AuthContext.Provider value={{ user, login, logout, loading }}>{children}</AuthContext.Provider>;
};
