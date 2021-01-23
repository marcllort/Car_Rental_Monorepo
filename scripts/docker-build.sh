

case $1 in

  api-gw)
    pushd ../api-gateway
    docker build -t marcllort/car-rental-apigw .
    docker push marcllort/car-rental-apigw
    popd
    ;;

  legal)
    pushd ../legal
    docker build -t marcllort/car-rental-legal .
    docker push marcllort/car-rental-legal
    popd
    ;;

  calendar)
    STATEMENTS
    ;;

  email)
    STATEMENTS
    ;;

  payment)
    STATEMENTS
    ;;

  *)
    STATEMENTS
    ;;
esac