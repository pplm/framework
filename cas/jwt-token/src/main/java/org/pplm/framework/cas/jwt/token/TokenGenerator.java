package org.pplm.framework.cas.jwt.token;
import java.io.IOException;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.jwt.config.encryption.SecretEncryptionConfiguration;
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration;
import org.pac4j.jwt.profile.JwtGenerator;
import org.pplm.framework.utils.config.ConfigPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;

/**
 * 
 * @author OracleGao
 *
 */
public class TokenGenerator {

	public static final String PROPERTIES_KEY_SIGN_SIZE = "jwt.secret.sign.size";
	public static final String PROPERTIES_KEY_ENCRYPTION_SIZE = "jwt.secret.encryption.size";
	
	public static final String PROPERTIES_KEY_SIGN = "jwt.secret.sign";
	public static final String PROPERTIES_KEY_ENCRYPTION = "jwt.secret.encryption";
	
	public static final String DEFAULT_PROPERTIES_FILE_NAME = "jwt-secret.properties";
	public static final int DEFAULT_SIGN_SIZE = 256;
	public static final int DEFAULT_ENCRYPTION_SIZE = 48;
	
	private static final TokenGenerator gen = new TokenGenerator(); 

	private Logger logger = LoggerFactory.getLogger(TokenGenerator.class); 
	
	private String sign;
	private String encryption;
	
	private int signSize;
	private int encryptionSize;
	
	private JwtGenerator<CommonProfile> jwtGenerator;
	
	private TokenGenerator() {
		super();
		init();
	}
	
	private void init() {
		try {
			ConfigPlayer.addProperties(DEFAULT_PROPERTIES_FILE_NAME);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		signSize = ConfigPlayer.getInt(PROPERTIES_KEY_SIGN_SIZE, DEFAULT_SIGN_SIZE);
		encryptionSize = ConfigPlayer.getInt(PROPERTIES_KEY_ENCRYPTION_SIZE, DEFAULT_ENCRYPTION_SIZE);
		sign = ConfigPlayer.getString(PROPERTIES_KEY_SIGN);
		if (StringUtils.isEmpty(sign)) {
			sign = RandomStringUtils.randomAlphanumeric(signSize);
			logger.info("generate [" + signSize + "] size sign [" + sign + "]");
		}
		encryption = ConfigPlayer.getString(PROPERTIES_KEY_ENCRYPTION);
		if (StringUtils.isEmpty(encryption)) {
			encryption = RandomStringUtils.randomAlphanumeric(encryptionSize);
			logger.info("generate [" + encryptionSize + "] size encryption [" + encryption + "]");
		}
		
		jwtGenerator = new JwtGenerator<>();
		jwtGenerator.setSignatureConfiguration(new SecretSignatureConfiguration(sign, JWSAlgorithm.HS256));
		jwtGenerator.setEncryptionConfiguration(new SecretEncryptionConfiguration(encryption, JWEAlgorithm.DIR, EncryptionMethod.A192CBC_HS384));
	}
	
	private String generate(String id) {
		CommonProfile commonProfile = new CommonProfile();
		commonProfile.setId(id);
		return jwtGenerator.generate(commonProfile);
	}
	
	public static String generateToken(String id) {
		return gen.generate(id);
	}
	
}
