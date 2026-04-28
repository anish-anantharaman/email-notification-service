# Email Notification Service

## Overview

Email Notification Service is a lightweight, asynchronous service designed to send email notifications to multiple 
recipients reliably and at scale. It enables microservices to trigger email delivery without handling email 
infrastructure directly, promoting loose coupling and reusability across systems. It is designed to be easily 
integrated into existing microservices architectures with little or no modification.

Built with Spring Boot, the service provides strongly typed APIs with validation, consistent error handling, and 
automatic API documentation using Springdoc OpenAPI (Swagger UI).
## Features

- Supports plain text emails, attachments, and template-driven email notifications. 
- Strongly typed request/response models with validation and consistent error handling. 
- Built-in Swagger UI for interactive API exploration and testing.

<br/>
<br/>

## Getting Started

### 1. Create Email Service Credentials

Before running the service, you must configure valid SMTP credentials. These credentials are required for 
authentication with the email provider (e.g., AWS SES, Gmail SMTP or any SMTP server).

By default, the configuration is pre-set for AWS SES SMTP endpoints, but it can be overridden via environment variables.

You must provide valid SMTP credentials for your chosen provider.

Required values:

- MAIL_USERNAME → SMTP username
- MAIL_PASSWORD → SMTP password
- MAIL_FROM → Verified sender email address
- MAIL_ALIAS → Display name for sender (optional but recommended)


### 2. Running Locally

#### Prerequisites

- Java 25
- Maven 3.9+
- SMTP credentials

#### Configuration

Before running the application, set the environment variables for SMTP

```bash
export MAIL_FROM=your_verified_sender@example.com
export MAIL_ALIAS=Email Service
export MAIL_USERNAME=your_smtp_username
export MAIL_PASSWORD=your_smtp_password
```

#### Steps

```bash
# Clone the repo
git clone -b main https://github.com/anish-anantharaman/email-notification-service.git

# Build the project
mvn clean install

# Run the unit and integration tests (Optional)
> Currently, the application does not include any automated tests.  
> You can add your own unit or integration tests as needed, and then run:
mvn test

# Run the application
mvn spring-boot:run
```

### 3. Running with Docker (Optional)

For a quick start without building locally, you can use Docker. No tests are included in this Docker build.

```bash
# Build Docker image
docker build -t email-notification-service .

# Run the container with the environment variables
docker run \
  -e MAIL_FROM="your-verified-sender@example.com" \
  -e MAIL_ALIAS="Tech Support" \
  -e MAIL_USERNAME="your-smtp-username" \
  -e MAIL_PASSWORD="your-smtp-password" \
  -p 8080:8080 \
  email-notification-service
```

This is useful if you want to start the service quickly without installing Java or Maven locally.

### 4. Sending a Test Email

Once the service is running, you can send a test notification using the curl command below:

```bash
curl --location 'http://localhost:8080/api/v1/emails/plain' \
--header 'Content-Type: application/json' \
--data-raw '{
"emails": [
"anish.pgt@gmail.com"
],
"subject": "Server Down",
"content": "The production server is down since 2 PM. "
}'
```

### 5. Swagger API Documentation

Once the application is running, open your browser to:

http://localhost:8080/swagger-ui/index.html
<br/>
<br/>

## Contributing

This project is open for suggestions, improvements, and PRs.
Feel free to fork the repo, make changes, and submit a PR. Your contributions are welcome!