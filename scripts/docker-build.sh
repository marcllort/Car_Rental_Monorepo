pushd ../api-gateway
mvn package

docker build -t marcllort/car-rental .
docker push marcllort/car-rental
popd