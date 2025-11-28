@echo off
REM Quick deployment script for Windows

echo Starting Kubernetes Deployment with Ansible...
docker-compose -f docker-compose-ansible.yml run --rm ansible ansible-playbook deploy-k8s.yml
echo Deployment complete!
pause