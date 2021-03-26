case $1 in

api-gw)
  pushd ../api-gateway
  docker build -t marcllort/car-rental-apigw .
  docker push marcllort/car-rental-apigw
  popd
  ;;

orchestrator)
  pushd ../orchestrator
  docker build -t marcllort/car-rental-orchestrator .
  docker push marcllort/car-rental-orchestrator
  popd
  ;;

legal)
  pushd ../legal
  docker build -t marcllort/car-rental-legal .
  docker push marcllort/car-rental-legal
  popd
  ;;

calendar)
  pushd ../calendar
  docker build -t marcllort/car-rental-calendar .
  docker push marcllort/car-rental-calendar
  popd
  ;;

email)
  pushd ../email
  docker build -t marcllort/car-rental-email .
  docker push marcllort/car-rental-email
  popd
  ;;

payment)
  pushd ../payment
  docker build -t marcllort/car-rental-payment .
  docker push marcllort/car-rental-payment
  popd
  ;;

*)
  echo "Error, non-existing option"
  ;;
esac
