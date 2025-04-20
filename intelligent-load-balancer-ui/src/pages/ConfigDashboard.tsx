import { useEffect, useState } from "react";
import {
  getAllConfigs,
  addConfig,
  updateConfig,
  ConfigEntry,
} from "../api/configApi";
import {
  HiOutlineCog,
  HiOutlinePlus,
  HiOutlinePencil,
  HiOutlineX,
} from "react-icons/hi";

export default function ConfigDashboard() {
  const [configs, setConfigs] = useState<ConfigEntry[]>([]);
  const [newKey, setNewKey] = useState("");
  const [newValue, setNewValue] = useState("");
  const [editItem, setEditItem] = useState<ConfigEntry | null>(null);
  const [editValue, setEditValue] = useState("");

  useEffect(() => {
    fetchConfigs();
  }, []);

  const fetchConfigs = async () => {
    try {
      const data = await getAllConfigs();
      setConfigs(data);
    } catch (err) {
      console.error("Error fetching configs", err);
    }
  };

  const handleAdd = async () => {
    if (!newKey.trim() || !newValue.trim()) {
      alert("Both key and value are required.");
      return;
    }
    try {
      await addConfig({ key: newKey.trim(), value: newValue.trim() });
      setNewKey("");
      setNewValue("");
      fetchConfigs();
    } catch (err) {
      alert("Failed to add config");
      console.error(err);
    }
  };

  const handleEdit = (item: ConfigEntry) => {
    setEditItem(item);
    setEditValue(item.value);
  };

  const handleUpdate = async () => {
    if (!editItem) return;
    try {
      await updateConfig(editItem.id, {
        key: editItem.key,
        value: editValue.trim(),
      });
      setEditItem(null);
      setEditValue("");
      fetchConfigs();
    } catch (err) {
      alert("Failed to update config");
      console.error(err);
    }
  };

  return (
    <div className="bg-white shadow-md rounded-lg p-8 max-w-5xl mx-auto space-y-10">
      <div className="flex items-center gap-2 text-blue-600 mb-2">
        <HiOutlineCog className="text-3xl" />
        <h2 className="text-3xl font-bold text-gray-900">System Properties</h2>
      </div>

      {/* Config Table */}
      <table className="w-full border shadow-sm mb-8">
        <thead className="bg-gray-100">
          <tr>
            <th className="p-2 border text-left">Key</th>
            <th className="p-2 border text-left">Value</th>
            <th className="p-2 border text-left">Description</th>
            <th className="p-2 border text-center">Actions</th>
          </tr>
        </thead>
        <tbody>
          {configs.map((config) => (
            <tr key={config.id} className="border-t hover:bg-gray-50">
              <td className="p-2 border font-mono">{config.key}</td>
              <td className="p-2 border">{config.value}</td>
              <td className="p-2 border text-sm text-gray-600">{config.description}</td>
              <td className="p-2 border text-center">
                <button
                  className="text-blue-600 hover:text-blue-800"
                  onClick={() => handleEdit(config)}
                  title="Edit"
                >
                  <HiOutlinePencil size={18} />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Add New Config */}
      <div>
        <h3 className="text-xl font-semibold mb-2 flex items-center gap-2">
          <HiOutlinePlus className="text-green-600" />
          Add New Config
        </h3>
        <div className="flex flex-wrap items-center gap-4">
          <input
            type="text"
            placeholder="Key"
            value={newKey}
            onChange={(e) => setNewKey(e.target.value)}
            className="border px-4 py-2 rounded w-64"
          />
          <input
            type="text"
            placeholder="Value"
            value={newValue}
            onChange={(e) => setNewValue(e.target.value)}
            className="border px-4 py-2 rounded w-64"
          />
          <button
            onClick={handleAdd}
            className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 flex items-center gap-2"
          >
            <HiOutlinePlus /> Add Config
          </button>
        </div>
      </div>

      {/* Edit Modal */}
      {editItem && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-full max-w-md relative">
            <button
              onClick={() => setEditItem(null)}
              className="absolute top-3 right-3 text-gray-500 hover:text-black"
              title="Close"
            >
              <HiOutlineX size={20} />
            </button>
            <h3 className="text-xl font-bold mb-4">Edit Config</h3>
            <p className="text-sm text-gray-500 mb-2">
              Key: <code>{editItem.key}</code>
            </p>
            <input
              value={editValue}
              onChange={(e) => setEditValue(e.target.value)}
              className="border px-4 py-2 rounded w-full mb-4"
            />
            <div className="flex justify-end gap-4">
              <button
                onClick={() => setEditItem(null)}
                className="text-gray-500 hover:text-black"
              >
                Cancel
              </button>
              <button
                onClick={handleUpdate}
                className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
              >
                Update
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
