package br.com.reservaFacil.controller;

import br.com.reservaFacil.model.FinancialTransferScheduling;
import br.com.reservaFacil.model.TransferType;
import br.com.reservaFacil.service.FinancialTransferSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class FinancialTransferSchedulingController {

    @Autowired
    private FinancialTransferSchedulingService financialTransferSchedulingService;

    @RequestMapping("/schedule")
    public FinancialTransferScheduling scheduleTransfer(@RequestParam(value = "originAccount") String originAccount, @RequestParam(value = "destinationAccount") String destinationAccount,
                                                        @RequestParam(value = "value") BigDecimal value, @RequestParam(value = "scheduleDate") String scheduleDate,
                                                        @RequestParam(value = "transferType") String transferType) {
        return financialTransferSchedulingService.scheduleTransfer(originAccount, destinationAccount, value, scheduleDate, transferType);
    }

    @RequestMapping("/listSchedules")
    public List<FinancialTransferScheduling> listSchedules() {
        return financialTransferSchedulingService.listSchedules();
    }

    @RequestMapping("/listTransferTypes")
    public TransferType[] listTransferTypes() {
        return TransferType.values();
    }
}
