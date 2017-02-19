Considerations

- Prerequisites remain unchanged except for spring-boot-starter-mail and javax.mail maven dependencies that were added for sending email messages.
- Database has been changed, users have now email address attribute.

- Before running application please edit "application.properties" to set up SMTP host, username and password.

- The daily digest is sent once only per day using a schedule service, with all the new journals info, and only when there is any new journal added for the day.
