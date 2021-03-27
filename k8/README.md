RetrieveCalendarProducer# Kubernetes

The kubernetes deployment files can be found in this directory. A declarative approach is being used, as it is the best
practice. There are some specific configurations depending on where the cluster is being deployed, but that logic is
being handled by the installation script.

## Installation

To install the kubernetes cluster in your local machine, you must have Docker desktop running with Kubernetes enabled.

Then, just run the setup-kubernetes.sh script, that can be found in the scripts folder. This script will do a few
installations:

- Install the nginx ingress controller.
- Create the firebase config secret.
- Create the TLS config secret.
- Apply all the declarative configurations (deployments, services, ingress...) found in the folder.

## Deployments

- apigw-deployment: Retrieves the apigw image, and starts X amounts of replicas, with the proper environment variables.
  The firebase-config JSON is retrieved as a secret.
- legal-deployment:  Retrieves the legal image, and starts X amounts of replicas, without any environment variables. No
  need for a service, as it connects to the other services using RabbitMQ.
- calendar-deployment:  Retrieves the calendar image, and starts X amounts of replicas, without any environment
  variables. No need for a service, as it connects to the other services using RabbitMQ.
- email-deployment:  Retrieves the email image, and starts X amounts of replicas, without any environment variables. No
  need for a service, as it connects to the other services using RabbitMQ.

## Services

- apigw-service: NodePort. Sets the port to connect to if another service wants to connect to the apigw-deployment.
- rabbitmq-service: Two services an internal clusterIP, and an external LoadBalancer, so all the microservices can
  connect using the dns name: `rabbitmq`. This loadBalancer points to the internal RabbitMQ ClusterIP service, which
  points to the RabbitMQ deployment.

## Ingress

- ingress-service: Configuration of the nginx ingress. Uses the TLS secret to decrypt the incoming requests.

## RBAC

- rabbitmq-rbac: Is a method of regulating access to computer or network resources based on the roles of individual
  users within your organization.

## ConfigMaps

- rabbitmq-configmap: Useful to set up some environment variables to be later used by the deployment.

## Stateful Set

- rabbitmq-stateful-set: StatefulSets represent a set of Pods with unique, persistent identities and stable hostnames
  that GKE maintains regardless of where they are scheduled. Once created, the StatefulSet ensures that the desired
  number of Pods are running and available at all times. Useful in case a RabbitMQ pod fails.

## Secrets

They are created in the `setup-kubernetes.sh` script. Important to notice that when running the script, you must pass
the SECRET_HASH environment variable for the api-gw. The same is applicable for the AWS DB password, used in the rest of
microservices.

## Troubleshooting

Problems with TLS:

- Check Cloudflare project
  configuration, [must be set to Full encryption](https://dash.cloudflare.com/8d08c56f89fa90a2165e3f6f8005cb3a/carrentalbarcelona.tk/ssl-tls)
- Check that port :443 in your machine is available, sometimes the server binds it even if it's being used.
- Check credentials for the TLS certificate are correct and working.
- Check the port forwarding settings of the router.
- Check that the public ip or private ip of the server haven't changed. Then modify accordingly in Cloudflare or router
  settings.