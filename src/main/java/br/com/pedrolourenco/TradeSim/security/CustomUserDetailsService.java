package br.com.pedrolourenco.TradeSim.security;

import br.com.pedrolourenco.TradeSim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {
        return userRepository.findByCpfAndActiveIsTrue(cpf)
                .map(u -> new CustomUserDetails(u.getId(), u.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }
}
