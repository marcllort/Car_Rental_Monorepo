# Kubernetes
The kubernetes deployment files can be found in this directory. A declarative approach is being used, as it is the best practice.
There are some specific configurations depending on where the cluster is being deployed, but that logic is being handled by the installation script.

## Installation
To install the kubernetes cluster in your local machine, you must have Docker desktop running with Kubernetes enabled.

Then, just run the setup-kubernetes.sh script, that can be found in the scripts folder.
This script will do a few installations:
- Install the nginx ingress controller.
- Create the firebase config secret.
- Create the TLS config secret.
- Apply all the declarative configurations (deployments, services, ingress...) found in the folder.

## Deployments
- apigw-deployment: Retrieves the apigw image, and starts X amounts of replicas, with the proper environment variables. The firebase-config JSON is retrieved as a secret.

## Services
- apigw-service: NodePort. Sets the port to connect to if another service wants to connect to the apigw-deployment.

## Ingress
- ingress-service: Configuration of the nginx ingress. Uses the TLS secret to decrypt the incoming requests.

## Troubleshooting
Problems with TLS:
- Check Cloudflare project configuration, [must be set to Full encryption](https://dash.cloudflare.com/8d08c56f89fa90a2165e3f6f8005cb3a/carrentalbarcelona.tk/ssl-tls)
- Check that port :443 in your machine is available, sometimes the server binds it even if it's being used.
- Check credentials for the TLS certificate are correct and working.
- Check the port forwarding settings of the router.
- Check that the public ip or private ip of the server haven't changed. Then modify accordingly in Cloudflare or router settings.