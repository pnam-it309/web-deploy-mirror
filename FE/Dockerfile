FROM node:22-alpine AS base
WORKDIR /app
COPY package*.json ./
RUN npm install

# Development Stage
FROM base AS development
COPY . .
EXPOSE 6789
CMD ["npm", "run", "dev"]

# Build Stage
FROM base AS build-stage
COPY . .
ARG VITE_API_URL
ENV VITE_API_BASE_URL=$VITE_API_URL
RUN npm run build

# Production Stage
FROM nginx:alpine AS production-stage
WORKDIR /usr/share/nginx/html
COPY --from=build-stage /app/dist .
COPY nginx/nginx.conf /etc/nginx/nginx.conf
EXPOSE 6789
CMD ["nginx", "-g", "daemon off;"]
