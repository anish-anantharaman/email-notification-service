package com.anish.email.util.swagger;

public class Swagger {

    public static final class SwaggerExampleResponses {

        private SwaggerExampleResponses() {}

        public static final String ACCEPTED = """
                {
                    "statusCode": 202,
                    "statusMessage": "Accepted",
                    "message": "Notification queued successfully",
                    "data": true
                }
                """;

        public static final String BAD_REQUEST = """
                {
                    "statusCode": 400,
                    "statusMessage": "Bad Request",
                    "message": "subject : must not be blank;",
                    "data": false
                }
                """;

        public static final String INTERNAL_SERVER_ERROR = """
                        {
                          "statusCode": 500,
                          "statusMessage": "Internal Server Error",
                          "message": "Failed to queue the notification",
                          "data": false
                        }
                """;
    }
}
