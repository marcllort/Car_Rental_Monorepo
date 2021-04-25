# Scripts

In this folder there are multiple scripts to automate the different processes needed in the application.

They can be classified by CI/CD, Docker and Kubernetes:

## CI/CD

There are two repositories, one where the COVID-19 project is maintained and the monorepo. In the case of the monorepo,
the CI/CD builds all the microservices, running the tests.

For the COVID-19 repo, there is a more complex flow taking place:

There are [two pipelines](https://github.com/marcllort/CanITravelTo_Backend/actions), the
CI ([ci.yml](https://github.com/marcllort/CanITravelTo_Backend/blob/master/.github/workflows/ci.yml)) and
CD ([cd.yml](https://github.com/marcllort/CanITravelTo_Backend/blob/master/.github/workflows/cd.yml)).

In the CI pipeline has the following steps implemented: build the two microservices, run the unit and integration/E2E
tests of each microservice. If it fails it will notify me through an email.

The Unit tests are done with the vanilla golang test methodology, similar to JUnit. The E2E tests, are a collection of
Postman calls/tests that are being **run in the pipeline with Newman** (cli version of Postman). The tests are written
in Javascript, here there is a simple example:

```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("No error messages", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.error).to.eql("");
});

pm.test("Origin/Destination Correct", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.origin).to.eql("France");
    pm.expect(jsonData.destination).to.eql("Spain");
});
```

In the CD pipeline, if the commit is in the master branch, and it's a commit marked as a release, it will decrypt the
Credentials needed to build each microservice image (explained in the next [section](#ssh-from-pipeline)),
**build and upload the new Docker images** (to Github's docker registry), and then SSH into the EC2 instance to stop the
old docker images and start the updated images with the latest changes.

Another solution, could be building the docker images in the server instead of doing it directly in the pipeline. This
would allow me to just have the Credentials in my server, and avoid uploading the encrypted version to Github. In case
this was a long-term project, or a business it would probably be better to keep the Creds only on your server, but
again, the Repository would be private, so the security shouldn't be a big problem... Make your choice!
I decided to use both solutions, the building of the image in the pipeline, and the deployment through an SSH script.

If we are in another branch (other than master) it will only run the CI pipeline (build and unit/integration tests).

## Docker

- docker-build.sh: Script to automatize the creation of the image with the necessary tag, and its upload to the
  registry.
  - `docker-build.sh SERVICE`: Where service is the name of the microservice (i.e: calendar). Will build the image and
    publish it to the repository.

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
    with the parameter `start local-rabbit` it will also port-forward the rabbitMQ deployment (5672).
  - `local-development.sh stop`: Stops all the proxies and port-forwards running.
  - `local-development.sh token`: Shows the token needed to login in the Kubernetes dashboard.