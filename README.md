# iot-temp-mgmt-uk-biobank-test

IoT temperature Management UK Bio-bank interview test

This project is a Spring Boot application that provides APIs for managing IoT temperature data.

## API Documentation

The API documentation is available at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

## Running the Application

To run the application, execute the following command:

```
./gradlew bootRun
```

The application will start on port 8080.

## Testing the Application

To test the application, execute the following command:

```
./gradlew test
```

The test results will be displayed in the console.

## Deployment

The application can be deployed to a cloud provider of your choice.

## Docker

The application can be deployed to a Docker container.

To build the Docker image, execute the following command:

```
./gradlew bootBuildImage
```

The Docker image will be created in the `build/docker` directory.

To run the Docker container, execute the following command:

```
docker run -p 8080:8080 temp_mgmt_backend_image
```

The application will start on port 8080.

## Kubernetes

The application can be deployed to a Kubernetes cluster.

To deploy the application to a Kubernetes cluster, execute the following command:

```
./gradlew bootBuildImage
docker build -t bashlaw/temp_mgmt_backend_image:latest .
kubectl apply -f env-config.yaml
kubectl apply -f env-secret.yaml
kubectl create -f deployment.yaml
kubectl create -f service.yaml
```

The application will be deployed to the Kubernetes cluster.
