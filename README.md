![alt tag](https://github.com/kidinov/Just-Weather/blob/master/screenshots/promo.png)

# Just Weather
Easy in use app for get track of current weather in different cities arround the world. 

<img src="https://github.com/kidinov/Just-Weather/blob/master/screenshots/1.png" width="256">  <img src="https://github.com/kidinov/Just-Weather/blob/master/screenshots/2.png" width="256">  <img src="https://github.com/kidinov/Just-Weather/blob/master/screenshots/3.png" width="256">

Find out it on [Google Play](https://play.google.com/store/apps/details?id=org.kidinov.just_weather)

Code archetecture follow MVP with RxJava2, Dagger2, Retrofit2 and Realm.

To control code quality:
```
./gradlew test  - To run JVM tests
./gradlew connectedAndroidTest - To run instrumentation tests. (don't forgot to disable animations in order to make it possible to run UI tests)
./gradlew pmd - To check code with pwd
./gradlew findbugs - To check code with findbugs
./gradlew checkstyle - To check code with checkstyle
./gradlew check - To run everything above + android lint
```

# Usage restriction
This sources mostly for educational porpuse. You can use any part of that source as you wish, but you not able to build this app (cutting something) and publish as your application.

Thanks.


