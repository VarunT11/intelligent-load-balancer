import instance from "./axiosInstance";

export interface BackendStatus {
  url: string;
  status: "HEALTHY" | "UNHEALTHY";
  circuitState: "OPEN" | "CLOSED";
  reason: string | null;
}

export interface HealthOverview {
  lastCheckedAt: string;
  healthyBackends: BackendStatus[];
  unhealthyBackends: BackendStatus[];
}

export const getHealthStatus = async (): Promise<HealthOverview> => {
  const res = await instance.get<HealthOverview>("/health");
  return res.data;
};

export const refreshHealthStatus = async (): Promise<HealthOverview> => {
  const res = await instance.post<HealthOverview>("/health/refresh");
  return res.data;
};

export const openCircuit = async (backendUrl: string, reason: string) => {
  return instance.post("/health/circuit/open", { backendUrl, reason });
};

export const closeCircuit = async (backendUrl: string, reason: string) => {
  return instance.post("/health/circuit/close", { backendUrl, reason });
};
