import { useEffect, useState } from "react";
import { getMetrics, saveMetricsSnapshot } from "../../api/metricsApi";
import instance from "../../api/axiosInstance";
import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import {
  HiOutlineChartBar,
  HiOutlineClock,
  HiOutlineArrowCircleDown,
  HiOutlineRefresh,
  HiLightningBolt
} from "react-icons/hi";

export default function LiveMetrics() {
  const [metrics, setMetrics] = useState<any>(null);
  const [sessionName, setSessionName] = useState("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [view, setView] = useState<"chart" | "table">("chart");

  useEffect(() => {
    fetchMetrics();
  }, []);

  const fetchMetrics = async () => {
    try {
      const data = await getMetrics();
      setMetrics(data);
    } catch (err) {
      console.error("Error fetching metrics:", err);
    }
  };

  const handleSave = async () => {
    if (!sessionName.trim()) {
      setError("Session name is required");
      return;
    }
    setError("");
    setSaving(true);
    try {
      await saveMetricsSnapshot(sessionName);
      alert("Snapshot saved successfully");
      setSessionName("");
    } catch (err) {
      alert("Failed to save snapshot");
      console.error(err);
    } finally {
      setSaving(false);
    }
  };

  const handleReset = async () => {
    if (!confirm("Are you sure you want to reset all live metrics?")) return;
    try {
      await instance.post("/metrics/reset");
      alert("Metrics reset successfully");
      fetchMetrics();
    } catch (err) {
      alert("Failed to reset metrics");
      console.error(err);
    }
  };

  const sum = (obj: Record<string, number>) =>
    Object.values(obj || {}).reduce((a, b) => a + b, 0);

  const formatDuration = (ms: number) => {
    const totalSeconds = Math.floor(ms / 1000);
    const h = Math.floor(totalSeconds / 3600);
    const m = Math.floor((totalSeconds % 3600) / 60);
    const s = totalSeconds % 60;
    return `${h}h ${m}m ${s}s`;
  };

  if (!metrics) return <p>Loading...</p>;

  const total = sum(metrics.totalRequests);
  const success = sum(metrics.successfulRequests);
  const failed = sum(metrics.failedRequests);
  const canary = sum(metrics.canaryHits);
  const successRate =
    total > 0 ? ((success / total) * 100).toFixed(2) : "0.00";
  const elapsed = metrics.liveMetricsStartTime
    ? formatDuration(Date.now() - metrics.liveMetricsStartTime)
    : null;

  const backends = Array.from(
    new Set([
      ...Object.keys(metrics.totalRequests || {}),
      ...Object.keys(metrics.successfulRequests || {}),
      ...Object.keys(metrics.failedRequests || {}),
    ])
  );

  const chartData = backends.map((b) => ({
    backend: b,
    successful: metrics.successfulRequests?.[b] || 0,
    failed: metrics.failedRequests?.[b] || 0,
  }));

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-5xl mx-auto space-y-10">
      <div className="flex justify-between items-start">
        <div>
          <h2 className="text-3xl font-bold mb-2 flex items-center gap-2">
            <HiLightningBolt className="text-blue-600" /> Live Metrics
          </h2>
          <p className="text-gray-600 text-sm flex items-center gap-2">
            <HiOutlineClock />
            <strong>Start:</strong>{" "}
            {metrics.liveMetricsStartTime
              ? new Date(metrics.liveMetricsStartTime).toLocaleString()
              : "N/A"}
            <span>•</span>
            <strong>Elapsed:</strong> {elapsed ?? "N/A"}
          </p>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid grid-cols-2 md:grid-cols-3 gap-4">
        <SummaryCard label="Total Requests" value={total} />
        <SummaryCard label="Successful" value={success} color="green" />
        <SummaryCard label="Failed" value={failed} color="red" />
      </div>

      {/* Success % Progress Bar */}
      <div>
        <h3 className="text-xl font-semibold mb-2 flex items-center gap-2">
          <HiOutlineChartBar />
          Overall Success Rate
        </h3>
        {total === 0 ? (
          <>
            <div className="w-full bg-gray-300 h-4 rounded-full mb-2" />
            <p className="text-sm text-gray-500">No traffic recorded yet</p>
          </>
        ) : (
          <>
            <div className="w-full bg-gray-200 rounded-full h-4 mb-2 flex overflow-hidden">
              <div
                className="bg-green-500 h-4"
                style={{
                  width: `${((success / total) * 100).toFixed(2)}%`,
                }}
              ></div>
              <div
                className="bg-red-500 h-4"
                style={{
                  width: `${((failed / total) * 100).toFixed(2)}%`,
                }}
              ></div>
            </div>
            <p className="text-sm text-green-800">
              ✅ {success} Successful out of {total} requests ({successRate}%)
            </p>
          </>
        )}
      </div>

      {/* Chart/Table Toggle */}
      <div>
        <div className="flex items-center justify-between mb-4">
          <h3 className="text-xl font-semibold">Backend Metrics</h3>
          <div className="space-x-2">
            <button
              onClick={() => setView("chart")}
              className={`px-4 py-1 rounded ${
                view === "chart" ? "bg-blue-600 text-white" : "bg-gray-200"
              }`}
            >
              Chart
            </button>
            <button
              onClick={() => setView("table")}
              className={`px-4 py-1 rounded ${
                view === "table" ? "bg-blue-600 text-white" : "bg-gray-200"
              }`}
            >
              Table
            </button>
          </div>
        </div>

        {view === "chart" ? (
          <div className="bg-gray-50 rounded shadow p-4">
            <ResponsiveContainer width="100%" height={280}>
              <BarChart data={chartData}>
                <XAxis dataKey="backend" />
                <YAxis />
                <Tooltip />
                <Bar dataKey="successful" stackId="a" fill="#4ade80" />
                <Bar dataKey="failed" stackId="a" fill="#f87171" />
              </BarChart>
            </ResponsiveContainer>
          </div>
        ) : (
          <table className="w-full border shadow-sm">
            <thead className="bg-gray-100">
              <tr>
                <th className="p-2 border">Backend</th>
                <th className="p-2 border">Total</th>
                <th className="p-2 border">Successful</th>
                <th className="p-2 border">Failed</th>
              </tr>
            </thead>
            <tbody>
              {backends.map((b) => (
                <tr key={b} className="border-t">
                  <td className="p-2 border font-mono">{b}</td>
                  <td className="p-2 border">{metrics.totalRequests?.[b] || 0}</td>
                  <td className="p-2 border text-green-700">
                    {metrics.successfulRequests?.[b] || 0}
                  </td>
                  <td className="p-2 border text-red-600">
                    {metrics.failedRequests?.[b] || 0}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Canary Hits */}
      <div>
        <h3 className="text-xl font-semibold mb-2">Canary Route Hits</h3>
        <div className="w-full bg-gray-200 rounded-full h-4 mb-2">
          <div
            className="bg-yellow-400 h-4 rounded-full"
            style={{ width: `${((canary / total) * 100).toFixed(2)}%` }}
          ></div>
        </div>
        <p className="text-sm text-yellow-800 mb-2">
          🟡 {canary} Canary Hits out of {total} requests (
          {((canary / total) * 100).toFixed(2)}%)
        </p>

        {Object.keys(metrics.canaryHits || {}).length === 0 ? (
          <p className="text-sm italic text-gray-500">No canary traffic recorded.</p>
        ) : (
          <table className="w-full border shadow-sm">
            <thead className="bg-gray-100">
              <tr>
                <th className="p-2 border">Route</th>
                <th className="p-2 border">Hits</th>
              </tr>
            </thead>
            <tbody>
            {Object.entries(metrics.canaryHits as Record<string, number>).map(([route, count]) => (
              <tr key={route} className="border-t">
                <td className="p-2 border font-mono">{route}</td>
                <td className="p-2 border">{count}</td>
              </tr>
            ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Snapshot Save */}
      <div className="flex flex-wrap items-center justify-between gap-4">
        <div className="flex flex-col">
          <input
            type="text"
            placeholder="Session Name"
            value={sessionName}
            onChange={(e) => setSessionName(e.target.value)}
            className={`border px-4 py-2 rounded w-64 ${
              error ? "border-red-500" : ""
            }`}
          />
          {error && <span className="text-sm text-red-500 mt-1">{error}</span>}
        </div>
        <div className="flex gap-4">
          <button
            onClick={handleSave}
            disabled={saving}
            className="flex items-center gap-2 bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 disabled:opacity-50"
          >
            <HiOutlineArrowCircleDown />
            Save Snapshot
          </button>
          <button
            onClick={handleReset}
            className="flex items-center gap-2 bg-gray-600 text-white px-4 py-2 rounded hover:bg-gray-700"
          >
            <HiOutlineRefresh />
            Reset Metrics
          </button>
        </div>
      </div>
    </div>
  );
}

function SummaryCard({
  label,
  value,
  color = "gray",
}: {
  label: string;
  value: number;
  color?: string;
}) {
  const bg = {
    gray: "bg-gray-100 text-gray-800",
    green: "bg-green-100 text-green-800",
    red: "bg-red-100 text-red-800",
  }[color];

  return (
    <div className={`rounded shadow p-4 ${bg}`}>
      <p className="text-sm text-gray-500">{label}</p>
      <p className="text-2xl font-semibold">{value}</p>
    </div>
  );
}
