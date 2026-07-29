package br.com.pedrolourenco.TradeSim.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TradeSim",
                version = "v1.0",
                description = "An API simulating an investment brokerage, enabling account creation and updates, login, withdrawals/deposits, and the buying/selling of Brazilian stocks, as well as providing simple metrics on positions and the portfolio.",
                contact = @Contact(
                        name = "Pedro Eduardo Lourenço",
                        email = "pedroedu2007@gmail.com"
                )
        )
)
public class OpenApiConfiguration {
}
