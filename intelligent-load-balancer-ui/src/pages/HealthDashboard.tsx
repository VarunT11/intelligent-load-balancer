import { useEffect, useState } from "react";
import {
  getHealthStatus,
  refreshHealthStatus,
  openCircuit,
  closeCircuit,
  BackendStatus,
} from "../api/healthApi";
import {
  HiOutlineHeart,
  HiOutlineRefresh,
  HiOutlineX,
} from "react-icons/hi";

import { FaTools } from "react-icons/fa";

export default function HealthDashboard() {
  const [health, setHealth] = useState<BackendStatus[]>([]);
  const [lastCheckedAt, setLastCheckedAt] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState<BackendStatus | null>(null);
  const [overrideReason, setOverrideReason] = useState("");
  const [checking, setChecking] = useState(false);

  useEffect(() => {
    fetchHealth();
  }, []);

  const fetchHealth = async () => {
    setLoading(true);
    try {
      const data = await getHealthStatus();
      setLastCheckedAt(data.lastCheckedAt);
      setHealth([...data.healthyBackends, ...data.unhealthyBackends]);
    } catch (err) {
      console.error("Failed to fetch health status", err);
    } finally {
      setLoading(false);
    }
  };

  const handleForceCheck = async () => {
    setChecking(true);
    try {
      const data = await refreshHealthStatus();
      setLastCheckedAt(data.lastCheckedAt);
      setHealth([...data.healthyBackends, ...data.unhealthyBackends]);
      alert("Health status refreshed successfully.");
    } catch (err) {
      alert("Health check failed.");
      console.error(err);
    } finally {
      setChecking(false);
    }
  };

  const handleSubmitOverride = async () => {
    if (!selected) return;
    try {
      const apiCall =
        selected.circuitState === "CLOSED" ? openCircuit : closeCircuit;
      await apiCall(selected.url, overrideReason.trim());
      alert("Circuit state updated successfully");
      setSelected(null);
      setOverrideReason("");
      fetchHealth();
    } catch (err) {
      alert("Failed to override circuit");
      console.error(err);
    }
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-5xl mx-auto space-y-10">
      <div className="flex justify-between items-center mb-6">
        <div className="flex items-center gap-2 text-red-600">
          <HiOutlineHeart className="text-3xl" />
          <h2 className="text-3xl font-bold text-gray-900">Health Dashboard</h2>
        </div>
        <button
          className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 flex items-center gap-2 disabled:opacity-50"
          onClick={handleForceCheck}
          disabled={checking}
        >
          <HiOutlineRefresh className="text-lg" />
          {checking ? "Refreshing..." : "Force Refresh"}
        </button>
      </div>

      <p className="text-sm text-gray-600 -mt-6">
        Last Checked:{" "}
        {lastCheckedAt ? new Date(lastCheckedAt).toLocaleString() : "N/A"}
      </p>

      <table className="w-full border shadow-sm">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 border text-left">Backend URL</th>
            <th className="p-2 border text-left">Health Status</th>
            <th className="p-2 border text-left">Circuit</th>
            <th className="p-2 border text-left">Reason</th>
            <th className="p-2 border text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
          {health.map((b) => (
            <tr key={b.url} className="border-t hover:bg-gray-50">
              <td className="p-2 border font-mono">{b.url}</td>
              <td
                className={`p-2 border font-semibold ${
                  b.status === "UNHEALTHY"
                    ? "text-red-600"
                    : "text-green-700"
                }`}
              >
                {b.status}
              </td>
              <td
                className={`p-2 border font-semibold ${
                  b.circuitState === "OPEN"
                    ? "text-red-600"
                    : "text-green-700"
                }`}
              >
                {b.circuitState}
              </td>
              <td className="p-2 border text-sm text-gray-600">
                {b.reason || "-"}
              </td>
              <td className="p-2 border text-center">
                <button
                  className="text-blue-600 hover:underline flex items-center justify-center gap-1 mx-auto"
                  onClick={() => setSelected(b)}
                >
                  <FaTools />
                  Override
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Override Modal */}
      {selected && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md relative">
            <button
              className="absolute top-3 right-3 text-gray-500 hover:text-black"
              onClick={() => setSelected(null)}
            >
              <HiOutlineX size={20} />
            </button>
            <h3 className="text-xl font-bold mb-4 flex items-center gap-2">
              <FaTools className="text-blue-600" />
              Override Circuit
            </h3>
            <p className="text-sm text-gray-600 mb-1">
              <strong>Backend:</strong> {selected.url}
            </p>
            <p className="text-sm text-gray-600 mb-4">
              <strong>Current State:</strong> {selected.circuitState}
            </p>
            <input
              type="text"
              value={overrideReason}
              onChange={(e) => setOverrideReason(e.target.value)}
              placeholder="Enter reason for override"
              className="border px-4 py-2 rounded w-full mb-4"
            />
            <div className="flex justify-end gap-4">
              <button
                onClick={() => setSelected(null)}
                className="text-gray-500 hover:text-black"
              >
                Cancel
              </button>
              <button
                onClick={handleSubmitOverride}
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
              >
                Submit
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
