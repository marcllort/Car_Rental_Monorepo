# API Gateway

Microservice responsible for the authentication of the user, and protecting the endpoints of the backend.

To do the authentication, Firebase (from Google) is used to provide a token, and also allow for advanced sign-in options,
and the same token is being used when the authenticated user tries to do a call to the API.

It checks if the user is logged in, and has the necessary role/permissions to do the requested call/action.

## Endpoints
- /public/covid
- /public/data

- /protected/
- /protected/
- /protected/
- /protected/

- /super/user
- /super/data

### Firebase Authentication for Spring boot

Firebase is a backendless platform to run applications without a dedicated backend. But, sometimes you may need to
communicate with API of an existing backend, or you may want a dedicated backend to perform operations that cannot be
done through firebase infrastructure.

This **Spring Boot Starter** is perfect for such situations when you want to extend firebase's authentication mechanism
with **Spring Security** to seamlessly create and use protected rest API's.

### Configuration

- Be sure to add the following environment variable globally or project specific run configuration environment
  variable `GOOGLE_APPLICATION_CREDENTIALS=path_to_firebase_server_config.json`

- The starter can be configured to use firebase session as client side / strictly server side or both together.
- Http Only / Secure enabled Session cookies may not work as expected in development hosts (localhost, 120.0.0.1).
  Adding self-signed ssl certificate with reverse proxied host will work perfectly fine. Read this article
  => [Local Domain Names with SSL for development applications ](https://thepro.io/post/local-domain-names-with-ssl-for-local-development-applications-LG)
- Following application properties can edited to customize for your needs.

### Role Management

- Roles can be added through `SecurityRoleService` during registration of user or manually managed by Super admins
- Super Admins are defined through application property `security.super-admins`
- With roles integrated with spring security, spring authorization annotations
  like **`@Secured, @RolesAllowed, @PreAuthorize, @PostAuthorized`** etc will work out of the box.
- I personally like to define per role annotations like **`@IsSuper`** etc for the sake of simplicity.

```
    @GetMapping("data")
	@isSuper
	public String getProtectedData() {
		return "You have accessed seller only data from spring boot";
	}
```

## Environment vars

GOOGLE_APPLICATION_CREDENTIALS=api-gateway/car-rental.json 
SUPER_ADMINS=mac12llm2@gmail.com

##Docker
1. Build image
docker build -t marcllort/car-rental .
docker push marcllort/car-rental 

2. Create secret in kubernetes cluster
kubectl create secret generic firebase-secret --from-file car-rental.json
   
3. Configure NGINX
   https://kubernetes.github.io/ingress-nginx/deploy/
    Steps:
    helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
    helm repo update
    helm install my-release ingress-nginx/ingress-nginx




apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
annotations:
kubernetes.io/ingress.class: nginx
name: example
namespace: foo
spec:
rules:
- host: www.example.com
http:
paths:
- backend:
serviceName: exampleService
servicePort: 80
path: /
# This section is only required if TLS is to be enabled for the Ingress
tls:
- hosts:
- www.example.com
secretName: example-tls

If TLS is enabled for the Ingress, a Secret containing the certificate and key must also be provided:

apiVersion: v1
kind: Secret
metadata:
name: example-tls
namespace: foo
data:
tls.crt: <base64 encoded cert>
tls.key: <base64 encoded key>
type: kubernetes.io/tls

  

## Related Tutorials :

- [Firebase Authentication for Spring Boot Rest API](https://thepro.io/post/firebase-authentication-for-spring-boot-rest-api-5V)
- [Firebase with Spring Boot for Kubernetes Deployment Configuration](https://thepro.io/post/firebase-with-spring-boot-kubernetes-deployment-configuration-RA)
- [Local Domain Names with SSL for development applications ](https://thepro.io/post/local-domain-names-with-ssl-for-local-development-applications-LG)