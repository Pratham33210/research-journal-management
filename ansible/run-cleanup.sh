#!/bin/bash
# Quick cleanup script

echo "🧹 Cleaning up Kubernetes resources..."
docker-compose -f docker-compose-ansible.yml run --rm ansible ansible-playbook cleanup-k8s.yml
echo "✅ Cleanup complete!"