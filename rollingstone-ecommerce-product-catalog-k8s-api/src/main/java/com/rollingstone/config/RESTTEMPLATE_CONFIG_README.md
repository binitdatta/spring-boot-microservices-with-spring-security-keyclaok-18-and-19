## Spring Boot or Spring MVS does not create Beans Randomly

## Instead, what it does is to calculate a Bean Dependency from Top Down

## Let's consider the @Configuration class RestTemplateConfiguration

## 1. If we look at the Controller which is ProductController, we will see

``` 
 private ProductService  productService;
   
   public ProductController(ProductService productService) {
	   this.productService = productService;
   }

```

## 2. As this ProductController is annotated with the annotation @RestController

## That Means Spring Boot has to call its Constructor to create a Bean of this Controller and 
##  put that bean into the Spring Application Context

## 3. The Moment it tries to analyze the ProductController, it sees that it has a dependency
## for ProductService and it cannot create a Bean of the ProductController without
## first creating a Bean of the ProductService or ProductServiceImpl to be exact

## 4. ProductServiceImpl itself is annotated with @Service
## and has a dependency named 

``` 
@Autowired
	private RestTemplate restTemplate;
```

## 5. What Spring Boot will now do is to find a Method in the @Configuration class / methods
## that returns an instance of a RestTemplate

## That method is in the class

``` 
@Configuration
public class RestTemplateConfiguration {
```

## 6. The Method

``` 
 @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .requestFactory(this::clientHttpRequestFactory)
                .errorHandler(new CustomClientErrorInterceptor())
                .interceptors(new CustomClientHttpRequestInterceptor())
                .build();
    }
```

## 7. Before Spring Boot Creates and returns a RestTemplate from this method
## 8. it has to get a RequestFactory from the method clientHttpRequestFactory
## 9. Create an instance of the class CustomClientErrorInterceptor
## 10. Create an instance of the class CustomClientHttpRequestInterceptor

## Lets go one by one

## 11.

``` 
 @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }
```

## 12. clientHttpRequestFactory returns an instance of HttpComponentsClientHttpRequestFactory
## 13. which comes from Apache Http Client
## 14. However, it needs a 

``` 
 private final CloseableHttpClient httpClient;
```

## 15. This trying to find a CloseableHttpClient, takes us to the Class ApacheHttpClientConfiguration

``` 
  @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }
```

## 16. Here we first are creating a RequestConfig and configuring it with

```
- 1. CONNECTION_TIMEOUT : Time we will wait to establish a physical TCP Connection at the start of the app
- 2. REQUEST_TIMEOUT: Time we will wait after we request a pooled TCP Connection from the Pool
- 3. SOCKET_TIMEOUT: Time we will wait for the server to send us to full response
```

## 17. Thus, once we create the RequestConfig instance, we need to get the instance of 

``` 
@Bean
    public PoolingHttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();

        // set total amount of connections across all HTTP routes
        poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);

        // set maximum amount of connections for each http route in pool
        poolingConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);

       return poolingConnectionManager;
    }
```

## 18. And

``` 
@Bean
    public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
        return (httpResponse, httpContext) -> {
            HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
            HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

            while (elementIterator.hasNext()) {
                HeaderElement element = elementIterator.nextElement();
                String param = element.getName();
                String value = element.getValue();
                if (value != null && param.equalsIgnoreCase("timeout")) {
                    return Long.parseLong(value) * 1000; // convert to ms
                }
            }

            return DEFAULT_KEEP_ALIVE_TIME;
        };
    }
```

## 19. Finally, we also create a Thread by implementing Runnable to Efficiently Manage Connections

``` 
 @Bean
    public Runnable idleConnectionMonitor(PoolingHttpClientConnectionManager pool) {
        return new Runnable() {
            @Override
            public void run() {
                // only if connection pool is initialised
                if (pool != null) {
                    pool.closeExpiredConnections();
                    pool.closeIdleConnections(IDLE_CONNECTION_WAIT_TIME, TimeUnit.MILLISECONDS);

                    logger.info("Idle connection monitor: Closing expired and idle connections");
                }
            }
        };
    }
```

## 20. Now that we came down so far analysing Bean Dependencies, it is time to go up the order
## 21. Now we have everything to create the CloseableHttpClient
``` 
  @Bean
    public CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setConnectionRequestTimeout(REQUEST_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT)
                .build();

        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(poolingConnectionManager())
                .setKeepAliveStrategy(connectionKeepAliveStrategy())
                .build();
    }
```

## 22. Once we can create the CloseableHttpClient, we can go up and create the

```

  @Bean
    public HttpComponentsClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return clientHttpRequestFactory;
    }
```

## 23. Once that is done, we can also create an instance of

``` 
public class CustomClientErrorInterceptor implements ResponseErrorHandler {

    final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean hasError (ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().is4xxClientError();
    }

    @Override
    public void handleError (ClientHttpResponse clientHttpResponse) throws IOException {
        log.error("CustomClientErrorHandler | HTTP Status Code: " + clientHttpResponse.getStatusCode().value());
        throw new HTTP404Exception(("Resource Not Found"));
    }
}
```

## 24. and finally

``` 
package com.rollingstone.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CustomClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] bytes, ClientHttpRequestExecution execution) throws IOException {
        log.info("URI: {}", request.getURI());
        log.info("HTTP Method: {}", request.getMethodValue());
        log.info("HTTP Headers: {}", request.getHeaders());

        return execution.execute(request, bytes);
    }
}
```

## 25. Once our lower order dependencies are create, we can finally create a RestTemplate

``` 
 @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .requestFactory(this::clientHttpRequestFactory)
                .errorHandler(new CustomClientErrorInterceptor())
                .interceptors(new CustomClientHttpRequestInterceptor())
                .build();
    }
```

## 26. Once we can create a RestTemplate we can also create a ProductServiceImpl

## 27. Once we can create a ProductServiceImpl, we can also create a ProductController

## 28. There are other Beans and Dependencies like the Repository etc byt
## 29. if we understand this README file, we are much better off understanding Spring Boot Dependency Injection or DI
