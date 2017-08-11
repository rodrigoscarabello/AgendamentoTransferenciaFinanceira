package br.com.reservaFacil.service;

import br.com.reservaFacil.inteface.CalculationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferTypeDService {

    @Autowired
    private TransferTypeAService transferTypeAService;
    @Autowired
    private TransferTypeBService transferTypeBService;
    @Autowired
    private TransferTypeCService transferTypeCService;

    public CalculationType getTransferTypeByValue(BigDecimal value) {
        if(value.compareTo(new BigDecimal("25000")) <= 0)
            return transferTypeAService;
        else if (value.compareTo(new BigDecimal("25000")) > 0 && value.compareTo(new BigDecimal("120000")) <= 0)
            return transferTypeBService;
        else if(value.compareTo(new BigDecimal("120000")) > 0)
            return transferTypeCService;

        return null;
    }

}
