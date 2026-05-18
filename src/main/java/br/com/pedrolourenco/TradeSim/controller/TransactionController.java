package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.response.BasicResponse;
import br.com.pedrolourenco.TradeSim.controller.response.DataListMetadata;
import br.com.pedrolourenco.TradeSim.controller.response.DataListResponse;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.domain.transaction.BalanceTransactionInputDTO;
import br.com.pedrolourenco.TradeSim.domain.transaction.StockTransactionInputDTO;
import br.com.pedrolourenco.TradeSim.domain.transaction.Transaction;
import br.com.pedrolourenco.TradeSim.domain.transaction.TransactionOutputDTO;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.mapper.TransactionMapper;
import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import br.com.pedrolourenco.TradeSim.service.TransactionOrchestratorService;
import br.com.pedrolourenco.TradeSim.service.TransactionService;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    private final TransactionOrchestratorService transactionOrchestratorService;

    private final TransactionMapper transactionMapper;

    @GetMapping()
    public ResponseEntity<DataListResponse<TransactionOutputDTO>> list(@RequestParam @PastOrPresent LocalDate startDate,
                                                                       @RequestParam @PastOrPresent LocalDate endDate,
                                                                       @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC) Pageable pageable){
        Page<TransactionOutputDTO> page = transactionService.list(
                getAuthenticatedUser().getId(),
                pageable,
                startDate,
                endDate)
                .map(transactionMapper::toDTO);

        DataListMetadata metadata = new DataListMetadata(
                page.getPageable().getPageNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getNumberOfElements());

        DataListResponse<TransactionOutputDTO> response = new DataListResponse<>(false, metadata, page);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<BasicResponse> deposit(@RequestBody BalanceTransactionInputDTO amount){
        transactionOrchestratorService.deposit(getAuthenticatedUser(), amount.getAmount());

        BasicResponse response = new BasicResponse(false, "deposito realizado");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<BasicResponse> withdraw(@RequestBody BalanceTransactionInputDTO amount){
        transactionOrchestratorService.withdraw(getAuthenticatedUser(), amount.getAmount());

        BasicResponse response = new BasicResponse(false, "saque realizado");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/buy/{ticker}")
    public ResponseEntity<DataResponse<TransactionOutputDTO>> buyStock(@PathVariable String ticker,
                                                                       @RequestBody StockTransactionInputDTO quantity){
        Transaction transaction = transactionOrchestratorService.buyStock(getAuthenticatedUser(), ticker, quantity.getQuantity());

        DataResponse<TransactionOutputDTO> response = new DataResponse<>(
                false,
                "ação comprada",
                transactionMapper.toDTO(transaction));

        return ResponseEntity.ok(response);
    }

    @PostMapping("sell/{ticker}")
    public ResponseEntity<DataResponse<TransactionOutputDTO>> sellStock(@PathVariable String ticker,
                                                                       @RequestBody StockTransactionInputDTO quantity){
        Transaction transaction = transactionOrchestratorService.sellStock(getAuthenticatedUser(), ticker, quantity.getQuantity());

        DataResponse<TransactionOutputDTO> response = new DataResponse<>(
                false,
                "ação vendida",
                transactionMapper.toDTO(transaction));

        return ResponseEntity.ok(response);
    }

    private User getAuthenticatedUser(){
        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = new User();
        user.setId(userDetails.getId());

        return user;
    }
}
