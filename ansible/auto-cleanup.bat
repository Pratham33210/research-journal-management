@echo off
echo ========================================
echo   Cleaning up Kubernetes Resources
echo ========================================
echo.

echo Deleting Frontend...
kubectl delete -f k8s/frontend-deployment.yml

echo.
echo Deleting Backend...
kubectl delete -f k8s/backend-deployment.yml

echo.
echo Deleting MySQL...
kubectl delete -f k8s/mysql-deployment.yml

echo.
echo Checking remaining pods...
kubectl get pods

echo.
echo Cleanup complete!
pause
```

---

## Now You Can Use:

- **`auto-deploy.bat`** - Deploy everything automatically
- **`auto-cleanup.bat`** - Clean up everything

---

## This is Actually Better Because:

✅ Uses your existing Windows setup
✅ No container complexity
✅ Directly uses minikube and kubectl
✅ One-click deployment!

---

## Create These Two Files Now!

Then test with:
```
auto-deploy.bat