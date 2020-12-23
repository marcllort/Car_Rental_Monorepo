# CarRental Monorepo

## What is CarRental?

A transport oriented e-commerce website. It will have two parts, a user-oriented website, and an admin/company
owner website.

When it comes to hire a luxury car with a driver, usually you need to contact with the company to get the prices,
availability… The user-oriented website will facilitate the retrieval of such information. On the other hand, the admin
website, will have different features to automatize all the process since a transfer is scheduled, until it has been
done and paid. 

The project uses a distributed architecture, with microservices, and using Kubernetes as the backbone.
This approach, as later explained, will facilitate the load balancing, scalability, and deployment of the services. The
deployment will be done in a cloud computing service, to avoid having to maintain physical servers and reduce the time
required to go live.

## Monorepo

In this repository, all the different submodules of the project are being tracked.

Each of them, has its own README.md file, with an explanation of what its purpose is, how it works and the process of
implementing it.