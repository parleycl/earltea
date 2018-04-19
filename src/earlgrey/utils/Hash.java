package earlgrey.utils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import earlgrey.core.Logging;

public class Hash {
	private static Logging log = new Logging(Hash.class.getName());
	public static String MD5(String value) {
		try {
			byte[] bytesOfMessage = value.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			String hash = new String(md.digest(bytesOfMessage), StandardCharsets.UTF_8);
			return hash;
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.Warning("No se pudo convertir el string a hash MD5");
			return null;
		}
		
	}
}
