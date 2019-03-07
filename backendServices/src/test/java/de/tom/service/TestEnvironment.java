package de.tom.service;

import org.junit.Test;

public class TestEnvironment {
	@Test
	public void getEnv() {
		System.getenv().forEach( (k, v) -> System.out.println( k + ": " + v ) );
	}
	@Test
	public void getProps() {
		System.getProperties().list( System.out );
	}
}
