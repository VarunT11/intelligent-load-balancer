import { ReactNode, useEffect, useState } from "react";
import { getAllRoutes } from "../api/routesApi";
import { getHealthStatus } from "../api/healthApi";
import { getMetrics } from "../api/metricsApi";
import { Link } from "react-router-dom";
import { HiOutlineCollection, HiOutlineChartBar, HiOutlineCog, HiOutlineHeart } from "react-icons/hi";


export default function Dashboard() {
  const [totalRoutes, setTotalRoutes] = useState(0);
  const [healthy, setHealthy] = useState(0);
  const [unhealthy, setUnhealthy] = useState(0);
  const [liveRequests, setLiveRequests] = useState(0);
  const [topCanaryRoute, setTopCanaryRoute] = useState<string | null>(null);

  useEffect(() => {
    fetchSummary();
  }, []);

  const fetchSummary = async () => {
    try {
      const routes = await getAllRoutes();
      setTotalRoutes(routes.length);

      const health = await getHealthStatus();
      setHealthy(health.healthyBackends.length);
      setUnhealthy(health.unhealthyBackends.length);

      const metrics = await getMetrics();
      const totalReq = Object.values(metrics.totalRequests || {}).reduce((a, b) => a + b, 0);
      setLiveRequests(totalReq);

      const canaryHits = metrics.canaryHits || {};
      const topRoute = Object.entries(canaryHits).sort((a, b) => b[1] - a[1])[0]?.[0];
      setTopCanaryRoute(topRoute || null);
    } catch (err) {
      console.error("Error loading dashboard stats:", err);
    }
  };

  const Card = ({
    label,
    value,
    color = "gray",
  }: {
    label: string;
    value: string | number;
    color?: "gray" | "green" | "red" | "blue";
  }) => {
    const bg = {
      gray: "bg-gray-100 text-gray-800",
      green: "bg-green-100 text-green-800",
      red: "bg-red-100 text-red-800",
      blue: "bg-blue-100 text-blue-800",
    }[color];
    return (
      <div className={`rounded shadow p-4 ${bg}`}>
        <p className="text-sm">{label}</p>
        <p className="text-2xl font-bold">{value}</p>
      </div>
    );
  };

  const NavCard = ({
    label,
    to,
    icon,
    color,
  }: {
    label: string;
    to: string;
    icon: ReactNode;
    color: string;
  }) => (
    <Link
      to={to}
      className={`p-4 rounded shadow flex items-center space-x-4 hover:bg-opacity-90 text-white ${color}`}
    >
      <span className="text-2xl">{icon}</span>
      <span className="font-semibold text-lg">{label}</span>
    </Link>
  );

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-6xl mx-auto space-y-10">
      <h2 className="text-3xl font-bold">Admin Dashboard</h2>

      {/* Summary Cards */}
      <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        <Card label="Total Routes" value={totalRoutes} color="blue" />
        <Card label="Healthy Backends" value={healthy} color="green" />
        <Card label="Unhealthy Backends" value={unhealthy} color="red" />
        <Card label="Live Requests" value={liveRequests} color="gray" />
      </div>

      {/* Canary Insight */}
      <div className="bg-yellow-100 text-yellow-800 rounded p-4 shadow text-sm">
        {topCanaryRoute ? (
          <p>
            🟡 Top Canary Route by traffic:{" "}
            <span className="font-mono font-semibold">{topCanaryRoute}</span>
          </p>
        ) : (
          <p className="italic">No canary traffic recorded yet.</p>
        )}
      </div>

      {/* Navigation Links */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mt-6">
  <NavCard to="/routes" label="Manage Routes" icon={<HiOutlineCollection size={24} />} color="bg-blue-600" />
  <NavCard to="/metrics" label="View Metrics" icon={<HiOutlineChartBar size={24} />} color="bg-green-600" />
  <NavCard to="/configs" label="Edit Configs" icon={<HiOutlineCog size={24} />} color="bg-yellow-500" />
  <NavCard to="/health" label="Health Status" icon={<HiOutlineHeart size={24} />} color="bg-red-600" />
</div>

    </div>
  );
}
