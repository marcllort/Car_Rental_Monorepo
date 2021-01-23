#http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

if [ $1 == 'start' ]; then
  echo 'Starting local development!'
  echo 'Port forwarding kubernetes-dashboard...'
  kubectl proxy </dev/null &>/dev/null &

  echo 'Port forwarding rabbitmq dashboard in port 15672...'
  kubectl port-forward --namespace default svc/rabbitmq 15672:15672 </dev/null &>/dev/null &

  if [ $2 == 'local-rabbit' ]; then
    echo 'Port forwarding local rabbitmq in port 5672...'
    kubectl port-forward --namespace default svc/rabbitmq 5672:5672 </dev/null &>/dev/null &
  fi
  # Link Dashboard: http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
  # Link RabbitMQ: http://http://127.0.0.1:15672/#/
elif [ $1 == 'stop' ]; then
  echo 'Stopping local development!'
  ps aux | grep -i kubectl | grep -v grep | awk {'print $2'} | xargs kill
elif [ $1 == 'token' ]; then
  # To get credential token
  # https://stackoverflow.com/questions/46664104/how-to-sign-in-kubernetes-dashboard
  kubectl -n kube-system describe secret $(kubectl -n kube-system get secret |
    awk '/^deployment-controller-token-/{print $1}') | awk '$1=="token:"{print $2}'
else
  echo 'Error, you must define an action: start or stop'
fi
