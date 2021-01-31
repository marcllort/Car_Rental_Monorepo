# Scripts
In this folder there are multiple scripts to automate the different processes needed in the application.

They can be classified by CI/CD, Docker and Kubernetes:

## CI/CD
For now, they are empty

## Docker
- docker-build.sh: Script to automatize the creation of the image with the necessary tag, and its upload to the registry.
  - `docker-build.sh SERVICE`: Where service is the name of the microservice (i.e: calendar). Will build the image and publish it to the repository.

## Kubernetes
- setup-kubernetes.sh:
  This script will do a few installations:
  - Add all the necessary Helm repos.
  - Install the nginx ingress controller (with Helm).
  - Install the RabbitMQ deployment (with Helm).
  - Setup the Kubernetes dashboard.
  - Create the firebase config secret.
  - Create the TLS config secret.
  - Apply all the declarative configurations (deployments, services, ingress...) found in the folder.

- cleanup-kubernetes.sh: Responsible for uninstalling all the services, deployments and secrets created by the
  installation script.

## Secrets

They are created in the `setup-kubernetes.sh` script. Important to notice that when running the script, you must pass
the SECRET_HASH environment variable for the api-gw. The same is applicable for the AWS DB password, used in the rest of
microservices.

## Local development

- local-development.sh: It creates a proxy with the Kubernetes dashboard and port-forwards the frontend of RabbitMQ (
  15672).
  - `local-development.sh start`: Starts the proxy of the dashboard and the forwarding of RabbitMQ frontend. If used
    with the parameter 'start local-rabbit' it will also port-forward the rabbitMQ deployment (5672).
  - `local-development.sh stop`: Stops all the proxies and port-forwards running.
  - `local-development.sh token`: Shows the token needed to login in the Kubernetes dashboard.