package com.bookStore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
		
	}
	
	
	@Test
	public void chechTest() {
		int expected = 12;
		int totalResult = 12;
		
		Assertions.assertEquals(expected, totalResult);
	}
}
