package de.tom.service;

import de.tom.entity.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith( SpringRunner.class )
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AccountControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private MockMvc mvc;

	private TestRestTemplate testRestTemplate = new TestRestTemplate();

	@Test
	public void testExists() throws Exception {
		final MvcResult result = mvc.perform( get( "/account/exists/Tom" ) )
			.andExpect( status().isOk() ) // HttpStatus = 200
			.andReturn();
		assertEquals( "true", result.getResponse().getContentAsString());
	}

	@Test
	public void testNotExists() throws Exception {
		final MvcResult result = mvc.perform( get( "/account/exists/TomTom" ) )
			.andExpect( status().isNotFound() ) // HttpStatus = 404
			.andReturn();
		assertEquals( "false", result.getResponse().getContentAsString());
	}

	@Test
	public void testEmptyNameNotExists() throws Exception {
		mvc.perform( get( "/account/exists/" ) )
			.andExpect( status().isMethodNotAllowed() ); // HttpStatus = 405

	}

	@Test
	public void testGet() throws Exception {
		mvc.perform( get( "/account/get/1" ) )
			.andExpect( status().isOk() )
			.andExpect( jsonPath("name", is( "Tom" )) );
	}

	@Test
	public void testGetNotExists() throws Exception {
		mvc.perform( get( "/account/get/4711" ) )
			.andExpect( status().isNotFound() );
	}

	@Test
	public void testGetWithTemplate() {
		final Account account = testRestTemplate.getForObject( "http://localhost:" + port + "/account/get/1", Account.class );
		assertEquals( "Tom", account.getName() );
	}

	@Test
	public void testGetNotExistsWithTemplate() {
		final ResponseEntity<Account> entity = testRestTemplate.getForEntity( "http://localhost:" + port + "/account/get/4711", Account.class );
		assertEquals( HttpStatus.NOT_FOUND, entity.getStatusCode() );
		final Account account = entity.getBody();
		assertTrue( null == account || null == account.getName() );
	}
}