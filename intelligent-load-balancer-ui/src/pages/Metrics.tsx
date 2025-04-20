import { useState } from "react";
import { HiOutlineLightningBolt, HiOutlineClipboardList } from "react-icons/hi";
import LiveMetrics from "../components/metrics/LiveMetrics";
import SavedSnapshots from "../components/metrics/SavedSnapshots";

export default function MetricsDashboard() {
  const [activeTab, setActiveTab] = useState<"live" | "saved">("live");

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-6xl mx-auto">
      <h2 className="text-3xl font-bold mb-6">Metrics Dashboard</h2>

      {/* Tabs */}
      <div className="flex mb-8 space-x-4">
        <button
          onClick={() => setActiveTab("live")}
          className={`flex items-center gap-2 px-6 py-2 rounded-full font-medium transition ${
            activeTab === "live"
              ? "bg-blue-600 text-white"
              : "bg-gray-100 text-gray-800 hover:bg-gray-200"
          }`}
        >
          <HiOutlineLightningBolt />
          Live Metrics
        </button>
        <button
          onClick={() => setActiveTab("saved")}
          className={`flex items-center gap-2 px-6 py-2 rounded-full font-medium transition ${
            activeTab === "saved"
              ? "bg-blue-600 text-white"
              : "bg-gray-100 text-gray-800 hover:bg-gray-200"
          }`}
        >
          <HiOutlineClipboardList />
          Saved Snapshots
        </button>
      </div>

      {/* Content */}
      {activeTab === "live" ? <LiveMetrics /> : <SavedSnapshots />}
    </div>
  );
}
