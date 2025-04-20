import instance from "./axiosInstance";
import { RouteDefinition } from "../types/RouteDefinition";

export const getAllRoutes = async (): Promise<RouteDefinition[]> => {
  const response = await instance.get<RouteDefinition[]>("/routes");
  return response.data;
};

export const deleteRoute = async (id: number) => {
  await instance.delete(`/routes/${id}`);
};

export const createRoute = async (route: Omit<RouteDefinition, "id">) => {
  await instance.post("/routes", route);
};

export const updateRoute = async (id: number, route: Omit<RouteDefinition, "id">) => {
  await instance.patch(`/routes/${id}`, route);
};
