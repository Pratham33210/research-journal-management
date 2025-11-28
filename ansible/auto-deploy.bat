@echo off
echo ========================================
echo   Automated Kubernetes Deployment
echo   Research Journal Management System
echo ========================================
echo.

echo [1/8] Checking Minikube status...
minikube status
if errorlevel 1 (
    echo Starting Minikube...
    minikube start --driver=docker
)

echo.
echo [2/8] Loading Backend image into Minikube...
minikube image load researchjournalmanagement-backend:latest

echo.
echo [3/8] Loading Frontend image into Minikube...
minikube image load researchjournalmanagement-frontend:latest

echo.
echo [4/8] Deploying MySQL...
kubectl apply -f k8s/mysql-deployment.yml

echo.
echo [5/8] Waiting for MySQL to be ready...
kubectl wait --for=condition=ready pod -l app=mysql --timeout=300s

echo.
echo [6/8] Deploying Backend...
kubectl apply -f k8s/backend-deployment.yml

echo.
echo [7/8] Deploying Frontend...
kubectl apply -f k8s/frontend-deployment.yml

echo.
echo [8/8] Waiting for all pods to be ready...
timeout /t 10 /nobreak
kubectl get pods

echo.
echo ========================================
echo   Deployment Complete!
echo ========================================
echo.
echo Getting frontend URL...
minikube service frontend --url

pause
```

Save this as `auto-deploy.bat` in:
```
C:\Users\PRATHAM HEBLE\OneDrive\Desktop\Researchjournalmanagement\