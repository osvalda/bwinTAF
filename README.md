# bwinTAF
bwin test automation example

# Requirements
* java 11+
* optional local gradle installation v8+

# Precondition settings
* Add environment variable:
```
ENV=develop
```
* Add optional environment variable:
```
browser=chrome
```

## possible browser options
* chrome
* chrome-headless (this is the default)
* chrome-mobile
* firefox
* firefox-headless
* safari

# Build
```
./gradlew clean build -x test
```
# Run tests
```
./gradlew testRegression
```
# Generate and deploy reports
```
./gradlew allureReport
./gradlew allureServe
```
