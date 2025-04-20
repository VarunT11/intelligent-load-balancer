import { useEffect, useState } from "react";
import {
  getAllSessions,
  getSessionById,
  MetricsSnapshot,
} from "../../api/metricsApi";
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
  HiOutlineDocumentText,
  HiOutlineTrash,
  HiOutlineEye,
  HiOutlineSwitchHorizontal,
  HiOutlineX,
} from "react-icons/hi";

export default function SavedSnapshots() {
  const [sessions, setSessions] = useState<MetricsSnapshot[]>([]);
  const [selected, setSelected] = useState<MetricsSnapshot | null>(null);
  const [chartView, setChartView] = useState(true);

  useEffect(() => {
    fetchSessions();
  }, []);

  const fetchSessions = async () => {
    try {
      const list = await getAllSessions();
      const detailed = await Promise.all(list.map((s) => getSessionById(s.id)));
      setSessions(detailed);
    } catch (err) {
      console.error("Failed to load sessions", err);
    }
  };

  const sum = (obj: Record<string, number>) =>
    Object.values(obj || {}).reduce((a, b) => a + b, 0);

  const handleDelete = async (id: number) => {
    if (!confirm("Are you sure you want to delete this snapshot?")) return;
    try {
      await instance.delete(`/metrics/sessions/${id}`);
      fetchSessions();
    } catch (err) {
      alert("Failed to delete");
      console.error(err);
    }
  };

  const formatTime = (ts: number | null) =>
    ts ? new Date(ts).toLocaleString() : "N/A";

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-5xl mx-auto">
      <h2 className="text-3xl font-bold mb-6 flex items-center gap-2">
        <HiOutlineDocumentText size={28} /> Saved Snapshots
      </h2>

      <table className="w-full border shadow-sm mb-8">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 border">Session</th>
            <th className="p-2 border">Start Time</th>
            <th className="p-2 border">End Time</th>
            <th className="p-2 border">Actions</th>
          </tr>
        </thead>
        <tbody>
          {sessions.map((s) => (
            <tr key={s.id} className="border-t hover:bg-gray-50">
              <td className="p-2 border font-semibold">{s.sessionName}</td>
              <td className="p-2 border">{formatTime(s.startTime)}</td>
              <td className="p-2 border">{formatTime(s.endTime)}</td>
              <td className="p-2 border space-x-3">
                <button
                  className="text-blue-600 hover:underline inline-flex items-center gap-1"
                  onClick={() => {
                    setSelected(s);
                    setChartView(true);
                  }}
                >
                  <HiOutlineEye /> View
                </button>
                <button
                  className="text-red-600 hover:underline inline-flex items-center gap-1"
                  onClick={() => handleDelete(s.id)}
                >
                  <HiOutlineTrash /> Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Modal */}
      {selected && (
        <div className="fixed inset-0 bg-black bg-opacity-50 z-40 flex items-center justify-center">
          <div className="bg-white rounded-lg p-6 max-w-3xl w-full relative shadow-lg z-50 overflow-y-auto max-h-[90vh]">
            <button
              className="absolute top-2 right-3 text-gray-500 hover:text-black text-lg"
              onClick={() => setSelected(null)}
            >
              <HiOutlineX />
            </button>

            <h3 className="text-2xl font-bold mb-4">
              Snapshot: {selected.sessionName}
            </h3>

            {/* Summary Cards */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
              <SummaryCard label="Total Requests" value={sum(selected.snapshot.totalRequests)} />
              <SummaryCard label="Successful" value={sum(selected.snapshot.successfulRequests)} color="green" />
              <SummaryCard label="Failed" value={sum(selected.snapshot.failedRequests)} color="red" />
            </div>

            {/* Success Rate Bar */}
            <SuccessRateBar snapshot={selected.snapshot} />

            {/* Toggle */}
            <div className="flex justify-end mb-4">
              <button
                onClick={() => setChartView(!chartView)}
                className="text-blue-600 hover:underline text-sm flex items-center gap-1"
              >
                <HiOutlineSwitchHorizontal /> Switch to {chartView ? "Table" : "Chart"} View
              </button>
            </div>

            {/* Backend Metrics View */}
            {chartView ? (
              <ResponsiveContainer width="100%" height={250}>
                <BarChart data={getChartData(selected.snapshot)}>
                  <XAxis dataKey="backend" />
                  <YAxis />
                  <Tooltip />
                  <Bar dataKey="successful" stackId="a" fill="#22c55e" />
                  <Bar dataKey="failed" stackId="a" fill="#ef4444" />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <table className="w-full border shadow-sm mb-4">
                <thead className="bg-gray-100">
                  <tr>
                    <th className="p-2 border">Backend</th>
                    <th className="p-2 border">Successful</th>
                    <th className="p-2 border">Failed</th>
                  </tr>
                </thead>
                <tbody>
                  {getChartData(selected.snapshot).map((row) => (
                    <tr key={row.backend} className="border-t">
                      <td className="p-2 border font-mono">{row.backend}</td>
                      <td className="p-2 border text-green-700">{row.successful}</td>
                      <td className="p-2 border text-red-600">{row.failed}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}

            {/* Canary Hits */}
            <h4 className="text-xl font-semibold mt-6 mb-2">Canary Route Hits</h4>
            <div className="w-full bg-gray-200 rounded-full h-4 mb-2 overflow-hidden">
              <div
                className="bg-yellow-400 h-4"
                style={{
                  width: `${
                    (sum(selected.snapshot.canaryHits) / sum(selected.snapshot.totalRequests || {})) * 100
                  }%`,
                }}
              />
            </div>
            <p className="text-sm text-yellow-800 font-medium mb-4">
              🟡 {sum(selected.snapshot.canaryHits)} Canary Hits out of {sum(selected.snapshot.totalRequests || {})} requests
            </p>
          </div>
        </div>
      )}
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

function SuccessRateBar({ snapshot }: { snapshot: MetricsSnapshot["snapshot"] }) {
  const total = Object.values(snapshot.totalRequests || {}).reduce((a, b) => a + b, 0);
  const success = Object.values(snapshot.successfulRequests || {}).reduce((a, b) => a + b, 0);
  const fail = Object.values(snapshot.failedRequests || {}).reduce((a, b) => a + b, 0);
  const percent = total > 0 ? ((success / total) * 100).toFixed(2) : "0.00";

  return (
    <div className="mb-6">
      <h4 className="text-lg font-semibold mb-2">Overall Success Rate</h4>
      {total === 0 ? (
        <>
          <div className="w-full bg-gray-300 h-4 rounded-full mb-2" />
          <p className="text-sm text-gray-500">No traffic recorded in this session</p>
        </>
      ) : (
        <>
          <div className="w-full bg-gray-200 rounded-full h-4 mb-2 flex overflow-hidden">
            <div className="bg-green-500 h-4" style={{ width: `${(success / total) * 100}%` }} />
            <div className="bg-red-500 h-4" style={{ width: `${(fail / total) * 100}%` }} />
          </div>
          <p className="text-sm text-green-800">
            ✅ {success} successful out of {total} requests ({percent}%)
          </p>
        </>
      )}
    </div>
  );
}

function getChartData(snapshot: MetricsSnapshot["snapshot"]) {
  const allBackends = new Set([
    ...Object.keys(snapshot.totalRequests || {}),
    ...Object.keys(snapshot.successfulRequests || {}),
    ...Object.keys(snapshot.failedRequests || {}),
  ]);

  return Array.from(allBackends).map((b) => ({
    backend: b,
    successful: snapshot.successfulRequests[b] || 0,
    failed: snapshot.failedRequests[b] || 0,
  }));
}
