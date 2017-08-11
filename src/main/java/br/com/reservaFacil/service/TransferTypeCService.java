package br.com.reservaFacil.service;

import br.com.reservaFacil.inteface.CalculationType;
import br.com.reservaFacil.model.FinancialTransferScheduling;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TransferTypeCService implements CalculationType {

    @Override
    public BigDecimal calculateFee(FinancialTransferScheduling financialTransferScheduling) {
        long days = DAYS.between(financialTransferScheduling.getRegistrationDate(), financialTransferScheduling.getScheduleDate());

        if(days > 30)
            return financialTransferScheduling.getValue().multiply(new BigDecimal("0.012")).setScale(2, RoundingMode.HALF_EVEN);
        else if(days > 25)
            return financialTransferScheduling.getValue().multiply(new BigDecimal("0.021")).setScale(2, RoundingMode.HALF_EVEN);
        else if(days > 20)
            return financialTransferScheduling.getValue().multiply(new BigDecimal("0.043")).setScale(2, RoundingMode.HALF_EVEN);
        else if(days > 15)
            return financialTransferScheduling.getValue().multiply(new BigDecimal("0.054")).setScale(2, RoundingMode.HALF_EVEN);
        else if(days > 10)
            return financialTransferScheduling.getValue().multiply(new BigDecimal("0.067")).setScale(2, RoundingMode.HALF_EVEN);
        else if(days > 5)
            return financialTransferScheduling.getValue().multiply(new BigDecimal("0.074")).setScale(2, RoundingMode.HALF_EVEN);
        else
            return financialTransferScheduling.getValue().multiply(new BigDecimal("0.083")).setScale(2, RoundingMode.HALF_EVEN);

    }

}
