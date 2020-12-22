# Firebase Authentication for Spring boot [![License: MIT](https://img.shields.io/badge/License-MIT-brightgreen.svg)](https://opensource.org/licenses/MIT)

[![Open with ThePro](https://thepro.io/button.svg)](https://thepro.io/post/firebase-authentication-for-spring-boot-rest-api-5V)

Firebase is a backendless platform to run applications without dedicated backend. But, sometimes you may need to communicate with API of an exisiting backend or you may want a dedicated backend to perform operations that cannot be done through firebase infrastructure.

This **Spring Boot Starter** is perfect for such situations when you want to extend firebase's authentication menchanism with **Spring Security** to seamlessly create and use protected rest API's.

### Configuration

- Be sure to add the following environment variable globally or project specific run configuration environment variable `GOOGLE_APPLICATION_CREDENTIALS=path_to_firebase_server_config.json`

- The starter can be configured to use firebase session as client side / strictly server side or both together.
- Htty Only / Secure enabled Session cookies may not work as expected in development hosts (localhost, 120.0.0.1). Adding self signed ssl certificate with reverse proxied host will work perfectly fine. Read this article => [Local Domain Names with SSL for development applications ](https://thepro.io/post/local-domain-names-with-ssl-for-local-development-applications-LG)
- Following application properties can edited to customize for your needs. Sample @ [application.yaml](src/main/resources/)

### Role Management

- Roles can be added through `SecurityRoleService` during registeration of user or manually managed by Super admins
- Super Admins are defined through application property `security.super-admins`
- With roles interated with spring security, spring authorization annotations like **`@Secured, @RolesAllowed, @PreAuthorize, @PostAuthorized`** etc will work out of the box.
- I personally like to define per role annotations like **`@IsSuper, @IsSeller`** etc for the sake of simplicity.

```
    @GetMapping("data")
	@isSeller
	public String getProtectedData() {
		return "You have accessed seller only data from spring boot";
	}
```

- UI useAuth hook also has utility properties like **_ `roles, hasRole, isSuper, isSeller `_** properties exposed accross the application to allow or restrict access to specific UI components

## Related Tutorials :

- [Firebase Authentication for Spring Boot Rest API](https://thepro.io/post/firebase-authentication-for-spring-boot-rest-api-5V)
- [Firebase with Spring Boot for Kubernetes Deployment Configuration](https://thepro.io/post/firebase-with-spring-boot-kubernetes-deployment-configuration-RA)
- [Local Domain Names with SSL for development applications ](https://thepro.io/post/local-domain-names-with-ssl-for-local-development-applications-LG)

## Environment vars
GOOGLE_APPLICATION_CREDENTIALS=car-rental.json;SUPER_ADMINS=mac12llm2@gmail.com

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
