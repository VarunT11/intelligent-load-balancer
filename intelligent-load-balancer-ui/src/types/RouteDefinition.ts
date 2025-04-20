export interface Backend {
    id: string;
    url: string;
}

export interface RouteDefinition {
    id: number;
    routePath: string;
    primaryBackends: Backend[];
    canaryBackends: Backend[];
    canaryTrafficPercent: number;
    strategyType: string;
}