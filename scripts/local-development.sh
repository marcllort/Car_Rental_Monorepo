#http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

if [ $1 == 'start' ]; then
  echo 'Starting local development!'
  echo 'Port forwarding kubernetes-dashboard...'
  kubectl proxy &

  echo 'Port forwarding rabbitmq dashboard in port 5672...'

  if [ $2 == 'local-rabbit' ]; then
      echo 'Port forwarding local rabbitmq in port 5672...'
      kubectl port-forward --namespace default svc/rabbitmq 5672:5672 &
  fi
elif [ $1 == 'stop' ]; then
  echo 'Stopping local development!'
else
  echo 'Error, you must define an action: start or stop'
fi
# To get credential token
#https://stackoverflow.com/questions/46664104/how-to-sign-in-kubernetes-dashboard
# kubectl -n kube-system get secret
# kubectl -n kube-system describe secret deployment-controller-token-XXXX
# Use token, delete enters
