#!/bin/bash

# Exit immediately if any command fails
set -e

echo "1. Pulling the latest code from GitHub..."
git pull origin main
echo "✅ Git pull completed"

echo "2. Performing Gradle build..."
./gradlew clean build -x test
echo "✅ Gradle build completed"

echo "3. Stopping running Gradle daemon..."
./gradlew --stop
echo "✅ Gradle daemon stopped"

echo "4. Stopping the existing Docker container..."
docker compose  --profile prod down web
echo "✅ Docker container stopped"

echo "5. Removing the existing Docker image..."
docker image rm mypicday || echo "⚠️ No image found to remove"
echo "✅ Docker image removed"

echo "6. Starting the Docker container..."
docker compose --profile prod up -d web
echo "✅ Docker container started"

echo "🎉 Deployment completed!"