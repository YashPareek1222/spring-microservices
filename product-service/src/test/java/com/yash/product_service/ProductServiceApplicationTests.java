package com.yash.product_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yash.product_service.dto.ProductRequest;
import com.yash.product_service.model.Product;
import com.yash.product_service.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestString))
				.andExpect(status().isCreated());

		log.info(productRepository.findAll().toString());
        Assertions.assertEquals(2, productRepository.findAll().size());
	}

	@Test
	void shouldFetchProducts() throws Exception {
		Product product = getProductResponse();
		ProductRequest productRequest = getProductRequest();
		String productRequestString = objectMapper.writeValueAsString(productRequest);

		// Act - Create the product
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());

		// Act - Fetch the product
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String jsonResponse = result.getResponse().getContentAsString();
		List<Product> fetchedProducts = objectMapper.readValue(jsonResponse, new TypeReference<List<Product>>() {});

		// Assert - Check if the product is correctly stored in the repository
		Assertions.assertEquals(1, productRepository.findAll().size());
		Assertions.assertEquals(fetchedProducts.get(0), productRepository.findAll().get(0));
	}

	private Product getProductResponse() {
		return Product.builder()
				.name("Iphone 13")
				.description("iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Iphone 13")
				.description("iphone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
