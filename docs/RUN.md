# R*bert UI

## How to Run with Gradle

Open a terminal shell and execute

```bash
./gradlew bootRun -Dspring.profiles.active=default,local -Dvaadin.productionMode=true
```
> Will run the application and automatically open the user interface in your favorite browser on port 8081.  If you want to run this application on the default port, 8080, then you can remove `-Dspring.profiles.active=default,local` above.

### targeting an instance of R*bert

By default the application is configured to interact with an instance of R*bert running on http://localhost:8080.

If you want to override this default, then execute with this additional command-line argument

```bash
./gradlew bootRun -DrobertUrl=https://robert.apps.dhaka.cf-app.com
```
> Replace the value after `-DrobertUrl=` above with the URL where your R*bert application instance is hosted.
