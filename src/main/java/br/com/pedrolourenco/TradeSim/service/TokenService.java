package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.exception.InternalErrorException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(UUID id){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withSubject(String.valueOf(id))
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            throw new InternalErrorException("Erro ao gerar o token jwt");
        }
    }

    public UUID validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return UUID.fromString(JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject());
        }catch (JWTVerificationException e){
            return null;
        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("-03:00"));
    }
}
