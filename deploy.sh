#!/bin/bash

# Exit on error
set -e

# Variables
IMAGE_NAME="us-central1-docker.pkg.dev/decent-destiny-463614-g6/spring-app-repo/june-app:latest"
REGION="us-central1"
SERVICE_NAME="june-app"
DOCKERFILE_PATH="june/Dockerfile"

echo "🔨 Building Docker image..."
docker buildx build \
  --platform=linux/amd64 \
  -f "$DOCKERFILE_PATH" \
  -t "$IMAGE_NAME" \
  --push .

echo "🚀 Deploying to Cloud Run..."
gcloud run deploy "$SERVICE_NAME" \
  --image="$IMAGE_NAME" \
  --region="$REGION" \
  --platform=managed \
  --allow-unauthenticated \
  --port=8080 \
  --timeout=300s

echo "✅ Deployment complete."
