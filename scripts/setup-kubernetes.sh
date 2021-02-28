# Add needed Helm repos
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo add stable https://charts.helm.sh/stable
helm repo add jetstack https://charts.jetstack.io
helm repo update

# Install NGINX ingress-controller with Helm
helm install ingress-controller ingress-nginx/ingress-nginx

# Install the Helm RabbitMQ, specifying user and password
#https://phoenixnap.com/kb/install-and-configure-rabbitmq-on-kubernetes
helm install rabbitmq --set rabbitmq.username=guest,rabbitmq.password=guest stable/rabbitmq

# Setup the Kubernetes dashboard
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml

# Secrets creation, first the Firebase secret and second the TLS CloudFlare secrets and DB creds
kubectl create secret generic firebase-secret --from-file ../api-gateway/creds/car-rental.json
kubectl create secret generic dbcreds-secret --from-file ../calendar/Creds/creds.json
kubectl create secret generic calendar-secret --from-file ../calendar/Creds/calendar-api-credentials.json
kubectl create secret tls cloudflare-tls --key ../api-gateway/creds/https-server.key --cert ../api-gateway/creds/https-server.crt

# Set the password through the parameters of the scripts
kubectl create secret generic secret-hash --from-literal="SECRET_HASH=$1"
kubectl create secret generic secret-db --from-literal="SECRET_DB=$1"
kubectl create secret generic secret-google-api --from-literal="google.client.client-secret=$2"
kubectl create secret generic secret-email-creds --from-literal="futbolsupplier=$3"

# Apply all kubernetes configurations
kubectl apply -f ../k8
