package br.com.pedrolourenco.TradeSim.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing authentication details.")
public record AuthResponse(
        @Schema(description = "Indicates if an error occurred during authentication", example = "false")
        boolean error,

        @Schema(description = "The JWT token generated upon successful authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
}
