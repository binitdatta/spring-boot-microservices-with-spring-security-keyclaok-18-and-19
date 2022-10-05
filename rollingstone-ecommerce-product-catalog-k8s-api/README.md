# Product Catalog Microservice

## Salient Features and Differences of the Product Microservice 

## Similarities in the build.gradle

``` 
- 1. 'org.springframework.boot:spring-boot-starter-actuator'
- 2. 'org.springframework.boot:spring-boot-starter-data-jpa'
- 3. 'org.springframework.boot:spring-boot-starter-web'
- 4. 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
- 5. 'org.springframework.boot:spring-boot-starter-aop'
- 6. 'com.fasterxml.jackson.core:jackson-databind'
- 7. 'javax.xml.bind:jaxb-api:2.3.1'
- 8. 'springdoc-openapi-ui'
- 9. 'mysql:mysql-connector-java'
- 10. 'org.springframework.boot:spring-boot-starter-test'
 
```
## Differences

``` 
- 1.  'org.springframework.boot:spring-boot-starter-oauth2-client'
- 2.  'org.springframework.security:spring-security-oauth2-jose'
- 3.  'org.springframework.boot:spring-boot-starter-security'
- 4.  'org.apache.httpcomponents:httpclient'
```
## Similarities in src/main/java

``` 
- 1. Has the same package structure from the Category Microservice
- 2. Uses the Same Aspect Oriented Program Class RestControllerAspect.java
- 3. Uses the same Actuator Metrics Configuration Class with a different name ProductMetricsConfiguration
- 4. Uses the same Swagger API Config Class with a different name ProductSwaggerconfig
- 5. Follows the same Event driven mechanism with ProductEvent and ProductEventListener
- 6. Uses the same Exception Classes
- 7. Uses the Same JPA Dao interfaces with a different Model Product.java
- 8. Uses the same Service Interface and Service Implementation Mostly ***
- 9. Uses the same AbstractController and AOP Annotations
- 10. Uses the same Spring Boot MVC Annotations like the CategoryController
```

## Differences in src/main/java

``` 
- 1. This Product Microservice is a OAuth2 ResourceServer as well as an OAuth2 Client
- 2. The Category Microservice was only a OAuth2 ResourceServer
- 3. We did not have the Spring Security Starter Dependency in the Category Microservice
- 4. We also did not have the OAuth2 Client dependency in the Category Microservice
- 5. Lets see the difference in Code one by one
```

## First

``` 
package com.rollingstone.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .csrf()
                .disable()
                .oauth2ResourceServer()
                .jwt();
    }
}
``` 
## Second Difference The createProduct API method of the ProductController

``` 
/*---Add new Product---*/

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@PostMapping("/product")
public ResponseEntity<?> createProduct(@RequestBody Product product,
@AuthenticationPrincipal OidcUser principal) {
Product savedProduct = productService.save(product, principal);
    ProductEvent productCreatedEvent = new ProductEvent("One Product is created", savedProduct);
    eventPublisher.publishEvent(productCreatedEvent);
    return ResponseEntity.ok().body("New Product has been saved with ID:" + savedProduct.getId());
}
```

## Third Difference The ProductService Interface
``` 
package com.rollingstone.spring.service;

import com.rollingstone.spring.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.List;
import java.util.Optional;

public interface ProductService {

   Product save(Product product, @AuthenticationPrincipal OidcUser principal);
   Optional<Product> get(long id);
   Page<Product> getProductsByPage(Integer pageNumber, Integer pageSize);
   List<Product> getAllProducts();
   void update(long id, Product product);
   void delete(long id);
}
```
## Fourth Difference the save method of the ProductServiceImpl class
``` 
@Override
	public Product save(Product product, @AuthenticationPrincipal OidcUser principal) {

		Category category = null;
		Category parentCategory = null;
		String URI = CATEGORY_SERVICE_HOST + ":" + CATEGORY_SERVICE_PORT + CATEGORY_REQUEST_PATH;
		
		if (product.getCategory() == null) {
			logger.info("Product Category is null :");
			throw new HTTP400Exception("Bad Request as Category Can not be empty");
		} else {
			logger.info("Product Category is not null :" + product.getCategory());
			logger.info("Product Category is not null ID :" + product.getCategory().getId());
		}
		if (product.getParentCategory() == null) {
			logger.info("Product Parent Category is null :");
			throw new HTTP400Exception("Bad Request as Parent Category Can not be empty");
		} else {
			logger.info("Product Parent Category is not null :" + product.getParentCategory());
			logger.info("Product Parent Category is not null Id :" + product.getParentCategory().getId());
		}

		logger.info("request port :"+CATEGORY_SERVICE_PORT);
		logger.info("request host :"+CATEGORY_SERVICE_HOST);
		logger.info("request path :"+ CATEGORY_REQUEST_PATH);

		logger.info("request uri :"+REQUEST_URI);
		logger.info(" uri :"+URI);

		logger.info("request uri modified :"+CATEGORY_SERVICE_HOST + ":" + CATEGORY_SERVICE_PORT + CATEGORY_REQUEST_PATH);
		try{
			Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("Authorization", "Bearer " + jwt.getTokenValue());
			HttpEntity<Category> entity = new HttpEntity<>(httpHeaders);

			String categoryUri = URI+Long.toString(product.getCategory().getId());
			logger.info(" categoryUri :"+categoryUri);
			ResponseEntity<Category> categoryEntity = restTemplate.exchange(categoryUri, HttpMethod.GET, entity, new ParameterizedTypeReference<Category>() {});

			if (categoryEntity != null ) {
				Category validCategory = categoryEntity.getBody();

				if (validCategory == null){
					logger.info("Product  Category is invalid :");
					throw new HTTP400Exception("Bad Request as  Category Can not be invalid");
				}
			}
		}
		catch(Exception e){
			logger.info("Product  Category is invalid :");
			logger.info("Product  Category is invalid :"+e.getMessage());
			throw new HTTP400Exception("Bad Request as  Category Can not be invalid");
		}
		return productDao.save(product);
	}
```

## How to test this Product Microservice

## First build the application using while staying at the root of the application

``` 
./gradlew clean build
```

## Second Run the application using

``` 
java -jar build/libs/rollingstone-ecommerce-product-catalog-k8s-api-1.0.jar
```

## Authorization Code Flow

## Getting An Authorization Code

``` 
- 1. Open a new tab in Postman and enter the following
- 2. Method as GET
- 3. Endpoint URL : http://localhost:8080/auth/realms/binitdattarealm/protocol/openid-connect/auth
- 4. Request Paramaters 
- 5. client_id=postman-demo-client
- 6. response_type=code
- 7. state=adhcd382yjjw4
- 8. redirect_uri=http://localhost:8092/callback
- 9. scope=openid
```
### Image for the Postman tab 
![Alt text](images/1.png?raw=true "Title")

### Then copy the Full Request URL

``` 
http://localhost:8080/auth/realms/binitdattarealm/protocol/openid-connect/auth?client_id=postman-demo-client&response_type=code&state=adhcd382yjjw4&redirect_uri=http://localhost:8092/callback&scope=openid
```
### Open a browser tab and enter the above URL and press enter

![Alt text](images/11.png?raw=true "Title")

### If KeyCloak asks you to login, loging using the user you create for the realm binitdattarealm (or your realm)
### Copy the code in the response url
### Getting an Access Token

``` 
- 1. Open another postman tab
- 2. Method : POST
- 3. URL : http://localhost:8080/auth/realms/binitdattarealm/protocol/openid-connect/token
- 4. Body : x-www-form-urlencoded
- 5. grant_type:authorization_code
- 6. client_id:postman-demo-client
- 7. client_secret: wGMZfPimfqeGo3fCf0H4XU2wk91L3W1x (yours would be different)
- 8. code: 611872d4-2aec-404d-9b86-00d48ad71d24.e9af4757-c80a-4612-9a39-8825ffeaef7e.8f8e73f3-6535-47e7-bf5c-022aa26be5c9
- 9. Enter the code that you collected from above not #8
- 10. redirect_uri:http://localhost:8092/callback
```
### Postman Image for the above and after clicking Send

![Alt text](images/2.png?raw=true "Title")


### Copy the Access Token and

```
- 1. Open a new postman tab
- 2. Navigate the Headers tab
- 3. Enter the method as GET
- 4. Enter the url as http://localhost:8081/product
- 5. Enter the first header as Authorization as Key
- 6. Bearer as value followed by the access token
- 7. Accept as application/json
- 8. Content-Type as application/json
- 9. Hit Send
```

![Alt text](images/3.png?raw=true "Title")

### Verify the Results of the GET Method

![Alt text](images/4.png?raw=true "Title")

### Get a specific Product using a Path Variable

![Alt text](images/5.png?raw=true "Title")

### Create a new Product

``` 
- 1. Open a new postman tab
- 2. Copy the Product Payload shown below
- 3. Change method as POST
- 4. URL as http://localhost:8081/product
- 5. Choose Body as Raw and enter the json payload
- 6. Click Send and verify the response of 200 and the message
```

Create json payload

``` 
{
    "productCode": "P06092001",
    "productName": "Samsung 120 inch TV",
    "shortDescription": "",
    "longDescription": "60 inch Samsung Television with Retina Display",
    "canDisplay": true,
    "productPrice": 2199.99,
    "productDiscountPercentage": 10.3,
    "parentCategory": {
        "id": 3,
        "categoryName": "Electronics",
        "categoryDescription": "Electronics"
    },
    "category": {
        "id": 4,
        "categoryName": "Televison",
        "categoryDescription": "Televison"
    },
    "deleted": false,
    "automotive": false,
    "international": true
}
```
![Alt text](images/6.png?raw=true "Title")

### Verify the newly created Product in the previous GET tab

![Alt text](images/7.png?raw=true "Title")

### Update the newly created product using PUT

![Alt text](images/8.png?raw=true "Title")

### Update json payload

``` 
{
    "id": 5,
    "productCode": "P06092001",
    "productName": "Samsung 120 inch TV",
    "shortDescription": "",
    "longDescription": "60 inch Samsung Television with Retina Display",
    "canDisplay": true,
    "productPrice": 2199.99,
    "productDiscountPercentage": 10.3,
    "parentCategory": {
        "id": 3,
        "categoryName": "Electronics",
        "categoryDescription": "Electronics"
    },
    "category": {
        "id": 4,
        "categoryName": "Televison",
        "categoryDescription": "Televison"
    },
    "deleted": false,
    "automotive": false,
    "international": true
}
```

### Verify the update

![Alt text](images/9.png?raw=true "Title")

### Delete the newly created product
![Alt text](images/10.png?raw=true "Title")



