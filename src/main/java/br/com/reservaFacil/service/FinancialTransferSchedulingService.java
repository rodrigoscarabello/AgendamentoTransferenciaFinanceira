package br.com.reservaFacil.service;

import br.com.reservaFacil.inteface.CalculationType;
import br.com.reservaFacil.model.FinancialTransferScheduling;
import br.com.reservaFacil.model.InputMessage;
import br.com.reservaFacil.model.Message;
import br.com.reservaFacil.model.TransferType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
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

    private final AtomicLong counter = new AtomicLong();

    public Message scheduleTransfer(String originAccount, String destinationAccount, BigDecimal value, String scheduleDate, String transferType) {
        Message message = new Message();
        FinancialTransferScheduling transfer = new FinancialTransferScheduling();
        try{
            transfer.setId(counter.incrementAndGet());
            transfer.setOriginAccount(originAccount);
            transfer.setDestinationAccount(destinationAccount);
            transfer.setValue(value);
            transfer.setRegistrationDate(LocalDate.now());

            try{
                if(scheduleDate != null && !StringUtils.isEmpty(scheduleDate)){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate date = LocalDate.parse(scheduleDate, formatter);
                    transfer.setScheduleDate(date);
                }
            }catch (Exception e){
                message.setSuccess(false);
                message.getInputMessages().add(new InputMessage("scheduleDate","Valor incorreto, favor verificar o valor informado"));
            }

            try {
                if(transferType!= null && !StringUtils.isEmpty(transferType))
                    transfer.setTransferType(TransferType.valueOf(transferType));
            }catch (Exception e){
                message.setSuccess(false);
                message.getInputMessages().add(new InputMessage("transferType","Valor incorreto, favor verificar o valor informado"));
            }

            validateSchedule(transfer, message);

            if(message.getSuccess() == null) {
                calculateFee(transfer);
                transfers.add(transfer);
                message.setSuccess(true);
                message.setMessage("Agendamento realizado com sucesso!");
            }
        }catch (Exception e) {
            message.setSuccess(false);
            message.setMessage("Ocorreu um erro! Por favor tente novamente.");
            return message;
        }

        return message;

    }

    public List<FinancialTransferScheduling> listSchedules() {
        return transfers.stream().sorted(
                Comparator.comparing(FinancialTransferScheduling::getScheduleDate)
        ).collect(Collectors.toList());
    }

    private void validateSchedule(FinancialTransferScheduling financialTransferScheduling, Message message) {
        List<InputMessage> listInputMessages = new ArrayList<>();
        if(financialTransferScheduling.getOriginAccount() == null || StringUtils.isEmpty(financialTransferScheduling.getOriginAccount()))
            listInputMessages.add(new InputMessage("originAccount","Necessário informar a conta de origem"));
        else if(financialTransferScheduling.getOriginAccount().length() < 7 || !financialTransferScheduling.getOriginAccount().contains("-"))
                listInputMessages.add(new InputMessage("originAccount","O número da conta deve estar no padrão XXXXX-X"));

        if(financialTransferScheduling.getDestinationAccount() == null || StringUtils.isEmpty(financialTransferScheduling.getDestinationAccount()))
            listInputMessages.add(new InputMessage("destinationAccount","Necessário informar a conta de destino"));
        else if(financialTransferScheduling.getDestinationAccount().length() < 7 || !financialTransferScheduling.getDestinationAccount().contains("-"))
            listInputMessages.add(new InputMessage("destinationAccount","O número da conta deve estar no padrão XXXXX-X"));

        if((financialTransferScheduling.getScheduleDate() == null || StringUtils.isEmpty(financialTransferScheduling.getScheduleDate())) && message.getInputMessages().stream().filter(inputMessage -> inputMessage.inputName.equalsIgnoreCase("scheduleDate")).count() == 0)
            listInputMessages.add(new InputMessage("scheduleDate","Necessário informar a data da transferência"));
        else if(financialTransferScheduling.getScheduleDate() != null && financialTransferScheduling.getScheduleDate().isBefore(LocalDate.now()))
            listInputMessages.add(new InputMessage("scheduleDate","A data para transferência deve ser superior a hoje"));

        if(financialTransferScheduling.getValue() == null || financialTransferScheduling.getValue().compareTo(new BigDecimal(0)) <= 0)
            listInputMessages.add(new InputMessage("value","Necessário informar o valor da transferência"));

        if((financialTransferScheduling.getTransferType() == null || StringUtils.isEmpty(financialTransferScheduling.getTransferType())) && message.getInputMessages().stream().filter(inputMessage -> inputMessage.inputName.equalsIgnoreCase("transferType")).count() == 0)
            listInputMessages.add(new InputMessage("transferType","Necessário informar o tipo da transferência"));

        if(!listInputMessages.isEmpty()) {
            message.setSuccess(false);
            message.getInputMessages().addAll(listInputMessages);
        }
    }

    public void calculateFee(FinancialTransferScheduling financialTransferScheduling) {
        CalculationType calculationType = getCalculationTypeByTransferType(financialTransferScheduling);
        financialTransferScheduling.setFee(calculationType.calculateFee(financialTransferScheduling));
    }

    public CalculationType getCalculationTypeByTransferType(FinancialTransferScheduling financialTransferScheduling) {
        switch (financialTransferScheduling.getTransferType()) {
            case A: return transferTypeAService;
            case B: return transferTypeBService;
            case C: return transferTypeCService;
            case D: return transferTypeDService.getTransferTypeByValue(financialTransferScheduling.getValue());
        }

        return null;
    }

}
