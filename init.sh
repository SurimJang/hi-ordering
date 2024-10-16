sudo apt-get update
sudo apt-get install net-tools
sudo apt-get install tree
sudo apt install iputils-ping
pip install httpie

#  << kubectl >>
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

# << Azure aks >>
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash 

#  << NVM >>
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash
. ~/.nvm/nvm.sh
nvm install 14.19.0 && nvm use 14.19.0
export NODE_OPTIONS=--openssl-legacy-provider

# << helm for kafka & ingress >>
curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 > get_helm.sh
chmod 700 get_helm.sh
./get_helm.sh
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add stable https://charts.helm.sh/stable
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
# Prometheus 저장소 추가
helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo update

#  << Kafka >>
# cd infra
# docker-compose up