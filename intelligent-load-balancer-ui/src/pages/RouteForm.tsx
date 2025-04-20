import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getAllRoutes, createRoute, updateRoute } from "../api/routesApi";
import { HiOutlineX, HiOutlinePlus } from "react-icons/hi";

const strategyOptions = [
  { value: "ROUND_ROBIN", label: "Round Robin" },
  { value: "RANDOM", label: "Random" },
  { value: "LEAST_CONNECTIONS", label: "Least Connections" },
  { value: "WEIGHTED_ROUND_ROBIN", label: "Weighted Round Robin" },
];

type Backend = { id: string; url: string };

export default function RouteFormPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEdit = Boolean(id);

  const [routePath, setRoutePath] = useState("");
  const [strategyType, setStrategyType] = useState("ROUND_ROBIN");
  const [primaryBackends, setPrimaryBackends] = useState<Backend[]>([{ id: "", url: "" }]);
  const [canaryBackends, setCanaryBackends] = useState<Backend[]>([]);
  const [canaryTrafficPercent, setCanaryTrafficPercent] = useState(0);
  const [loading, setLoading] = useState(isEdit);
  const [errors, setErrors] = useState<string[]>([]);

  useEffect(() => {
    if (isEdit) {
      getAllRoutes()
        .then((routes) => {
          const route = routes.find((r) => r.id === Number(id));
          if (route) {
            setRoutePath(route.routePath);
            setStrategyType(route.strategyType);
            setPrimaryBackends(route.primaryBackends.length ? route.primaryBackends : [{ id: "", url: "" }]);
            setCanaryBackends(route.canaryBackends);
            setCanaryTrafficPercent(route.canaryTrafficPercent);
          }
        })
        .catch(console.error)
        .finally(() => setLoading(false));
    }
  }, [id, isEdit]);

  const validateForm = () => {
    const errs: string[] = [];
    if (!routePath.trim()) errs.push("Route Path is required.");
    if (primaryBackends.length < 2) errs.push("At least 2 primary backends are required.");
    if (primaryBackends.some(b => !b.id.trim() || !b.url.trim())) errs.push("All primary backend fields must be filled.");
    if (canaryBackends.some(b => !b.id.trim() || !b.url.trim())) errs.push("All canary backend fields must be filled.");
    if (canaryTrafficPercent > 0 && canaryBackends.length === 0) errs.push("At least one canary backend is required when traffic % is greater than 0.");
    return errs;
  };

  const handleSubmit = async () => {
    const validationErrors = validateForm();
    if (validationErrors.length > 0) {
      setErrors(validationErrors);
      return;
    }

    const payload = {
      routePath,
      strategyType,
      primaryBackends,
      canaryBackends,
      canaryTrafficPercent,
    };

    try {
      if (isEdit) {
        await updateRoute(Number(id), payload);
      } else {
        await createRoute(payload);
      }
      navigate(isEdit ? `/routes/${id}` : "/routes");
    } catch (err) {
      console.error("Error saving route:", err);
      alert("Failed to save route.");
    }
  };

  const handleBackendChange = (
    list: Backend[],
    setList: (v: Backend[]) => void,
    index: number,
    field: keyof Backend,
    value: string
  ) => {
    const updated = [...list];
    updated[index][field] = value;
    setList(updated);
  };

  const addBackendRow = (list: Backend[], setList: (v: Backend[]) => void) => {
    setList([...list, { id: "", url: "" }]);
  };

  const removeBackendRow = (list: Backend[], setList: (v: Backend[]) => void, index: number) => {
    const updated = [...list];
    updated.splice(index, 1);
    setList(updated);
  };

  if (loading) return <p>Loading...</p>;

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-3xl mx-auto space-y-8">
      <h2 className="text-3xl font-bold text-center">
        {isEdit ? "Update Route" : "Add New Route"}
      </h2>

      {/* Errors */}
      {errors.length > 0 && (
        <div className="bg-red-100 border border-red-400 text-red-700 p-4 rounded">
          <ul className="list-disc ml-5 text-sm">
            {errors.map((err, idx) => (
              <li key={idx}>{err}</li>
            ))}
          </ul>
        </div>
      )}

      {/* Route Path */}
      <div>
        <label className="block font-medium mb-1">Route Path *</label>
        <input
          type="text"
          value={routePath}
          onChange={(e) => setRoutePath(e.target.value)}
          className="border px-4 py-2 rounded w-full shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
        />
      </div>

      {/* Strategy */}
      <div>
        <label className="block font-medium mb-1">Strategy *</label>
        <select
          value={strategyType}
          onChange={(e) => setStrategyType(e.target.value)}
          className="border px-4 py-2 rounded w-full shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
        >
          {strategyOptions.map((opt) => (
            <option key={opt.value} value={opt.value}>
              {opt.label}
            </option>
          ))}
        </select>
      </div>

      {/* Primary Backends */}
      <div>
        <label className="block font-semibold mb-2 text-lg">Primary Backends *</label>
        <div className="space-y-2">
          {primaryBackends.map((b, idx) => (
            <div key={idx} className="flex gap-2 items-center">
              <input
                placeholder="ID"
                value={b.id}
                onChange={(e) =>
                  handleBackendChange(primaryBackends, setPrimaryBackends, idx, "id", e.target.value)
                }
                className="border px-3 py-2 rounded w-1/3"
              />
              <input
                placeholder="URL"
                value={b.url}
                onChange={(e) =>
                  handleBackendChange(primaryBackends, setPrimaryBackends, idx, "url", e.target.value)
                }
                className="border px-3 py-2 rounded w-2/3"
              />
              {primaryBackends.length > 1 && (
                <button
                  className="text-red-600 hover:text-red-800"
                  onClick={() => removeBackendRow(primaryBackends, setPrimaryBackends, idx)}
                >
                  <HiOutlineX size={18} />
                </button>
              )}
            </div>
          ))}
        </div>
        <button
          className="mt-2 flex items-center text-sm text-blue-600 hover:underline"
          onClick={() => addBackendRow(primaryBackends, setPrimaryBackends)}
        >
          <HiOutlinePlus className="mr-1" /> Add Primary Backend
        </button>
      </div>

      {/* Canary Backends */}
      <div>
        <label className="block font-semibold mb-2 text-lg">Canary Backends</label>
        <div className="space-y-2">
          {canaryBackends.map((b, idx) => (
            <div key={idx} className="flex gap-2 items-center">
              <input
                placeholder="ID"
                value={b.id}
                onChange={(e) =>
                  handleBackendChange(canaryBackends, setCanaryBackends, idx, "id", e.target.value)
                }
                className="border px-3 py-2 rounded w-1/3"
              />
              <input
                placeholder="URL"
                value={b.url}
                onChange={(e) =>
                  handleBackendChange(canaryBackends, setCanaryBackends, idx, "url", e.target.value)
                }
                className="border px-3 py-2 rounded w-2/3"
              />
              <button
                className="text-red-600 hover:text-red-800"
                onClick={() => removeBackendRow(canaryBackends, setCanaryBackends, idx)}
              >
                <HiOutlineX size={18} />
              </button>
            </div>
          ))}
        </div>
        <button
          className="mt-2 flex items-center text-sm text-blue-600 hover:underline"
          onClick={() => addBackendRow(canaryBackends, setCanaryBackends)}
        >
          <HiOutlinePlus className="mr-1" /> Add Canary Backend
        </button>
      </div>

      {/* Canary Traffic Slider */}
      <div>
        <label className="block font-semibold mb-1">Canary Traffic %</label>
        <input
          type="range"
          min={0}
          max={100}
          value={canaryTrafficPercent}
          onChange={(e) => setCanaryTrafficPercent(Number(e.target.value))}
          className="w-full"
        />
        <p className="text-sm text-gray-600 mt-1">{canaryTrafficPercent}%</p>
      </div>

      {/* Submit */}
      <button
        className="w-full bg-blue-600 text-white px-6 py-3 rounded font-semibold hover:bg-blue-700 transition-all"
        onClick={handleSubmit}
      >
        {isEdit ? "Update Route" : "Add Route"}
      </button>
    </div>
  );
}
