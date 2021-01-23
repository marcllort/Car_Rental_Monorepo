helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-controller ingress-nginx/ingress-nginx

#https://phoenixnap.com/kb/install-and-configure-rabbitmq-on-kubernetes
helm install rabbitmq --set rabbitmq.username=guest,rabbitmq.password=guest stable/rabbitmq

kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml

kubectl create secret generic firebase-secret --from-file ../api-gateway/creds/car-rental.json

kubectl create secret tls cloudflare-tls --key ../api-gateway/creds/https-server.key --cert ../api-gateway/creds/https-server.crt

kubectl apply -f ../k8