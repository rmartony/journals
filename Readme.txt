Considerations

- Prerequisites remain unchanged except for spring-boot-starter-mail and javax.mail maven dependencies that were added for sending email messages.
- No database changes were performed.

- Before running application please edit "application.properties" to set up SMTP host, username and password.

- I assume loginName attribute in User class holds the user's email.
Another option would be to add an email attribute to the User class.

- The daily digest is sent once only per day using a schedule service, with all the new journals info, and only when there is any new journal added for the day.
