import { useEffect, useState } from "react";
import { getAllRoutes } from "../api/routesApi";
import { RouteDefinition } from "../types/RouteDefinition";
import { Link } from "react-router-dom";
import {
  HiOutlinePlusCircle,
  HiOutlineSearch,
  HiOutlineEye,
  HiOutlinePencilAlt,
} from "react-icons/hi";

const strategyMap: Record<string, string> = {
  ROUND_ROBIN: "Round Robin",
  RANDOM: "Random",
  LEAST_CONNECTIONS: "Least Connections",
  WEIGHTED_ROUND_ROBIN: "Weighted Round Robin",
};

export default function RouteList() {
  const [routes, setRoutes] = useState<RouteDefinition[]>([]);
  const [search, setSearch] = useState("");

  useEffect(() => {
    getAllRoutes().then(setRoutes).catch(console.error);
  }, []);

  const filteredRoutes = routes.filter((route) =>
    route.routePath.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-5xl mx-auto space-y-8">
      {/* Header */}
      <div className="flex justify-between items-center">
        <h2 className="text-3xl font-bold">Routes</h2>
        <Link
          to="/routes/new"
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 flex items-center gap-2"
        >
          <HiOutlinePlusCircle size={20} />
          Add Route
        </Link>
      </div>

      {/* Search Input */}
      <div className="relative">
        <HiOutlineSearch className="absolute top-3 left-3 text-gray-400" />
        <input
          type="text"
          placeholder="Search by route path..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="border px-4 py-2 pl-10 rounded w-full"
        />
      </div>

      {/* Table */}
      <table className="w-full border shadow-sm">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-3 border text-left">Route Path</th>
            <th className="p-3 border text-left">Strategy</th>
            <th className="p-3 border text-left">Total Servers</th>
            <th className="p-3 border text-left">Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredRoutes.map((route) => (
            <tr key={route.id} className="border-t hover:bg-gray-50">
              <td className="p-3 border font-mono">{route.routePath}</td>
              <td className="p-3 border">
                {strategyMap[route.strategyType] || route.strategyType}
              </td>
              <td className="p-3 border">
                {route.primaryBackends.length + route.canaryBackends.length}
              </td>
              <td className="p-3 border space-x-3">
                <Link
                  to={`/routes/${route.id}`}
                  className="text-blue-600 hover:underline inline-flex items-center gap-1"
                >
                  <HiOutlineEye /> View
                </Link>
                <Link
                  to={`/routes/${route.id}/edit`}
                  className="text-green-600 hover:underline inline-flex items-center gap-1"
                >
                  <HiOutlinePencilAlt /> Edit
                </Link>
              </td>
            </tr>
          ))}
          {filteredRoutes.length === 0 && (
            <tr>
              <td
                colSpan={4}
                className="p-4 text-center text-sm text-gray-500"
              >
                No routes found.
              </td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
