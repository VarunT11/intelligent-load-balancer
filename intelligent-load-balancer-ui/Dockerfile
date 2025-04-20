# Stage 1: Build the Vite React UI
FROM node:18-alpine AS build

WORKDIR /app
COPY . .
RUN npm install
RUN npm run build

# Stage 2: Serve with NGINX
FROM nginx:alpine

# 🧹 Optional: clean out default nginx static files
RUN rm -rf /usr/share/nginx/html/*

# 📦 Copy built dist files
COPY --from=build /app/dist /usr/share/nginx/html

# 🌐 Optional: Custom nginx config if using react-router
# COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
