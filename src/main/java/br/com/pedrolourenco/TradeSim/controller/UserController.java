package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.BasicResponse;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.domain.user.RegisterUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.UpdateUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.domain.user.UserOutputDTO;
import br.com.pedrolourenco.TradeSim.mapper.UserMapper;
import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import br.com.pedrolourenco.TradeSim.service.UserService;
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
public class UserController {
    private final UserService userService;

    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<DataResponse<UserOutputDTO>> findUser(){

        UserOutputDTO userOutputDTO = userMapper.toDTO(userService.findUser(getAuthenticatedUserId()));

        DataResponse<UserOutputDTO> response = new DataResponse<>(
                false,
                "Usuario encontrado",
                userOutputDTO
        );

        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<DataResponse<UserOutputDTO>> save(@RequestBody @Valid RegisterUserInputDTO userInputDTO){
        User savedUser = userService.register(userMapper.toEntity(userInputDTO));

        UserOutputDTO userOutputDTO = userMapper.toDTO(savedUser);

        DataResponse<UserOutputDTO> response = new DataResponse<>(
                false,
                "Usuario criado com sucesso!",
                userOutputDTO
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<BasicResponse> update(@RequestBody @Valid UpdateUserInputDTO updateUserInputDTO){
        userService.update(getAuthenticatedUserId(), userMapper.toEntity(updateUserInputDTO));

        BasicResponse response = new BasicResponse(
                false,
                "Usuario editado com sucesso!"
        );

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping
    public ResponseEntity<BasicResponse> delete(){
        userService.delete(getAuthenticatedUserId());

        BasicResponse response = new BasicResponse(
                false,
                "Usuario deletado com sucesso!"
        );

        return ResponseEntity.ok().body(response);
    }

    private UUID getAuthenticatedUserId(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userDetails.getId();
    }
}
