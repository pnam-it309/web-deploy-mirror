# Build Stage
FROM node:22-alpine AS build-stage
WORKDIR /app

# Copy package files first to leverage caching
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Production Stage
FROM nginx:alpine AS production-stage
WORKDIR /usr/share/nginx/html

# Copy built assets from build stage
COPY --from=build-stage /app/dist .

# Copy custom nginx configuration
COPY nginx/nginx.conf /etc/nginx/nginx.conf

# Expose port (must match nginx.conf)
EXPOSE 6789

CMD ["nginx", "-g", "daemon off;"]
