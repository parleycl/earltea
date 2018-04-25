package earlgrey.utils;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import io.jsonwebtoken.*;
import java.util.Date;  

import org.json.JSONObject;

import earlgrey.annotations.AddConfig;
import earlgrey.core.Properties;

@AddConfig(defaultTo = "Earlgrey", earlgrey_name = "JSON Web token Secret", name = "JWT_SECRET")
@AddConfig(defaultTo = "3600", earlgrey_name = "JSON Web token TTL", name = "JWT_TTL")
public class JWT {
	public static String getJWT(JSONObject payload, String id){
		String apiKey = Properties.getInstance().getConfig("JWT_SECRET");
		String subject = payload.toString();
		int ttlMillis = Integer.parseInt(Properties.getInstance().getConfig("JWT_TTL"));
		//The JWT signature algorithm we will be using to sign the token
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	 
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	 
	    //We will sign our JWT with our ApiKey secret
	    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(apiKey);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	 
	    //Let's set the JWT Claims
	    JwtBuilder builder = Jwts.builder().setId(id)
	                                .setIssuedAt(now)
	                                .setSubject(subject)
	                                .setIssuer("Earlgrey")
	                                .signWith(signatureAlgorithm, signingKey);
	 
	    //if it has been specified, let's add the expiration
	    if (ttlMillis >= 0) {
	    long expMillis = nowMillis + ttlMillis;
	        Date exp = new Date(expMillis);
	        builder.setExpiration(exp);
	    }
	 
	    //Builds the JWT and serializes it to a compact, URL-safe string
	    return builder.compact();
	}
	public static Claims getJWTPayload(String JWT){
		String apiKey = Properties.getInstance().getConfig("JWT_SECRET");
		JwtParser jwt = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(apiKey));		
		Claims claims = jwt.parseClaimsJwt(JWT).getBody();
	    return claims;
	}
}
