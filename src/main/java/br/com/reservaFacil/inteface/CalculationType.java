package br.com.reservaFacil.inteface;

import br.com.reservaFacil.model.FinancialTransferScheduling;

import java.math.BigDecimal;

public interface CalculationType {

    public BigDecimal calculateFee(FinancialTransferScheduling financialTransferScheduling);

}
