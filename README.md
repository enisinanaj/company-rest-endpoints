# RESTful API with Spring MVC

[![CircleCI](https://circleci.com/gh/enisinanaj/company-rest-endpoints/tree/master.svg?style=svg)](https://circleci.com/gh/enisinanaj/company-rest-endpoints/tree/master) [![codecov](https://codecov.io/gh/enisinanaj/company-rest-endpoints/branch/master/graph/badge.svg)](https://codecov.io/gh/enisinanaj/company-rest-endpoints)


This project is an example implementation of a RESTful API in Java using the Spring MVC framework. 

## Getting Started

Clone this project and run the application with gradle, using gradle wrapper:

```
gradlew bootRun
```

or your own gradle installation

```
gradlew bootRun
```

### Prerequisites

You'll need Java JDK 1.8+ installed. Gradle is not necessary because gradlew wrapper is provided with the project as described above.

To check for bugs and be able to browse them locally you have to download Spotbugs from http://spotbugs.readthedocs.io/en/latest/installing.html. Download it in zip format, then launch the spotbugs executable that is found inside the _bin_ folder. Once the application is running, you can go to `File > Open` and select the findbugs report file (.xml) generally inside _${project_root}/build/reports/spotbugs/main.xml_


### Available endpoints

`POST /companies` to create a new company

```
## Create a new company
curl -X "POST" "http://localhost:8080/companies" \
     -H 'Content-Type: application/json' \
     -d $'{
  "email": "info@apple.com",
  "phone": "+1 333 555 8294",
  "country": "USA",
  "name": "Apple, INC",
  "city": "Coupertino",
  "address": "One Infinite Loop"
}'
```

the expected HTTP result is as follows

```
HTTP/1.1 201 
Location: http://localhost:8080/companies/17
Content-Length: 0
Date: Sun, 17 Jun 2018 00:46:52 GMT
Connection: close
```

returning a **Location** header that points at the newly created resource.

`GET /companies` to obtain a list of all companies present in the database

```
## Get all companies
curl "http://localhost:8080/companies"
```

returns a json array of companies

```
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 17 Jun 2018 00:52:01 GMT
Connection: close

[
    {
        "id": 17,
        "name": "Apple, INC",
        "address": "One Infinite Loop",
        "city": "Coupertino",
        "country": "USA",
        "email": "info@apple.com",
        "phone": "+1 333 555 8294",
        "benificialOwners": []
    }
]
```

`GET /companies/{companyId}` to obtain the information regarding a single company. Example `GET /companies/17` as below

```
## Getting all data of company 17
curl "http://localhost:8080/companies/17"
```

Result:

```
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 17 Jun 2018 00:56:13 GMT
Connection: close

{
    "id": 17,
    "name": "Apple, INC",
    "address": "One Infinite Loop",
    "city": "Coupertino",
    "country": "USA",
    "email": "info@apple.com",
    "phone": "+1 333 555 8294",
    "benificialOwners": []
}
```

## Running the tests

The tests are run easily with the gradle task

```
gradlew test
```

For a better reporting there is another gradle task available which runs the tests and aferwards ccreates the reports in `jacoco` xml format. This task also runs `spotbugs`. To have a browsable report from `Jacoco` you need to edit the build file `build.gradle` from this

```
jacocoTestReport {
	reports {
		xml.enabled true
		csv.enabled false
		html.enabled = false
		//html.destination file("${buildDir}/jacocoHtml")
	}
}
```

to this

```
jacocoTestReport {
	reports {
		xml.enabled false
		csv.enabled false
		html.enabled = true
	}
}
```

To have tests run and reports generated, execute the following gradle task:

```
gradlew check
```

### Break down into end to end tests

`TODO`

### And coding style tests

`TODO`

## Deployment

`TODO`

## Built With

* [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) - The web framework used
* [Gradle](https://gradle.org) - Dependency Management

## Authors

* **Eni Sinanaj** - *Initial work*

## License

This project is licensed under the GPL 3.0 - see the [LICENSE.md](LICENSE.md) file for details