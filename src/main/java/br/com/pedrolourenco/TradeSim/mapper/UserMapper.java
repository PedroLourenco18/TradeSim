package br.com.pedrolourenco.TradeSim.mapper;

import br.com.pedrolourenco.TradeSim.domain.user.RegisterUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.UpdateUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.domain.user.UserOutputDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterUserInputDTO dto);
    User toEntity(UpdateUserInputDTO dto);
    UserOutputDTO toDTO(User user);
}
