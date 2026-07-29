package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.AuthResponse;
import br.com.pedrolourenco.TradeSim.domain.user.AuthenticationUserInputDTO;
import br.com.pedrolourenco.TradeSim.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates a user using CPF and password. Returns a JWT token upon success."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Authentication successful",
            content = @Content(schema = @Schema(implementation = AuthResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(examples = @ExampleObject(value = """
                {
                    "error": true,
                    "message": "User not authenticated"
                }
                """))),
        @ApiResponse(responseCode = "400", description = "Invalid input data",
            content = @Content(examples = @ExampleObject(value = """
                {
                    "error": true,
                    "message": "Invalid Field(s)",
                    "fields": {
                        "cpf": "Enter a valid CPF"
                    }
                }
                """)))
    })
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthenticationUserInputDTO authUser){
        String jwt = authenticationService.login(authUser.getCpf(), authUser.getPassword());

        AuthResponse response = new AuthResponse(false, jwt);

        return ResponseEntity.ok(response);
    }
}
