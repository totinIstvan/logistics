package hu.webuni.totinistvan.logistics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtConfigProperties {

    private JwtData jwtData = new JwtData();

    public JwtData getJwtData() {
        return jwtData;
    }

    public void setJwtData(JwtData jwtData) {
        this.jwtData = jwtData;
    }

    public static class JwtData {
        private String issuer;
        private String secret;
        private String algorithm;
        private Duration duration;

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getAlgorithm() {
            return algorithm;
        }

        public void setAlgorithm(String algorithm) {
            this.algorithm = algorithm;
        }

        public Duration getDuration() {
            return duration;
        }

        public void setDuration(Duration duration) {
            this.duration = duration;
        }
    }
}
