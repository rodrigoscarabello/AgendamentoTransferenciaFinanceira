package br.com.reservaFacil.controller;

import br.com.reservaFacil.model.FinancialTransferScheduling;
import br.com.reservaFacil.model.Message;
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
    public Message scheduleTransfer(@RequestParam(value = "originAccount", required = false) String originAccount, @RequestParam(value = "destinationAccount", required = false) String destinationAccount,
                                    @RequestParam(value = "value", required = false) BigDecimal value, @RequestParam(value = "scheduleDate", required = false) String scheduleDate,
                                    @RequestParam(value = "transferType", required = false) String transferType) {
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
