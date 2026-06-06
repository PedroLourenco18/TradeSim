package br.com.pedrolourenco.TradeSim.config;

import br.com.pedrolourenco.TradeSim.service.IdempotencyKeyInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final IdempotencyKeyInterceptor idempotencyKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(idempotencyKeyInterceptor)
                .addPathPatterns(
                        "/transactions/deposit",
                        "/transactions/withdraw",
                        "/transactions/buy",
                        "/transactions/sell");
    }
}
