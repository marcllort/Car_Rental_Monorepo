# Scripts
In this folder there are multiple scripts to automate the different processes needed in the application.

They can be classified by CI/CD, Docker and Kubernetes:

## CI/CD
For now, they are empty

## Docker
- docker-build.sh: Script to automatize the creation of the image with the necessary tag, and its upload to the registry.

## Kubernetes
- setup-kubernetes.sh: 
  This script will do a few installations:
    - Install the nginx ingress controller.
    - Create the firebase config secret.
    - Create the TLS config secret.
    - Apply all the declarative configurations (deployments, services, ingress...) found in the folder.
  
- cleanup-kubernetes.sh: Responsible of uninstalling all the services, deployments and secrets created by the installation script.