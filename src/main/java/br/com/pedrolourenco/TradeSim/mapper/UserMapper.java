package br.com.pedrolourenco.TradeSim.mapper;

import br.com.pedrolourenco.TradeSim.domain.user.RegisterUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.UpdateUserInputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.domain.user.UserOutputDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterUserInputDTO dto);
    User toEntity(UpdateUserInputDTO dto);

    @Mapping(target = "balance", expression = "java( user.getBalance().setScale(2, java.math.RoundingMode.HALF_EVEN) )")
    UserOutputDTO toDTO(User user);
}
