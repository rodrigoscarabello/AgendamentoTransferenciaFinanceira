package br.com.reservaFacil.service;

import br.com.reservaFacil.inteface.CalculationType;
import br.com.reservaFacil.model.FinancialTransferScheduling;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TransferTypeBService implements CalculationType {

    @Override
    public BigDecimal calculateFee(FinancialTransferScheduling financialTransferScheduling) {
        long days = DAYS.between(financialTransferScheduling.getRegistrationDate(), financialTransferScheduling.getScheduleDate());
        if(days <= 30)
            return new BigDecimal("10");

        return new BigDecimal("8");
    }

}
