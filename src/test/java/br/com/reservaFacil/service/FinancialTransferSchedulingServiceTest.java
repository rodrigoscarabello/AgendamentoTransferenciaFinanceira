package br.com.reservaFacil.service;

import br.com.reservaFacil.inteface.CalculationType;
import br.com.reservaFacil.model.FinancialTransferScheduling;
import br.com.reservaFacil.model.Message;
import br.com.reservaFacil.model.TransferType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FinancialTransferSchedulingServiceTest {

    @SpyBean
    private FinancialTransferSchedulingService financialTransferSchedulingService;
    @SpyBean
    private TransferTypeAService transferTypeAService;
    @SpyBean
    private TransferTypeBService transferTypeBService;
    @SpyBean
    private TransferTypeCService transferTypeCService;
    @SpyBean
    private TransferTypeDService transferTypeDService;

    private FinancialTransferScheduling financialTransferScheduling;

    @Before
    public void setUp() {
        financialTransferScheduling = new FinancialTransferScheduling();
    }

    @Test
    public void getTransferTypeAServiceInstance() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.A);
        CalculationType calculationType = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);

        assertTrue(calculationType instanceof TransferTypeAService);

        assertFalse(calculationType instanceof TransferTypeBService);
        assertFalse(calculationType instanceof TransferTypeCService);
    }

    @Test
    public void getTransferTypeBServiceInstance() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.B);
        CalculationType calculationType = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);

        assertTrue(calculationType instanceof TransferTypeBService);

        assertFalse(calculationType instanceof TransferTypeAService);
        assertFalse(calculationType instanceof TransferTypeCService);
    }

    @Test
    public void getTransferTypeCServiceInstance() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.C);
        CalculationType calculationType = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);

        assertTrue(calculationType instanceof TransferTypeCService);

        assertFalse(calculationType instanceof TransferTypeAService);
        assertFalse(calculationType instanceof TransferTypeBService);
    }

    @Test
    public void getTransferTypeByTransferTypeD() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.D);

        financialTransferScheduling.setValue(new BigDecimal(20000));
        CalculationType calculationTypeA = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);
        assertTrue(calculationTypeA instanceof TransferTypeAService);
        assertFalse(calculationTypeA instanceof TransferTypeBService);
        assertFalse(calculationTypeA instanceof TransferTypeCService);

        financialTransferScheduling.setValue(new BigDecimal(25000));
        CalculationType calculationTypeA2 = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);
        assertTrue(calculationTypeA2 instanceof TransferTypeAService);
        assertFalse(calculationTypeA2 instanceof TransferTypeBService);
        assertFalse(calculationTypeA2 instanceof TransferTypeCService);

        financialTransferScheduling.setValue(new BigDecimal(25001));
        CalculationType calculationTypeB = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);
        assertTrue(calculationTypeB instanceof TransferTypeBService);
        assertFalse(calculationTypeB instanceof TransferTypeAService);
        assertFalse(calculationTypeB instanceof TransferTypeCService);

        financialTransferScheduling.setValue(new BigDecimal(90000));
        CalculationType calculationTypeB2 = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);
        assertTrue(calculationTypeB2 instanceof TransferTypeBService);
        assertFalse(calculationTypeB2 instanceof TransferTypeAService);
        assertFalse(calculationTypeB2 instanceof TransferTypeCService);

        financialTransferScheduling.setValue(new BigDecimal(120000));
        CalculationType calculationTypeB3 = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);
        assertTrue(calculationTypeB3 instanceof TransferTypeBService);
        assertFalse(calculationTypeB3 instanceof TransferTypeAService);
        assertFalse(calculationTypeB3 instanceof TransferTypeCService);

        financialTransferScheduling.setValue(new BigDecimal(120001));
        CalculationType calculationTypeC = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);
        assertTrue(calculationTypeC instanceof TransferTypeCService);
        assertFalse(calculationTypeC instanceof TransferTypeAService);
        assertFalse(calculationTypeC instanceof TransferTypeBService);

        financialTransferScheduling.setValue(new BigDecimal(200000));
        CalculationType calculationTypeC2 = financialTransferSchedulingService.getCalculationTypeByTransferType(financialTransferScheduling);
        assertTrue(calculationTypeC2 instanceof TransferTypeCService);
        assertFalse(calculationTypeC2 instanceof TransferTypeAService);
        assertFalse(calculationTypeC2 instanceof TransferTypeBService);
    }

    @Test
    public void calculateFeeTransferTypeA() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.A);

        financialTransferScheduling.setValue(new BigDecimal(100));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(12756));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("384.68")) == 0);
    }

    @Test
    public void calculateFeeTransferTypeB() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.B);

        financialTransferScheduling.setValue(new BigDecimal(100));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(10));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("10")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(100));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(30));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("10")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(100));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(31));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("8")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(100));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(35));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("8")) == 0);
    }

    @Test
    public void calculateFeeTransferTypeC() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.C);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now());
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("41.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(5));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("41.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(6));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("37")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(10));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("37")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(11));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("33.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(15));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("33.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(16));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("27")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(20));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("27")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(21));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("21.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(25));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("21.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(26));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("10.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(30));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("10.5")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(31));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("6")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(500));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(35));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("6")) == 0);
    }

    @Test
    public void calculateFeeTransferTypeD() throws Exception {
        financialTransferScheduling.setTransferType(TransferType.D);

        financialTransferScheduling.setValue(new BigDecimal(20000));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("602")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(25000));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("752")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(25001));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(10));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("10")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(50000));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(10));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("10")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(120000));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(35));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("8")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(120001));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(10));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("8880.07")) == 0);

        financialTransferScheduling.setValue(new BigDecimal(150000));
        financialTransferScheduling.setScheduleDate(LocalDate.now().plusDays(26));
        financialTransferSchedulingService.calculateFee(financialTransferScheduling);
        assertTrue(financialTransferScheduling.getFee().compareTo(new BigDecimal("3150")) == 0);
    }

    @Test
    public void scheduleTransfer() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = LocalDate.now().format(formatter);

        Message message = financialTransferSchedulingService.scheduleTransfer("12345-6","12345-6",new BigDecimal(500),formattedString,"A");

        assertTrue(message.getSuccess() == true && message.getMessage().equals("Agendamento realizado com sucesso!") && message.getInputMessages().isEmpty());
    }

    @Test
    public void listSchedules() throws Exception {
        List<FinancialTransferScheduling> schedules = financialTransferSchedulingService.listSchedules();
        assertTrue(schedules != null);
    }

}
