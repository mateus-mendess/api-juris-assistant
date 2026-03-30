package br.com.juristrack.Juris.Track.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Configuration
public class KeyConfig {

    @Value("${jwt.private.key}")
    private String privateKey;

    @Value("${jwt.public.key}")
    private String publicKey;

    @Bean
    public RSAPrivateKey privateKey() throws Exception {
        return loadPrivateKey(privateKey);
    }

    @Bean
    public RSAPublicKey publicKey() throws Exception {
        return loadPublicKey(publicKey);
    }

    private RSAPrivateKey loadPrivateKey(String key) throws Exception {
        key = key.replace("\\n", "\n");

        String content = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(content);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);

        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private RSAPublicKey loadPublicKey(String key) throws Exception {
        key = key.replace("\\n", "\n");

        String content = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        byte[] decoded = Base64.getDecoder().decode(content);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);

        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }
}
