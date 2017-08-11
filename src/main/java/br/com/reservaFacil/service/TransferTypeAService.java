package br.com.reservaFacil.service;

import br.com.reservaFacil.inteface.CalculationType;
import br.com.reservaFacil.model.FinancialTransferScheduling;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TransferTypeAService implements CalculationType {

    @Override
    public BigDecimal calculateFee(FinancialTransferScheduling financialTransferScheduling) {
        return new BigDecimal("2").add(
                financialTransferScheduling.getValue().multiply(new BigDecimal("0.03"))
            ).setScale(2, RoundingMode.HALF_EVEN);
    }

}
