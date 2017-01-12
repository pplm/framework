package org.pplm.framework.cas.jwt.token.test;

import org.junit.Test;
import org.pplm.framework.cas.jwt.token.TokenGenerator;

import org.junit.Assert;

public class TokenGeneratorTest {
	
	@Test
	public void generateTest() {
		String token = TokenGenerator.generateToken("helloworld");
		Assert.assertEquals(399, token.length());
	}
	
}
