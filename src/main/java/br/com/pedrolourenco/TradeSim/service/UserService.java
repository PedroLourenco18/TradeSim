package br.com.pedrolourenco.TradeSim.service;

import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.exception.ConflictDataException;
import br.com.pedrolourenco.TradeSim.exception.UnprocessableDataException;
import br.com.pedrolourenco.TradeSim.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public User register(User userInput){
        if(userRepository.existsByEmail(userInput.getEmail())){
            throw new ConflictDataException(
                    "O email '" + userInput.getEmail() + "' ja esta sendo usado");
        }

        if(userRepository.existsByCpf(userInput.getCpf())){
            throw new ConflictDataException(
                    "Uma conta ja foi criada usado o cpf '" + userInput.getCpf() + "'");
        }

        if(userInput.getBirthDate().isAfter(LocalDate.now().minusYears(18))){
            throw new UnprocessableDataException(
                    "É necessário ser maior de idade para criar uma conta");
        }

        userInput.setActive(true);

        userInput.setPassword(passwordEncoder.encode(userInput.getPassword()));

        return userRepository.save(userInput);
    }

    public User findUser(UUID id){
        return userRepository.findByIdAndActiveIsTrue(id).orElseThrow();
    }

    public void update(UUID id, User updateUserInput){
        if(userRepository.existsByEmailIsAndIdIsNot(updateUserInput.getEmail(), id)){
            throw new ConflictDataException(
                    "O email '" + updateUserInput.getEmail() + "' ja esta sendo usado");
        }

        userRepository.updateNicknameAndEmail(id, updateUserInput.getNickname(), updateUserInput.getEmail());
    }

    public void delete(UUID id){
        userRepository.deactivate(id);
    }

    public void sumToBalance(User user, BigDecimal amount){
        userRepository.addBalance(user.getId(), amount);
    }
}
