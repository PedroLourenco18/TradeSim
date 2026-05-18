package br.com.pedrolourenco.TradeSim.security;

import br.com.pedrolourenco.TradeSim.repository.PositionRepository;
import br.com.pedrolourenco.TradeSim.repository.TransactionRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IDORFilter extends OncePerRequestFilter {
    private final PositionRepository positionRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if(path.startsWith("/positions/") && request.getMethod().equals("GET")){
            UUID resourceId = extractIdFromPath(path, "/positions/");
            UUID userId = getAuthenticatedUserId();

            if(!positionRepository.existsByIdAndUserId(resourceId, userId)){
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }

            return;
        }

        filterChain.doFilter(request, response);
    }

    private static UUID extractIdFromPath(String path, String prefix) {
        String[] parts = path.replace(prefix, "").split("/");
        return UUID.fromString(parts[0]);
    }

    private static UUID getAuthenticatedUserId(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userDetails.getId();
    }
}
