# Cities

## About App

This is a "City" CRUD RESTful Web Service with Spring Boot/Hibernate H2 in memory database. 

You can - 

* **browse through the paginated list of cities with the corresponding photos**
```
GET - http://localhost:8080/cities?pageNumber=0&pageSize=50
 ```

* **search by the name**
```
GET - http://localhost:8080/cities/{cityname} i.e. http://localhost:8080/cities/Tokyo
 ```


* **edit the city (both name and photo)**
```
PUT - http://localhost:8080/cities/{cityid} i.e. http://localhost:8080/cities/1
```
```JSON
{
    "name": "Tokyo new",
    "photo": "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4c/Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg/500px-Enseada_de_Botafogo_e_P%C3%A3o_de_A%C3%A7%C3%BAcar.jpg"
}
```


### Spec -
------
* Accepts JSON 
* Response in JSON 
* Java 17
* Build with Maven
* Data storage: (in memory) database
* Lombok has been used to reduce boilerplate code


### Running
Run as a Spring Boot App
