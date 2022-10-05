package com.rollingstone.controller;

import com.rollingstone.model.Category;
import com.rollingstone.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
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
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;


import java.awt.*;
import java.io.IOException;
import java.util.List;


@Controller
public class UIController
{
	Logger logger = LoggerFactory.getLogger("UIController");

	@Autowired
    OAuth2AuthorizedClientService oauth2ClientService;

	@Autowired
	private ClientRegistrationRepository clientRegistrationRepository;

	RestTemplate restTemplate;

	@Autowired
	Environment env;

	@Autowired
	WebClient webClient;

	public UIController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

    @GetMapping("/category")
    public String getCategory(Model model, @AuthenticationPrincipal OidcUser principal) {

		String url = null;
		logger.info("Principal :" + principal);
		//OAuth2 Authentication Object

		String[] activeProfiles = env.getActiveProfiles();
		logger.info("Active Profile is :" + activeProfiles[0]);

		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();

		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

		OAuth2AuthorizedClient oauth2Client = oauth2ClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(),
				oauthToken.getName());

		logger.info("oauthToken.getName() = " + oauthToken.getName());

		String jwtAccessToken = oauth2Client.getAccessToken().getTokenValue();
		logger.info("jwtAccessToken = " + jwtAccessToken);

		if (activeProfiles[0].equalsIgnoreCase("kc18")) {
			url = "http://localhost:6090/api/category/all/";
		}else if (activeProfiles[0].equalsIgnoreCase("kc19")) {
			url = "http://localhost:8090/api/category/all/";
			logger.info("KC19 URL = " + url);
		}

		logger.info("final URL = " + url);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwtAccessToken);

		HttpEntity<List<Category>> entity = new HttpEntity<>(headers);
		ResponseEntity<List<Category>> responseEntity =  restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Category>>() {});
		logger.info("responseEntity.getBody() = " + responseEntity.getBody());
		List<Category> categories = responseEntity.getBody();
        model.addAttribute("categories", categories);
        return "categories";
    }

	@GetMapping("/category-wc")
	public String getAlbumsLila(Model model,
                                @AuthenticationPrincipal OidcUser principal) {
		String url = "http://localhost:8090/api/category/all/";
		List<Category> categories = webClient.get()
				.uri(url)
				.retrieve()
				.bodyToMono(new ParameterizedTypeReference<List<Category>>(){})
				.block();
		model.addAttribute("categories", categories);
		return "categories";
	}

	@GetMapping("/product")
	public String getProduct(Model model, @AuthenticationPrincipal OidcUser principal) {
		logger.info("Principal :" + principal);

		String url = null;

		String[] activeProfiles = env.getActiveProfiles();
		logger.info("Active Profile is :" + activeProfiles[0]);

		//OAuth2 Authentication Object
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
		OAuth2AuthorizedClient oauth2Client = oauth2ClientService.loadAuthorizedClient(oauthToken.getAuthorizedClientRegistrationId(),
				oauthToken.getName());
		logger.info("oauthToken.getName() = " + oauthToken.getName());
		String jwtAccessToken = oauth2Client.getAccessToken().getTokenValue();
		logger.info("jwtAccessToken = " + jwtAccessToken);

		//String url = "http://localhost:8090/api/product/all/";

		if (activeProfiles[0].equalsIgnoreCase("kc18")) {
			url = "http://localhost:6090/api/product/all/";
		}else if (activeProfiles[0].equalsIgnoreCase("kc19")) {
			url = "http://localhost:8090/api/product/all/";
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + jwtAccessToken);

		HttpEntity<List<Product>> entity = new HttpEntity<>(headers);
		ResponseEntity<List<Product>> responseEntity =  restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Product>>() {});
		logger.info("responseEntity.getBody() = " + responseEntity.getBody());
		List<Product> products = responseEntity.getBody();
		model.addAttribute("products", products);
		return "products";
	}

	@GetMapping("/architecture")
	public String getArchitecture(Model model, @AuthenticationPrincipal OidcUser principal) {
		return "architecture";
	}

	@RequestMapping(value = "/arch", method = RequestMethod.GET,
			produces = MediaType.IMAGE_JPEG_VALUE)
	public void getImage(HttpServletResponse response) throws IOException {

		var imgFile = new ClassPathResource("images/architecture.png");

		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
	}
}
