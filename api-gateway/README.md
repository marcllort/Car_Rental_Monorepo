# API Gateway

Microservice responsible for the authentication of the user, and protecting the endpoints of the backend.

To do the authentication, Firebase (from Google) is used to provide a token, and also allow for advanced sign-in options,
and the same token is being used when the authenticated user tries to do a call to the API.

It checks if the user is logged in, and has the necessary role/permissions to do the requested call/action.

## Endpoints
- /public/covid
- /public/data

- /protected/data
- /protected/calendar
- /protected/
- /protected/

- /super/user
- /super/list-users
- /super/create-user
- /super/update-user
- /super/delete-user
- /super/set-claim
- /super/activate-account
- /super/reset-password
- /super/revoke-token

- /session/login
- /session/logout
- /session/me
- /session/create/token

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

- GOOGLE_APPLICATION_CREDENTIALS=api-gateway/scripts/car-rental.json 
- SUPER_ADMINS=mac12llm2@gmail.com
- CORS_DOMAIN=http://localhost:63343

##Docker
As Kubernetes is being used to host the infrastructure of the project, all the microservices must be containerized.

The api-gw, and all the other microservices, have a Dockerfile that builds the image that will later be used by Kubernetes.

There is also, in the scripts folder, a docker-build.sh script to automatize the creation of the image with the necessary tag, and its upload to the registry.