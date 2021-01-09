helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-controller ingress-nginx/ingress-nginx

kubectl create secret generic firebase-secret --from-file ../api-gateway/car-rental.json

kubectl apply -f ../k8