import instance from "./axiosInstance";

export const saveMetricsSnapshot = async (sessionName: string) => {
  await instance.post(`/metrics/sessions`, null, {
    params: { sessionName },
  });
};

export interface MetricsSession {
  id: number;
  sessionName: string;
  timestamp: number;
}

export interface MetricsData {
  liveMetricsStartTime: number | null;
  totalRequests: Record<string, number>;
  successfulRequests: Record<string, number>;
  failedRequests: Record<string, number>;
  canaryHits: Record<string, number>;
}

export interface MetricsSnapshot {
  id: number;
  sessionName: string;
  startTime: number;
  endTime: number;
  snapshot: MetricsData;
}

export const getAllSessions = async (): Promise<MetricsSession[]> => {
  const res = await instance.get<MetricsSession[]>("/metrics/sessions");
  return res.data;
};

export const getSessionById = async (id: number): Promise<MetricsSnapshot> => {
  const res = await instance.get<MetricsSnapshot>(`/metrics/sessions/${id}`);
  return res.data;
};

export const getMetrics = async (): Promise<MetricsData> => {
  const res = await instance.get<MetricsData>("/metrics");
  return res.data;
};
