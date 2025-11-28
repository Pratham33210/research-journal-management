FROM python:3.11-slim

# Install Ansible and required dependencies
RUN apt-get update && \
    apt-get install -y \
    curl \
    apt-transport-https \
    ca-certificates \
    gnupg \
    lsb-release && \
    pip install ansible && \
    curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" && \
    install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl && \
    rm kubectl

WORKDIR /ansible

# Copy playbooks
COPY . /ansible

CMD ["/bin/bash"]