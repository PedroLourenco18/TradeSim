package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.exception.ConflictDataException;
import br.com.pedrolourenco.TradeSim.exception.UnprocessableDataException;
import br.com.pedrolourenco.TradeSim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void register(User userInput){
        if(userRepository.existsByEmail(userInput.getEmail())){
            throw new ConflictDataException(
                    "The email '" + userInput.getEmail() + "' is already in use");
        }

        if(userRepository.existsByCpf(userInput.getCpf())){
            throw new ConflictDataException(
                    "An account has already been created using the CPF '" + userInput.getCpf() + "'");
        }

        if(userInput.getBirthDate().isAfter(LocalDate.now().minusYears(18))){
            throw new UnprocessableDataException(
                    "You must be of legal age to create an account");
        }

        userInput.setActive(true);

        userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));

        userRepository.save(userInput);
    }

    public User findUser(UUID id){
        return userRepository.findByIdAndActiveIsTrue(id).orElseThrow();
    }

    public void update(UUID id, User updateUserInput){
        if(userRepository.existsByEmailIsAndIdIsNot(updateUserInput.getEmail(), id)){
            throw new ConflictDataException(
                    "The email '" + updateUserInput.getEmail() + "' is already in use");
        }

        userRepository.updateNicknameAndEmail(id, updateUserInput.getNickname(), updateUserInput.getEmail());
    }

    public void delete(UUID id){
        userRepository.deactivate(id);
    }

    public void sumToBalance(User user, BigDecimal amount){
        userRepository.addBalance(user.getId(), amount.setScale(4, RoundingMode.HALF_EVEN));
    }
}
