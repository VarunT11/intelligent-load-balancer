import { NavLink, useNavigate } from "react-router-dom";
import {
  HiOutlineViewGrid,
  HiOutlineCog,
  HiOutlineHeart,
  HiOutlineChartBar,
  HiOutlineCollection,
  HiOutlineLogout,
} from "react-icons/hi";
import { useAuth } from "../context/AuthContext";

export default function Sidebar() {
  const { logout } = useAuth();
  const navigate = useNavigate();

  const navItems = [
    { to: "/", label: "Dashboard", icon: <HiOutlineViewGrid size={20} /> },
    { to: "/routes", label: "Routes", icon: <HiOutlineCollection size={20} /> },
    { to: "/metrics", label: "Metrics", icon: <HiOutlineChartBar size={20} /> },
    { to: "/health", label: "Health", icon: <HiOutlineHeart size={20} /> },
    { to: "/configs", label: "Config", icon: <HiOutlineCog size={20} /> },
  ];

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="w-64 h-screen bg-gray-900 text-white flex flex-col p-6 space-y-6 justify-between">
      <div>
        <h1 className="text-2xl font-bold text-white tracking-wide mb-6">Admin Panel</h1>
        <nav className="space-y-2">
          {navItems.map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) =>
                `flex items-center px-4 py-2 rounded transition-colors duration-200 ${
                  isActive
                    ? "bg-blue-600 text-white"
                    : "hover:bg-gray-800 text-gray-300"
                }`
              }
            >
              <span className="mr-3">{item.icon}</span>
              <span className="font-medium">{item.label}</span>
            </NavLink>
          ))}
        </nav>
      </div>

      {/* Logout Button */}
      <button
        onClick={handleLogout}
        className="flex items-center px-4 py-2 rounded hover:bg-gray-800 text-gray-300 transition-colors duration-200"
      >
        <HiOutlineLogout size={20} className="mr-3" />
        <span className="font-medium">Logout</span>
      </button>
    </div>
  );
}
