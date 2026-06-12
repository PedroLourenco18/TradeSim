package br.com.pedrolourenco.TradeSim.controller;

import br.com.pedrolourenco.TradeSim.controller.request.DateRangeRequest;
import br.com.pedrolourenco.TradeSim.controller.response.PageMetadata;
import br.com.pedrolourenco.TradeSim.controller.response.DataResponse;
import br.com.pedrolourenco.TradeSim.controller.response.PagedDataResponse;
import br.com.pedrolourenco.TradeSim.domain.transaction.*;
import br.com.pedrolourenco.TradeSim.domain.user.User;
import br.com.pedrolourenco.TradeSim.exception.UnprocessableDataException;
import br.com.pedrolourenco.TradeSim.mapper.BalanceTransactionMapper;
import br.com.pedrolourenco.TradeSim.mapper.TransactionMapper;
import br.com.pedrolourenco.TradeSim.security.CustomUserDetails;
import br.com.pedrolourenco.TradeSim.service.TransactionOrchestratorService;
import br.com.pedrolourenco.TradeSim.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    private final TransactionOrchestratorService transactionOrchestratorService;

    private final TransactionMapper transactionMapper;

    private final BalanceTransactionMapper balanceTransactionMapper;

    private static final Map<String, String> SORT_FIELDS = Map.of(
            "transactionType", "type",
            "transactionTime", "createdAt",
            "stockTicker",     "stock.ticker",
            "amount",          "amount"
    );

    @GetMapping()
    public ResponseEntity<PagedDataResponse<TransactionOutputDTO>> list(@Valid @ModelAttribute DateRangeRequest dateRange,
                                                                        @PageableDefault(page = 0, size = 10, direction = Sort.Direction.ASC) Pageable pageable){
        LocalDate startDate = LocalDate.parse(dateRange.startDate());
        LocalDate endDate = LocalDate.parse(dateRange.endDate());

        if (startDate.isAfter(endDate)) {
            throw new UnprocessableDataException("A data inicial deve ser igual ou posterior a final");
        }

        if(endDate.isAfter(LocalDate.now())){
            throw new UnprocessableDataException("Ambas as datas devem ser no presente ou passado");
        }

        Page<TransactionOutputDTO> page = transactionService.list(
                getAuthenticatedUser().getId(),
                translateSort(pageable),
                startDate,
                endDate)
                .map(transactionMapper::toDTO);

        PageMetadata metadata = new PageMetadata(
                page.getNumber(),
                page.getSize(),
                page.getNumberOfElements(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.hasNext(),
                page.hasPrevious()
        );

        String message = page.getNumberOfElements() == 0 ?
                "Nenhuma transação encontrada" : "transações encontradas";

        PagedDataResponse<TransactionOutputDTO> response = new PagedDataResponse<>(
                false,
                message,
                metadata,
                page.getContent()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity<DataResponse<BalanceTransactionOutputDTO>> deposit(@RequestBody @Valid BalanceTransactionInputDTO amount){
        Transaction transaction = transactionOrchestratorService.deposit(getAuthenticatedUser(), new BigDecimal(amount.getAmount()));

        DataResponse<BalanceTransactionOutputDTO> response = new DataResponse<>(
                false,
                "deposito realizado",
                balanceTransactionMapper.toDTO(transaction)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<DataResponse<BalanceTransactionOutputDTO>> withdraw(@RequestBody @Valid BalanceTransactionInputDTO amount){
        Transaction transaction = transactionOrchestratorService.withdraw(getAuthenticatedUser(), new BigDecimal(amount.getAmount()));

        DataResponse<BalanceTransactionOutputDTO> response = new DataResponse<>(
                false,
                "saque realizado",
                balanceTransactionMapper.toDTO(transaction)
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/buy/{ticker}")
    public ResponseEntity<DataResponse<TransactionOutputDTO>> buyStock(@PathVariable String ticker,
                                                                       @RequestBody @Valid StockTransactionInputDTO quantity){
        Transaction transaction = transactionOrchestratorService.buyStock(getAuthenticatedUser(), ticker, Long.parseLong(quantity.getQuantity()));

        DataResponse<TransactionOutputDTO> response = new DataResponse<>(
                false,
                "ação comprada",
                transactionMapper.toDTO(transaction));

        return ResponseEntity.ok(response);
    }

    @PostMapping("sell/{ticker}")
    public ResponseEntity<DataResponse<TransactionOutputDTO>> sellStock(@PathVariable String ticker,
                                                                       @RequestBody @Valid StockTransactionInputDTO quantity){
        Transaction transaction = transactionOrchestratorService.sellStock(getAuthenticatedUser(), ticker, Long.parseLong(quantity.getQuantity()));

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

    private Pageable translateSort(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(pageable.getSort().stream()
                        .map(o -> {
                            String property = SORT_FIELDS.get(o.getProperty());
                            if(property == null){
                                throw new UnprocessableDataException("campo de sort invalido");
                            }

                            return o.withProperty(property);
                        })
                        .toList())
        );
    }
}
