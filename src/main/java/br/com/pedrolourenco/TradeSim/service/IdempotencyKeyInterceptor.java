package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.controller.response.BasicResponse;
import br.com.pedrolourenco.TradeSim.domain.idempotency_key.IdempotencyKey;
import br.com.pedrolourenco.TradeSim.domain.idempotency_key.RequestStatus;
import br.com.pedrolourenco.TradeSim.repository.IdempotencyKeyRepository;
import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdempotencyKeyInterceptor implements HandlerInterceptor {
    private final String idempotencyKeyHeader = "Idempotency-Key";

    private final IdempotencyKeyRepository idempotencyKeyRepository;

    private static final String SHOULD_COMPLETE = "idempotency.shouldComplete";

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = request.getHeader(idempotencyKeyHeader);

        if(key == null || key.isEmpty()){
            setResponse(response, 400, buildJsonResponseBody("O header '" + idempotencyKeyHeader + "' é obrigatório nesse enpoint"));
            return false;
        }

        UUID uuidKey;

        try{
            uuidKey = UUID.fromString(key);
        }catch (IllegalArgumentException e){
            setResponse(response, 400, buildJsonResponseBody("A idempotency-key deve ser no formato UUID"));
            return false;
        }

        Optional<IdempotencyKey> optionalIdempotencyKey = idempotencyKeyRepository.findValid(
                uuidKey,
                getAuthenticatedUserId(),
                request.getRequestURI(),
                LocalDateTime.now());

        if(optionalIdempotencyKey.isEmpty()){
            idempotencyKeyRepository.insertIfAbsent(
                    uuidKey,
                    getAuthenticatedUserId(),
                    request.getRequestURI(),
                    genExpireAtTime());

            request.setAttribute(SHOULD_COMPLETE, true);
            request.setAttribute("idempotency.userId", getAuthenticatedUserId());

            return true;
        }

        IdempotencyKey idempotencyKey = optionalIdempotencyKey.get();

        if(idempotencyKey.getStatus().equals(RequestStatus.PROCESSING)){
            setResponse(response, 409, buildJsonResponseBody("Essa requisição ja esta sendo processada"));
            return false;
        }

        setResponse(response, idempotencyKey.getHttpStatus(), idempotencyKey.getResponse());
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        if (request.getAttribute(SHOULD_COMPLETE) == null) return;

        UUID uuidKey = UUID.fromString(request.getHeader(idempotencyKeyHeader));

        UUID userId = (UUID) request.getAttribute("idempotency.userId");

        if(response.getStatus() == 400){
            idempotencyKeyRepository.deleteById(uuidKey);

            return;
        }

        ContentCachingResponseWrapper wrappedResponse =
                (ContentCachingResponseWrapper) response;

        String responseBody = new String(
                wrappedResponse.getContentAsByteArray(),
                StandardCharsets.UTF_8
        );

        idempotencyKeyRepository.markCompleted(
                uuidKey,
                userId,
                request.getRequestURI(),
                response.getStatus(),
                responseBody);
    }

    private LocalDateTime genExpireAtTime(){
        return LocalDateTime.now().plusHours(12);
    }

    private UUID getAuthenticatedUserId(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userDetails.getId();
    }

    private void setResponse(HttpServletResponse response, int status, String body){
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildJsonResponseBody(String message) throws JsonProcessingException {
        BasicResponse responseBody = new BasicResponse(true, message);

        return objectMapper.writeValueAsString(responseBody);
    }
}
