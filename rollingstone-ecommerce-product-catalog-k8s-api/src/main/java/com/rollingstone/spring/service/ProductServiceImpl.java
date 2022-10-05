package com.rollingstone.spring.service;

import com.rollingstone.exceptions.HTTP400Exception;
import com.rollingstone.spring.dao.ProductCrudDao;
import com.rollingstone.spring.dao.ProductDaoRepository;
import com.rollingstone.spring.model.Category;
import com.rollingstone.spring.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

	final static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

	@Value("${category.request.path}")
	private  String CATEGORY_REQUEST_PATH = "";

	@Value("${category.port}")
	private  Integer CATEGORY_SERVICE_PORT = 0;

	@Value("${category.service.host}")

	// @Value("${value.from.file}")
	private  String CATEGORY_SERVICE_HOST = "";

	private  String REQUEST_URI = CATEGORY_SERVICE_HOST + ":" + CATEGORY_SERVICE_PORT + CATEGORY_REQUEST_PATH;

	@Autowired
	private ProductDaoRepository productDao;

	@Autowired
	private ProductCrudDao productCrudDao;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	OAuth2AuthorizedClientService oauth2ClientService;


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
	
	public Product saveProductWithoutValidation(Product product) {
		logger.info("Hystrix Circuit Breaker Enabled and called fallback method");

		return productDao.save(product);
	}

	@Override
	public Optional<Product> get(long id) {
		return productDao.findById(id);
	}

	@Override
	public Page<Product> getProductsByPage(Integer pageNumber, Integer pageSize) {
		Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("productCode").descending());
		return productDao.findAll(pageable);
	}

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		Iterable<Product> iterableProducts = productCrudDao.findAll();
		iterableProducts.forEach(products::add);
		return products;
	}

	@Override
	public void update(long id, Product product) {
		productDao.save(product);
	}

	@Override
	public void delete(long id) {
		productDao.deleteById(id);
	}

}
