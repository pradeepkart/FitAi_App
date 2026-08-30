import { Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
export function ProtectedRoute({ children }) {
  return useAuth().isAuthenticated ? (
    children
  ) : (
    <Navigate to="/login" replace />
  );
}
export function AdminRoute({ children }) {
  return useAuth().user?.role === "ADMIN" ? (
    children
  ) : (
    <Navigate to="/dashboard" replace />
  );
}
