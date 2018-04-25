package earlgrey.utils;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;
import org.json.JSONObject;

import earlgrey.annotations.AddConfig;
import earlgrey.core.Properties;

@AddConfig(defaultTo = "Earlgrey", earlgrey_name = "JSON Web token Secret", name = "JWT_SECRET")
@AddConfig(defaultTo = "3600", earlgrey_name = "JSON Web token TTL", name = "JWT_TTL")
public class JWT {
	public static String getJWT(JSONObject payload, String id, String subject){
		String apiKey = Properties.getInstance().getConfig("JWT_SECRET");
		try {
			Key key = new HmacKey(apiKey.getBytes("UTF-8"));
			
			JwtClaims claims = new JwtClaims();
			claims.setJwtId(id);
			claims.setIssuedAt(NumericDate.now());
			claims.setSubject(subject);
			claims.setStringClaim("payload", payload.toString());
			claims.setIssuer("Earlgrey");
			
			//if it has been specified, let's add the expiration
			long ttlSec = Integer.parseInt(Properties.getInstance().getConfig("JWT_TTL"));
		    if (ttlSec >= 0) {
		    	NumericDate exp = NumericDate.now();
		    	exp.addSeconds(ttlSec);
		        claims.setExpirationTime(exp);
		    }
		    
		    JsonWebSignature jws = new JsonWebSignature();
		    jws.setPayload(claims.toJson());
		    jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
		    jws.setKey(key);
		    jws.setDoKeyValidation(false); // relaxes the key length requirement
		    
		    return jws.getCompactSerialization();
			
		} catch (UnsupportedEncodingException | JoseException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
	public static JwtClaims getJWTPayload(String JWT){
		String apiKey = Properties.getInstance().getConfig("JWT_SECRET");
		try {
			Key key = new HmacKey(apiKey.getBytes("UTF-8"));
			JwtConsumer jwtConsumer = new JwtConsumerBuilder()
	        .setRequireExpirationTime()
	        .setAllowedClockSkewInSeconds(30)
	        .setRequireSubject()
	        .setExpectedIssuer("Earlgrey")
	        .setVerificationKey(key)
	        .setRelaxVerificationKeyValidation() // relaxes key length requirement 
	        .build();
			
			JwtClaims processedClaims = jwtConsumer.processToClaims(JWT);
			return processedClaims;
		} catch (UnsupportedEncodingException | InvalidJwtException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
