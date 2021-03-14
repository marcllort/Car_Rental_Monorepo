## Sample @ [application.properties](src/main/resources/application.yaml)

| Properties                                             | Description                                                                                                                                                                 | DataType |
| ------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------- |
| `security.firebase-props.database-url`                 | Firebase Database URL found in Firebase Web SDK config                                                                                                                      | String   |
| `security.firebase-props.enable-strict-server-session` | server will only look for session cookie to verify request                                                                                                                  | Boolean  |
| `security.firebase-props.enable-check-session-revoked` | will check if firebase session was revoked elsewhere, this will also add overhead of few seconds to each request. Applicable only if `enable-strict-server-session` enabled | Boolean  |
| `security.firebase-props.enable-logout-everywhere`     | firebase will revoke refresh tokens everywhere. Applicable only if `enable-strict-server-session` enabled                                                                   | Boolean  |
| `security.firebase-props.session-expiry-in-days`       | Expiration time for long lived session. Applicable only if `enable-strict-server-session` enabled                                                                           | Integer  |
| `security.cookie-props.max-age-in-minutes`             | Default Cookie expiration time.                                                                                                                                             | Integer  |
| `security.cookie-props.http-only`                      | Cookies will not be accessible to client side scripts.                                                                                                                      | Boolean  |
| `security.cookie-props.secure`                         | Cookies will be sent only over secure https channel                                                                                                                         | Boolean  |
| `security.cookie-props.domain`                         | Cookies will only be available on provided domain eg:- "demo.dev"                                                                                                           | String   |
| `security.cookie-props.path`                           | Cookies will only available on provided path. Path "/" will allow access from any page.                                                                                     | String   |
| `security.allow-credentials`                           | Lets client know that server accepts cookies and other credentials from `security.allowed-origins`.                                                                         | String   |
| `security.allowed-origins`                             | An array of allowed cross origin domain names eg:- https://demo.dev.                                                                                                        | Array    |
| `security.allowed-methods`                             | An array of HTTP methods server will accept                                                                                                                                 | Array    |
| `security.allowed-headers`                             | An array of HTTP headers server will accept                                                                                                                                 | Array    |
| `security.allowed-public-apis`                         | An array of rest path on server which can be publicaly accessible. path can be wildcard ie. `/public/*` will accept `/public/path1,/public/path2`                           | Array    |
| `security.exposed-headers`                             | An array of exposed headers, this is required only if CSRF tokens are generated by the server                                                                               | Array    |
| `valid-application-roles:`                             | Valid application roles, Add or remove roles. Roles must be of format ROLE\_`ROLENAME`                                                                                      | Array    |
| `security.super-admins:`                               | An array of user email id's to be designated as super admins                                                                                                                | Array    |