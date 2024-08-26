# R*bert UI

## How to Run with Gradle

Open a terminal shell and execute

```bash
./gradlew bootRun -Dspring.profiles.active=default,local -Dvaadin.productionMode=true
```
> Will run the application and automatically open the user interface in your favorite browser on port 8081.  If you want to run this application on the default port, 8080, then you can remove `-Dspring.profiles.active=default,local` above.
