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
