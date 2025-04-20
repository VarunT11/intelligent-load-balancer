import { useEffect, useState } from "react";
import { useNavigate, useParams, Link } from "react-router-dom";
import { getAllRoutes, deleteRoute } from "../api/routesApi";
import { RouteDefinition } from "../types/RouteDefinition";
import { getHealthStatus } from "../api/healthApi";
import {
  HiOutlinePencilAlt,
  HiOutlineTrash,
} from "react-icons/hi";

export default function RouteDetail() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [route, setRoute] = useState<RouteDefinition | null>(null);
  const [loading, setLoading] = useState(true);
  const [healthMap, setHealthMap] = useState<Record<string, string>>({});
  const [circuitMap, setCircuitMap] = useState<Record<string, string>>({});

  useEffect(() => {
    fetchRoute();
    fetchHealth();
  }, [id]);

  const fetchRoute = async () => {
    const all = await getAllRoutes();
    const found = all.find((r) => r.id === Number(id));
    setRoute(found || null);
    setLoading(false);
  };

  const fetchHealth = async () => {
    const health = await getHealthStatus();

    const healthStatus: Record<string, string> = {};
    const circuitStatus: Record<string, string> = {};

    [...health.healthyBackends, ...health.unhealthyBackends].forEach((b) => {
      healthStatus[b.url] = b.status;
      circuitStatus[b.url] = b.circuitState;
    });

    setHealthMap(healthStatus);
    setCircuitMap(circuitStatus);
  };

  const handleDelete = async () => {
    if (window.confirm("Are you sure you want to delete this route?")) {
      await deleteRoute(Number(id));
      navigate("/routes");
    }
  };

  const strategyMap: Record<string, string> = {
    ROUND_ROBIN: "Round Robin",
    RANDOM: "Random",
    LEAST_CONNECTIONS: "Least Connections",
    WEIGHTED_ROUND_ROBIN: "Weighted Round Robin",
  };

  const renderBackendTable = (backends: { id: string; url: string }[]) => (
    <table className="w-full border mb-8">
      <thead className="bg-gray-100">
        <tr>
          <th className="text-left p-2 border">ID</th>
          <th className="text-left p-2 border">URL</th>
          <th className="text-left p-2 border">Health</th>
          <th className="text-left p-2 border">Circuit</th>
        </tr>
      </thead>
      <tbody>
        {backends.map((b) => (
          <tr key={b.id} className="border-t">
            <td className="p-2 border font-mono">{b.id}</td>
            <td className="p-2 border text-sm">{b.url}</td>
            <td
              className={`p-2 border font-semibold ${
                healthMap[b.url] === "UNHEALTHY"
                  ? "text-red-600"
                  : "text-green-700"
              }`}
            >
              {healthMap[b.url] || "Unknown"}
            </td>
            <td className="p-2 border">{circuitMap[b.url] || "-"}</td>
          </tr>
        ))}
      </tbody>
    </table>
  );

  if (loading) return <p>Loading...</p>;
  if (!route) return <p className="text-red-600">Route not found.</p>;

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-4xl mx-auto">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-3xl font-bold">Route: {route.routePath}</h2>
        <div className="space-x-3 flex">
          <Link
            to={`/routes/${route.id}/edit`}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 flex items-center gap-2"
          >
            <HiOutlinePencilAlt size={18} />
            Edit
          </Link>
          <button
            className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700 flex items-center gap-2"
            onClick={handleDelete}
          >
            <HiOutlineTrash size={18} />
            Delete
          </button>
        </div>
      </div>

      <p className="mb-4 text-lg">
        <strong>Strategy:</strong>{" "}
        {strategyMap[route.strategyType] || route.strategyType}
      </p>

      <div className="mb-8">
        <h3 className="text-xl font-semibold mb-2">Primary Backends</h3>
        {renderBackendTable(route.primaryBackends)}
      </div>

      <div className="mb-8">
        <h3 className="text-xl font-semibold mb-2">Canary Traffic</h3>
        <div className="w-full bg-gray-200 rounded-full h-4 mb-1">
          <div
            className="bg-yellow-400 h-4 rounded-full"
            style={{ width: `${route.canaryTrafficPercent}%` }}
          ></div>
        </div>
        <p className="text-sm text-gray-600">
          {route.canaryTrafficPercent}% traffic routed to canaries
        </p>
      </div>

      <div>
        <h3 className="text-xl font-semibold mb-2">Canary Backends</h3>
        {route.canaryBackends.length === 0 ? (
          <p className="text-sm italic text-gray-500">
            No canary backends defined.
          </p>
        ) : (
          renderBackendTable(route.canaryBackends)
        )}
      </div>
    </div>
  );
}
