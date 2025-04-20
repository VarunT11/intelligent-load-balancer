import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Sidebar from "./components/Sidebar";
import Dashboard from "./pages/Dashboard";
import Metrics from "./pages/Metrics";
import HealthDashboard from "./pages/HealthDashboard";
import ConfigDashboard from "./pages/ConfigDashboard";
import RouteList from "./pages/RouteList";
import RouteDetail from "./pages/RouteDetail";
import RouteFormPage from "./pages/RouteForm";
import LoginPage from "./pages/LoginPage";
import { AuthProvider, useAuth } from "./context/AuthContext";
import ProtectedRoute from "./components/ProtectedRoutes";

function AppLayout() {
  const { isAuthenticated } = useAuth();

  return (
    <div className="flex">
      {isAuthenticated && <Sidebar />}
      <div className="flex-1 p-6 bg-gray-100 min-h-screen">
      <Routes>
  {/* Public */}
  <Route path="/login" element={<LoginPage />} />

  {/* Protected */}
  <Route path="/" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
  <Route path="/routes" element={<ProtectedRoute><RouteList /></ProtectedRoute>} />
  <Route path="/routes/new" element={<ProtectedRoute><RouteFormPage /></ProtectedRoute>} />
  <Route path="/routes/:id/edit" element={<ProtectedRoute><RouteFormPage /></ProtectedRoute>} />
  <Route path="/routes/:id" element={<ProtectedRoute><RouteDetail /></ProtectedRoute>} />
  <Route path="/metrics" element={<ProtectedRoute><Metrics /></ProtectedRoute>} />
  <Route path="/health" element={<ProtectedRoute><HealthDashboard /></ProtectedRoute>} />
  <Route path="/config" element={<ProtectedRoute><ConfigDashboard /></ProtectedRoute>} />

  {/* Fallback */}
  <Route path="*" element={<Navigate to={isAuthenticated ? "/" : "/login"} replace />} />
</Routes>

      </div>
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <Router>
        <AppLayout />
      </Router>
    </AuthProvider>
  );
}
