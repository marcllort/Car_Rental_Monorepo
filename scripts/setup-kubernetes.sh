helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-controller ingress-nginx/ingress-nginx

kubectl create secret generic firebase-secret --from-file ../api-gateway/car-rental.json

kubectl create secret tls cloudflare-tls --key ../api-gateway/creds/https-server.key --cert ../api-gateway/creds/https-server.crt

kubectl apply -f ../k8