# CarRental Monorepo

## What is CarRental?

Transport oriented e-commerce website. It will have two parts, a user-oriented website, and an admin/company owner
website.

When it comes to hire a luxury car with a driver, usually you need to contact with the company to get the prices,
availability… The user-oriented website will facilitate the retrieval of such information. On the other hand, the admin
website, will have different features to automatize all the process since a transfer is scheduled, until it has been
done and paid.

The project uses a distributed architecture, with microservices, and using Kubernetes as the backbone. This approach, as
later explained, will facilitate the load balancing, scalability, and deployment of the services. The deployment will be
done in a cloud computing service, to avoid having to maintain physical servers and reduce the time required to go live.

## Monorepo

In this repository, all the different submodules of the project are being tracked.

Each of them, has its own README.md file, with an explanation of what its purpose is, how it works and the process of
implementing it.

## Frontend

There are two frontends, the admin frontend and the user frontend.

### User frontend

Normal company website, where their services are offered, and contact forms plus reviews are shown. In the case of the
user frontend,it is a rather simple design, as most of its content will be static.

### Admin frontend

Frontend for the company, where the services, calendar, clients, revenue... are shown.

#### Technology used

Bootstrap is an open-source CSS framework. It has many templates for many types of interface components. Bootstrap
strives to simplify the development of information web pages. The main purpose of adding it to a web project is to apply
the color, size, font, and layout chosen by Bootstrap to the project. Once added to the project, Bootstrap will provide
basic style definitions for all HTML elements. The result is a uniform appearance of cross-browser prose, tables, and
form elements.

SweetAlerts is a library I use, to create more complex and "beautiful" alerts in the frontend.

All the logic required, which are mainly API calls to the backend are done with Javascript.

FullCalendar is the library that creates the calendar, with some integrations to Google Calendar.

Firebase/Firestore libraries are used to retrieve the basic user data, allow the login with email/Google, and
authenticate the requests to the backend.

#### Hosting

GitHub Pages is a static site hosting service that can host HTML, CSS, and JavaScript files directly from the repository
on GitHub, and you can also choose to run these files during the build process and publish the website. It is completely
free and can be linked to a custom domain.

It automatically hosts it in HTTPS, which then forces you to use an HTTPS hosted backend, because if not the CORS (cross
origin requests) would fail.

## Backend

Each microservice and service (DB, Kubernetes, Scripts...) is explained inside its own folder of this repository.

- [API-GW](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/api-gateway)
- [Calendar](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/calendar)
- [Covid](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/covid)
- [Database](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/database)
- [Email](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/email)
- [Firebase](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/firebase)
- [Kubernetes](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/k8)
- [Legal](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/legal)
- [Postman](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/postman)
- [RabbitMQ](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/rabbitmq)
- [Scripts](https://github.com/marcllort/Car_Rental_Monorepo/tree/master/scripts)