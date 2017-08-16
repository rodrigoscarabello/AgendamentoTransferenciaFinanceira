package br.com.reservaFacil.controller;

import br.com.reservaFacil.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RunWith(SpringRunner.class)
@WebMvcTest(value = FinancialTransferSchedulingController.class)
public class FinancialTransferSchedulingControllerTest {

    private static final String RETURN_JSON_TRANSFER_TYPE_LIST = "[\"A\", \"B\", \"C\", \"D\"]";
    private static final String RETURN_JSON_TRANSFER_LIST = "[{\"id\":1,\"originAccount\":\"12345-6\",\"destinationAccount\":\"12345-6\",\"value\":100,\"fee\":5.00,\"scheduleDate\":{\"year\":2017,\"month\":\"OCTOBER\",\"monthValue\":10,\"era\":\"CE\",\"dayOfMonth\":1,\"dayOfWeek\":\"SUNDAY\",\"dayOfYear\":274,\"leapYear\":false,\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\"}},\"registrationDate\":{\"year\":2017,\"month\":\"AUGUST\",\"monthValue\":8,\"era\":\"CE\",\"dayOfMonth\":16,\"dayOfWeek\":\"WEDNESDAY\",\"dayOfYear\":228,\"leapYear\":false,\"chronology\":{\"id\":\"ISO\",\"calendarType\":\"iso8601\"}},\"transferType\":\"A\"}]";
    private static final String RETURN_JSON_SUCCESS_SCHEDULE_TRANSFER = "{\"success\":true,\"message\":\"Agendamento realizado com sucesso!\",\"inputMessages\":[]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_PARAMS = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"originAccount\",\"message\":\"Necessário informar a conta de origem\"},{\"inputName\":\"destinationAccount\",\"message\":\"Necessário informar a conta de destino\"},{\"inputName\":\"scheduleDate\",\"message\":\"Necessário informar a data da transferência\"},{\"inputName\":\"value\",\"message\":\"Necessário informar o valor da transferência\"},{\"inputName\":\"transferType\",\"message\":\"Necessário informar o tipo da transferência\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_ORIGIN = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"originAccount\",\"message\":\"Necessário informar a conta de origem\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_DESTINATION = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"destinationAccount\",\"message\":\"Necessário informar a conta de destino\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_DATE = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"scheduleDate\",\"message\":\"Necessário informar a data da transferência\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_VALUE = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"value\",\"message\":\"Necessário informar o valor da transferência\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_TRANSFER_TYPE = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"transferType\",\"message\":\"Necessário informar o tipo da transferência\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_ORIGIN_DESTINATION_OUT_OF_PATTERN = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"originAccount\",\"message\":\"O número da conta deve estar no padrão XXXXX-X\"},{\"inputName\":\"destinationAccount\",\"message\":\"O número da conta deve estar no padrão XXXXX-X\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_INVALID_DATE = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"scheduleDate\",\"message\":\"Valor incorreto, favor verificar o valor informado\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_INFERIOR_DATE = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"scheduleDate\",\"message\":\"A data para transferência deve ser superior a hoje\"}]}";
    private static final String RETURN_JSON_FAIL_SCHEDULE_TRANSFER_INVALID_TRANSFER_TYPE = "{\"success\":false,\"message\":null,\"inputMessages\":[{\"inputName\":\"transferType\",\"message\":\"Valor incorreto, favor verificar o valor informado\"}]}";

    @Autowired
    private MockMvc mvc;

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

    @Test
    public void scheduleTransferWithNoParams() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/schedule").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_PARAMS, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithNoOrigin() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = LocalDate.now().format(formatter);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("destinationAccount", "12345-6");
        params.add("value", "100");
        params.add("scheduleDate", formattedString);
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_ORIGIN, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithNoDestination() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = LocalDate.now().format(formatter);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("value", "100");
        params.add("scheduleDate", formattedString);
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_DESTINATION, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithNoDate() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("destinationAccount", "12345-6");
        params.add("value", "100");
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_DATE, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithNoValue() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = LocalDate.now().format(formatter);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("destinationAccount", "12345-6");
        params.add("scheduleDate", formattedString);
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_VALUE, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithNoTransferType() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedString = LocalDate.now().format(formatter);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("destinationAccount", "12345-6");
        params.add("value", "100");
        params.add("scheduleDate", formattedString);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_NO_TRANSFER_TYPE, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithOriginDestinaionOutOfPattern() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "123456");
        params.add("destinationAccount", "12345");
        params.add("value", "100");
        params.add("scheduleDate", "01/10/2017");
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_ORIGIN_DESTINATION_OUT_OF_PATTERN, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithInvalidDate() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("destinationAccount", "12345-6");
        params.add("value", "100");
        params.add("scheduleDate", "01/10/201");
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_INVALID_DATE, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithInferiorDate() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("destinationAccount", "12345-6");
        params.add("value", "100");
        params.add("scheduleDate", "01/08/2017");
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_INFERIOR_DATE, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void scheduleTransferWithInvalidTransferType() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("destinationAccount", "12345-6");
        params.add("value", "100");
        params.add("scheduleDate", "01/10/2017");
        params.add("transferType", "E");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_FAIL_SCHEDULE_TRANSFER_INVALID_TRANSFER_TYPE, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void successfulScheduleTransfer() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("originAccount", "12345-6");
        params.add("destinationAccount", "12345-6");
        params.add("value", "100");
        params.add("scheduleDate", "01/10/2017");
        params.add("transferType", "A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/schedule").accept(MediaType.APPLICATION_JSON).params(params);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_SUCCESS_SCHEDULE_TRANSFER, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void retrieveListTransferTypes() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/listTransferTypes").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        JSONAssert.assertEquals(RETURN_JSON_TRANSFER_TYPE_LIST, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void retrieveListSchedules() throws Exception {
//        financialTransferSchedulingService.scheduleTransfer("12345-6","12345-6",new BigDecimal(100),"01/10/2017","A");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/listSchedules").accept(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        JSONAssert.assertEquals(RETURN_JSON_TRANSFER_LIST, result.getResponse().getContentAsString(), false);
    }

}
