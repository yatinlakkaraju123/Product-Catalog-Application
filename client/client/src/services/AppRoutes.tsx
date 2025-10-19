import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import { AuthProvider, useAuth } from "./Auth";
import Loader from "../utils/Loader";

// Pages
import Home from "../pages/User/Home";
import Login from "../pages/Authentication/Login";
import SignUp from "../pages/Authentication/SignUp";
import AdminHome from "../pages/Admin/AdminHome";
import ResponsiveAppBar from "../pages/Admin/ResponsiveAppBar";
import ProductManager from "../pages/Admin/ProductManager";
import CategoryManager from "../pages/Admin/CategoryManager";
import UserNavBar from "../pages/User/ResponsiveAppBar";
import UserProductManager from "../pages/User/UserProductManager";
import NotAuthorizedErrorForUsersPages from "../pages/error/NotAuthorizedForUsers";
import NotAuthorizedErrorForAdminPages from "../pages/error/NotAuthorizedErrorForAdminPages";

interface RouteWrapperProps {
  children: React.ReactNode;
}

const AuthenticatedUserRoute: React.FC<RouteWrapperProps> = ({ children }) => {
  const auth = useAuth();
  if (auth.loading) return <Loader />;
  if (!auth.user) return <Navigate to="/login" />;
  if (auth.user.role !== "user") return <Navigate to="/notAuthorizedForUser" />;
  return <>{children}</>;
};

const AuthenticatedAdminRoute: React.FC<RouteWrapperProps> = ({ children }) => {
  const auth = useAuth();
  if (auth.loading) return <Loader />;
  if (!auth.user) return <Navigate to="/login" />;
  if (auth.user.role !== "admin") return <Navigate to="/notAuthorizedForAdmin" />;
  return <>{children}</>;
};

const AppRoutes: React.FC = () => (
  <AuthProvider>
    <Routes>
      {/* Admin Routes */}
      <Route
        path="/adminHome"
        element={
          <AuthenticatedAdminRoute>
            <ResponsiveAppBar />
            <AdminHome />
          </AuthenticatedAdminRoute>
        }
      />
      <Route
        path="/admin/productManager"
        element={
          <AuthenticatedAdminRoute>
            <ResponsiveAppBar />
            <ProductManager />
          </AuthenticatedAdminRoute>
        }
      />
      <Route
        path="/admin/categoryManager"
        element={
          <AuthenticatedAdminRoute>
            <ResponsiveAppBar />
            <CategoryManager />
          </AuthenticatedAdminRoute>
        }
      />

      {/* User Routes */}
      <Route
        path="/"
        element={
          <AuthenticatedUserRoute>
            <UserNavBar />
            <Home />
          </AuthenticatedUserRoute>
        }
      />
      <Route
        path="/user/products"
        element={
          <AuthenticatedUserRoute>
            <UserNavBar />
            <UserProductManager />
          </AuthenticatedUserRoute>
        }
      />

      {/* Public Routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<SignUp />} />

      {/* Error Routes */}
      <Route path="/notAuthorizedForUser" element={<NotAuthorizedErrorForUsersPages />} />
      <Route path="/notAuthorizedForAdmin" element={<NotAuthorizedErrorForAdminPages />} />
    </Routes>
    <ToastContainer />
  </AuthProvider>
);

export default AppRoutes;
