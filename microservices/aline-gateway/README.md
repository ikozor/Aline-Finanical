# Aline API Gateway

This server is a gateway that routes specific requests specified in the
configurations to a microservice.

### Starting Up The Server

The service does not require and special configurations to run for the first 
time. There is a `dev` profile available for immediate use with the current
microservices that are available. Feel free to update that configuration as
needed per your feature branch so that it may be merged when your 
microservice is ready for use.

Properties that are recommended to be set are the following:

```
app:
    gateway:
        service-host: http://localhost
        path-prefix: /api
        routes: # list of routes
        portal-origins: # list of front-end origins
```

Lets breakdown these properties:
- `app.gateway`: This is the configuration property prefix.
- `service-host`: This is the url of where the microservices will be hosted. The ports will be specified individually. _**Leave the `service host` uri without port definitions are other paths.**_
- `path-prefix`: This is the prefix of the API to use. It could be `/api` or `/api/v2`. _**There should not be a trailing `/` at the end of the prefix.**_
- `routes`: A list of routes that map to an added microservice.
- `portal-origins`: A list of allowed front-end origins.

### Adding Your Microservice

Adding your microservice is actually quite easy compared to previous
orchestration methods this project has used.

The property `app.gateway.routes` holds a list of routes that specify
the pattern of the incoming request as well as the uri of the service
the request is being sent to.

It is recommended that `yaml` is used instead the default `.properties`
file for this. 

Find the `routes` property. The property should be nested within `app.gateway`:

```
app:
    gateway:
        routes: # List of routes
```

Under the `routes` property a list of `route` objects are stored.

A route object consists of the following properties:

```
    id: user # String name of your microservice
    port: 8070 # Port of the service (This is appended to a global property.
    paths:
        - @/users
        - @/users/{segment}
        - @/login
        - /login
```

Let's break down that schema.

- `id`: The ID property is the name of your microservice. In this case, the ID is user for the 'user microservice.'
- `port`: This property is appended to the global property `serviceHost`. It represents the port your service is running on.
- `paths`: This is a string array of path patterns specified by the gateway.
  - Please see the patterns available in the Spring Cloud Gateway documentation.
  - An extra symbol that had been added was the `@` symbol which is replaced by the global property `path-prefix`
