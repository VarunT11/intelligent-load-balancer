# BalancePro: Intelligent Load Balancing Engine

An **Intelligent Load Balancer** built using **Java (Spring Boot)** and **React** that supports advanced traffic routing strategies, health checks, circuit breaking, canary deployments, real-time metrics, and admin configurability via a UI dashboard.

---

## ✨ Features

- ⚙ **Load Balancing Strategies**  
  - Round Robin
  - Random
  - Least Connections
  - Weighted Round Robin

- 🔒 **Circuit Breaker**  
  Detects failures & opens circuits when repeated failures occur. Auto-recovery supported.

- ⚡ **Health Check Service**  
  Periodically pings backends to detect failures & restores healthy ones automatically.

- 📈 **Metrics Monitoring**  
  - Live metrics: Total requests, success/failures, canary hits
  - Save/load snapshot sessions
  - Visualized in Admin Panel

- 🛠️ **Admin Panel (React + Vite)**  
  - Route management (CRUD)
  - Metrics dashboard
  - System configuration

- 🤝 **Canary Deployments**  
  Diverts a configurable % of traffic to canary backends.

- ⚖️ **Security**  
  - Basic Authentication (admin-only)
  - Configurable credentials

- 🌐 **Docker & Docker Compose Support**  
  - Fully containerized backend, UI, DB
  - One-liner setup using `docker-compose`

---

## 📚 Tech Stack

**Backend:**  
Java 21, Spring Boot, Spring Security, JPA, PostgreSQL, Docker

**Frontend:**  
React, Vite, TailwindCSS, Axios, TypeScript

**Tools:**  
Docker, Docker Compose, K6 (for load testing)

---

## 🚀 How to Run

```bash
git clone https://github.com/VarunT11/intelligent-load-balancer.git
cd intelligent-load-balancer

docker-compose up --build
```

- UI: [http://localhost:3000](http://localhost:3000)
- Backend: [http://localhost:8080](http://localhost:8080)

Login credentials for Admin Panel:
```
Username: admin
Password: Welcome1
```

---

## 📊 Load Testing Results (via K6)

Performed load test with:
- **553,744 requests** in 3 minutes
- **0% failure rate**
- **~3,074 req/sec throughput**
- **Canary Hits:** 115,959 (~21%)
- **P95 Response Time:** 130ms

> System remained stable across all routing strategies and backends recovered dynamically during simulated failures.

---

## 🔍 Project Structure

```
intelligent-load-balancer/
├── intelligent-load-balancer-backend/   # Spring Boot service
├── intelligent-load-balancer-ui/        # React + Vite frontend
├── docker-compose.yml                   # Full stack orchestration
```

---

## 📦 Production-Ready Highlights

- Supports graceful failover, scalability, and modular design
- Config-driven health checks and circuit thresholds
- Easily extensible (add metrics exporters, Kafka, etc.)

---

## 🚧 Future Enhancements

- Add Prometheus + Grafana dashboards
- JWT-based auth
- Rate limiting
- Request logging & alerting

---

## 👥 Author

Made with ❤️ by [Varun Tiwari](https://github.com/VarunT11)
