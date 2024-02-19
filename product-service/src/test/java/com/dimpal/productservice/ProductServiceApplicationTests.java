package com.dimpal.productservice;

import com.dimpal.productservice.dto.ProductRequest;
import com.dimpal.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests
{

	/* Junit5 will understand that this is the MySQLContainer */
	@Container
	/*
	 * Define a MySQLContainer and create its object, specifying the version of
	 * mysql to be used for these tests
	 */
	/*
	 * static declaration because I want to statically access this MySQLContainer
	 * and fetch the url of the mysql database
	 */
	static MySQLContainer mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"));

	@Autowired
	private MockMvc mockMvc;

	/* convert POJO object to JSON and JSON to POJO object */

	@Autowired
	private ObjectMapper objectMapper;

	/* Autowiring the repo to verify that the data is saved or not */
	@Autowired
	ProductRepository productRepository;

	/* We need to set the URI of MySQLContainer in our test properties */
	/*
	 * add properties with dynamic values to the Environment's set of
	 * PropertySources.
	 */
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mysql.uri", mysqlContainer::getJdbcUrl);
	}

	/*
	 * This test will give a call to the api endpoint - /api/product
	 * and will expect status code to be 201
	 */

	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();

		/*
		 * I need to convert this productRequest object to a string so that I can pass
		 * it in content So I need to user ObjectMapper
		 */

		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/product").contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());  //verify that the ResponseStatus code is 201 or not

		/* verify that the data is saved or not */
		Assertions.assertEquals(1,productRepository.findAll().size());
	}

	private ProductRequest getProductRequest() {

		return ProductRequest.builder()
				.name("IPhone 13")
				.description("IPhone 13")
				.price(BigDecimal.valueOf(1200))
				.build();
	}
}
//{
//	@Container
//	static MySQLContainer mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0"));
//
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private ObjectMapper objectMapper;
//
//	@DynamicPropertySource
//	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
//		dynamicPropertyRegistry.add("spring.data.mysql.uri", mysqlContainer::getJdbcUrl);
//	}
//	@Test
//	void shouldCreateProduct() throws Exception {
//
//		ProductRequest productRequest = getProductRequest();
//        String productRequestString = objectMapper.writeValueAsString(productRequest);
//		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
//				.contentType(MediaType.APPLICATION_JSON)
//				.content(productRequestString))
//				.andExpect(status().isCreated());
//	}
//
//	private ProductRequest getProductRequest() {
//
//		return ProductRequest.builder()
//				.name("iphone 14")
//				.description("iphone 14")
//				.price(BigDecimal.valueOf(75000))
//				.build();
//	}
//}

