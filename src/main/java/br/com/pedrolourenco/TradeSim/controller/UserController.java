package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.BasicResponse;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.domain.user.RegisterUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.UpdateUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.UserOutputDTO;
import br.com.pedrolourenco.TradeSim.mapper.UserMapper;
import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import br.com.pedrolourenco.TradeSim.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "Endpoints for user management")
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    @Operation(
            summary = "Get authenticated user details",
            description = """
            Returns the data of the user corresponding to the JWT token sent in the request.

            **🔒 Endpoint protected — requires JWT authentication.**
            Send header: `Authorization: Bearer {token}`

            The authenticated user can only consult their own data.
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DataResponse.class), examples = @ExampleObject(value = "{\"error\": false, \"message\": \"User found\", \"data\": {\"name\": \"John Doe\", \"nickname\": \"johndoe\", \"email\": \"john@example.com\", \"cpf\": \"12345678900\", \"birthDate\": \"1990-01-01\", \"balance\": 1500.50}}")))
    })
    public ResponseEntity<DataResponse<UserOutputDTO>> findUser(){

        UserOutputDTO userOutputDTO = userMapper.toDTO(userService.findUser(getAuthenticatedUserId()));

        DataResponse<UserOutputDTO> response = new DataResponse<>(
                false,
                "User found",
                userOutputDTO
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user in the system. This is a public endpoint."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BasicResponse.class), examples = @ExampleObject(value = "{\"error\": false, \"message\": \"User created successfully!\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict - Email or CPF already registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BasicResponse.class), examples = @ExampleObject(value = "{\"error\": true, \"message\": \"Email 'john@example.com' is already in use\"}"))),
            @ApiResponse(responseCode = "422", description = "Unprocessable Entity - User must be of legal age", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BasicResponse.class), examples = @ExampleObject(value = "{\"error\": true, \"message\": \"User must be of legal age\"}")))
    })
    public ResponseEntity<BasicResponse> save(@RequestBody @Valid RegisterUserInputDTO userInputDTO){
        userService.register(userMapper.toEntity(userInputDTO));

        BasicResponse response = new BasicResponse(
                false,
                "User created successfully!"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @Operation(
            summary = "Update user information",
            description = """
            Updates the nickname and email of the authenticated user.

            **🔒 Endpoint protected — requires JWT authentication.**
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BasicResponse.class), examples = @ExampleObject(value = "{\"error\": false, \"message\": \"User updated successfully!\"}"))),
            @ApiResponse(responseCode = "409", description = "Conflict - Email already registered", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BasicResponse.class), examples = @ExampleObject(value = "{\"error\": true, \"message\": \"Email 'john@example.com' is already in use\"}")))
    })
    public ResponseEntity<BasicResponse> update(@RequestBody @Valid UpdateUserInputDTO updateUserInputDTO){
        userService.update(getAuthenticatedUserId(), userMapper.toEntity(updateUserInputDTO));

        BasicResponse response = new BasicResponse(
                false,
                "User updated successfully!"
        );

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    @Operation(
            summary = "Delete user account",
            description = """
            Deletes the authenticated user account.

            **🔒 Endpoint protected — requires JWT authentication.**
            """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = BasicResponse.class), examples = @ExampleObject(value = "{\"error\": false, \"message\": \"User deleted successfully!\"}")))
    })
    public ResponseEntity<BasicResponse> delete(){
        userService.delete(getAuthenticatedUserId());

        BasicResponse response = new BasicResponse(
                false,
                "User deleted successfully!"
        );

        return ResponseEntity.ok().body(response);
    }

    private UUID getAuthenticatedUserId(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userDetails.getId();
    }
}
