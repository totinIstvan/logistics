package hu.webuni.totinistvan.logistics.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import hu.webuni.totinistvan.logistics.config.JwtConfigProperties;
import hu.webuni.totinistvan.logistics.config.JwtConfigProperties.JwtData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    public static final String AUTH = "auth";

    @Autowired
    private JwtConfigProperties config;

    private Algorithm alg;
    private String issuer;

    @PostConstruct
    public void init() {
        JwtData jwtData = config.getJwtData();
        this.issuer = jwtData.getIssuer();
        try {
            this.alg = (Algorithm) Algorithm.class.getMethod(jwtData.getAlgorithm(), String.class)
                    .invoke(Algorithm.class, jwtData.getSecret());
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public String createJwtToken(UserDetails principal) {

        return JWT.create()
                .withSubject(principal.getUsername())
                .withArrayClaim(AUTH, principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .withExpiresAt(new Date(System.currentTimeMillis() + config.getJwtData().getDuration().toMillis()))
                .withIssuer(issuer)
                .sign(alg);
    }

    public UserDetails parseJwt(String jwtToken) {
        DecodedJWT decodedJwt = JWT.require(alg)
                .withIssuer(issuer)
                .build()
                .verify(jwtToken);

        return new User(decodedJwt.getSubject(), "none",
                decodedJwt
                        .getClaim(AUTH)
                        .asList(String.class).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())
        );
    }
}
