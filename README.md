# RESTful API with Spring MVC

[![CircleCI](https://circleci.com/gh/enisinanaj/company-rest-endpoints/tree/master.svg?style=svg)](https://circleci.com/gh/enisinanaj/company-rest-endpoints/tree/master) [![codecov](https://codecov.io/gh/enisinanaj/company-rest-endpoints/branch/master/graph/badge.svg)](https://codecov.io/gh/enisinanaj/company-rest-endpoints) [![Heroku](https://heroku-badge.herokuapp.com/?app=restful-cmp&root=companies)](https://restful-cmp.herokuapp.com/companies)


This project is an example implementation of a RESTful API in Java using the Spring MVC framework. 

## Getting Started

Clone this project and run the application with gradle, using gradle wrapper:

```
gradlew bootRun
```

or your own gradle installation

```
gradle bootRun
```

### Prerequisites

You'll need Java JDK 1.8+ installed. Gradle is not necessary because gradlew wrapper is provided with the project as described above.

To check for bugs and be able to browse them locally you have to download Spotbugs from http://spotbugs.readthedocs.io/en/latest/installing.html. Download it in zip format, then launch the spotbugs executable that is found inside the _bin_ folder. Once the application is running, you can go to `File > Open` and select the findbugs report file (.xml) generally inside _${project_root}/build/reports/spotbugs/main.xml_


## Available endpoints

### Companies (`/companies`)

### `POST /companies` to create a new company

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

### `GET /companies` to obtain a list of all companies present in the database

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
        "beneficialOwners": []
    }
]
```

### `GET /companies/{companyId}` to obtain the information regarding a single company. Example `GET /companies/17` as below

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
    "beneficialOwners": []
}
```

> Note that if the given company ID does not exist an HTTP NotFound error will be returned with a 404 status code.

### `PATCH /companies/{companyId}` can be used to update one or more fields of the company resource.
Curl call example:

```
## Update company fields
curl -X "PATCH" "http://localhost:8080/companies/1" \
     -H 'Content-Type: application/json' \
     -d $'{
  "email": "info@one.com",
  "phone": "+1 333 555 8294",
  "country": "USA",
  "name": "One",
  "city": "Loop",
  "address": "Infinite"
}'
```

Response:

```
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 17 Jun 2018 01:40:08 GMT
Connection: close

{
    "id": 1,
    "name": "One",
    "address": "Infinite",
    "city": "Loop",
    "country": "USA",
    "email": "info@one.com",
    "phone": "+1 333 555 8294",
    "beneficialOwners": [
        {
            "id": 2,
            "name": "jhoeller"
        }
    ]
}
```

> Note that if the given company ID does not exist an HTTP NotFound error will be returned with a 404 status code.

### Beneficial owners of a company (`/companies/{companyId}/beneficialOwners`)

The provided methods for these resources are GET allo beneficial owners of a company and POST (create) a new beneficial owner.

### `POST /companies/{companyId}/beneficialOwners` to create a new Beneficial owner

```
## Create a new beneficial owner for the given company resource
curl -X "POST" "http://localhost:8080/companies/17/beneficialOwners" \
     -H 'Content-Type: application/json' \
     -d $'{
  "name": "Beneficial"
}'
```

Response from this call is as follows:

```
HTTP/1.1 201 
Location: http://localhost:8080/companies/17/beneficialOwners/18
Content-Length: 0
Date: Sun, 17 Jun 2018 22:15:40 GMT
Connection: close
```

### `GET /companies/{companyId}/beneficialOwners` to obtain all beneficial owners of the given company

```
## Create a new company
curl "http://localhost:8080/companies/17/beneficialOwners" \
     -H 'Content-Type: application/json'
```

Responds with the following array of object

```
HTTP/1.1 200 
Content-Type: application/json;charset=UTF-8
Transfer-Encoding: chunked
Date: Sun, 17 Jun 2018 22:22:12 GMT
Connection: close

[{"id":18,"name":"Beneficial"}]
```

> Note that if the given company ID does not exist an HTTP NotFound error will be returned with a 404 status code.

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

## Deployment

Application is deployed to Heroku at https://restful-cmp.herokuapp.com/companies

## Authentication

A suggested authentication method between client and this backend server would be two-legged OAuth 2.0.

_As described in this image_
![2-legged OAuth 2.0](http://codehustler.org/wp-content/uploads/2014/06/2_legged_oauth_1.png)

## Built With

* [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html) - The web framework used
* [Gradle](https://gradle.org) - Dependency Management
* [JUnit 4.12](https://junit.org/junit4/) - Testing Framework
* [JaCoCo](https://www.jacoco.org/jacoco/trunk/index.html) - Java Code Coverage
* [CodeCov](https://codecov.io/) - JaCoCo integration tool for GitHub
* [CircleCI](https://circleci.com) - Continuous Integration for GitHub

## Authors

* **Eni Sinanaj** - *Initial work*

## License

This project is licensed under the GPL 3.0 - see the [LICENSE.md](LICENSE.md) file for details
