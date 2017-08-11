package br.com.reservaFacil.service;

import br.com.reservaFacil.inteface.CalculationType;
import br.com.reservaFacil.model.FinancialTransferScheduling;
import br.com.reservaFacil.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialTransferSchedulingService {

    @Autowired
    private TransferTypeAService transferTypeAService;
    @Autowired
    private TransferTypeBService transferTypeBService;
    @Autowired
    private TransferTypeCService transferTypeCService;
    @Autowired
    private TransferTypeDService transferTypeDService;

    private static List<FinancialTransferScheduling> transfers = new ArrayList<>();

    public FinancialTransferScheduling scheduleTransfer(String originAccount, String destinationAccount, BigDecimal value, String scheduleDate, String transferType) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        FinancialTransferScheduling transfer = new FinancialTransferScheduling();
        transfer.setOriginAccount(originAccount);
        transfer.setDestinationAccount(destinationAccount);
        transfer.setValue(value);
        transfer.setScheduleDate(LocalDate.parse(scheduleDate, formatter));
        transfer.setRegistrationDate(LocalDate.now());
        transfer.setTransferType(TransferType.valueOf(transferType));

        validateSchedule(transfer);
        calculateFee(transfer);
        transfers.add(transfer);
        return transfer;
    }

    public List<FinancialTransferScheduling> listSchedules() {
        return transfers.stream().sorted(
                Comparator.comparing(FinancialTransferScheduling::getScheduleDate)
        ).collect(Collectors.toList());
    }

    private void validateSchedule(FinancialTransferScheduling financialTransferScheduling) {

    }

    private void calculateFee(FinancialTransferScheduling financialTransferScheduling) {
        CalculationType calculationType = getCalculationTypeByTransferType(financialTransferScheduling);
        financialTransferScheduling.setFee(calculationType.calculateFee(financialTransferScheduling));
    }

    private CalculationType getCalculationTypeByTransferType(FinancialTransferScheduling financialTransferScheduling) {
        switch (financialTransferScheduling.getTransferType()) {
            case A: return transferTypeAService;
            case B: return transferTypeBService;
            case C: return transferTypeCService;
            case D: return transferTypeDService.getTransferTypeByValue(financialTransferScheduling.getValue());
        }

        return null;
    }

}
