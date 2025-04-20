import instance from "./axiosInstance";

export interface ConfigEntry {
  id: number;
  key: string;
  value: string;
  description: string;
}

export interface ConfigPayload {
  key: string;
  value: string;
}

// GET all configs
export const getAllConfigs = async (): Promise<ConfigEntry[]> => {
  const res = await instance.get<ConfigEntry[]>("/configs");
  return res.data;
};

// PATCH existing config using ID
export const updateConfig = async (id: number, payload: ConfigPayload) => {
  return instance.patch(`/configs/${id}`, payload);
};

// POST new config (key + value)
export const addConfig = async (payload: ConfigPayload) => {
  return instance.post("/configs", payload);
};
