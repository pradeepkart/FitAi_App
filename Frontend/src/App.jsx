import { Routes, Route } from "react-router-dom";
import Layout from "./components/Layout";
import { ProtectedRoute, AdminRoute } from "./components/ProtectedRoute";
import { LoginPage, RegisterPage } from "./pages/AuthPages";
import DashboardPage from "./pages/DashboardPage";
import TrackerPage from "./pages/TrackerPage";
import {
  HomePage,
  ExercisePage,
  AIRecommendationPage,
  ProfilePage,
  AdminDashboardPage,
  NotFoundPage,
} from "./pages/OtherPages";
export default function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route
        element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }
      >
        <Route path="/dashboard" element={<DashboardPage />} />
        <Route path="/workouts" element={<TrackerPage type="workouts" />} />
        <Route path="/workouts/new" element={<TrackerPage type="workouts" />} />
        <Route path="/exercises" element={<ExercisePage />} />
        <Route path="/weight" element={<TrackerPage type="weight" />} />
        <Route path="/water" element={<TrackerPage type="water" />} />
        <Route path="/calories" element={<TrackerPage type="calories" />} />
        <Route path="/goals" element={<TrackerPage type="goals" />} />
        <Route path="/ai" element={<AIRecommendationPage />} />
        <Route path="/profile" element={<ProfilePage />} />
        <Route
          path="/admin"
          element={
            <AdminRoute>
              <AdminDashboardPage />
            </AdminRoute>
          }
        />
      </Route>
      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
}
