package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public String login(String cpf, String password){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(cpf, password);

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        return tokenService.generateToken(customUserDetails.getId());
    }
}
