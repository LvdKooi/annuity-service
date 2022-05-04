package nl.kooi.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.kooi.api.dto.ErrorResponseDto;
import nl.kooi.api.dto.Mapper;
import nl.kooi.api.dto.RepaymentScheduleDto;
import nl.kooi.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitConfig(AnnuityController.class)
class AnnuityControllerTest {

    @Autowired
    private AnnuityController annuityController;

    @MockBean
    private RepaymentScheduleService repaymentScheduleService;

    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void initTestDependencies() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders
                .standaloneSetup(annuityController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }


    @Test
    public void getRepaymentScheduleTest() throws Exception {
        when(repaymentScheduleService.getRepaymentScheduleForLoan(any()))
                .thenReturn(getRepaymentSchedule());

        var mvcResult = mockMvc.perform(post("/annuity/repayment-schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json("{\n" +
                        "'annualInterestPercentage': 10,\n" +
                        "'initialLoan': 100,\n" +
                        "'loanTermInMonths': 36,\n" +
                        "'periodicity': 'ANNUALLY',\n" +
                        "'startDate': '2020-01-01',\n" +
                        "'timing': 'DUE', \n" +
                        "'repaymentType': 'ANNUITY'\n" +
                        "}")))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        var response = objectMapper.readValue(mvcResult.getContentAsString(), RepaymentScheduleDto.class);

        assertThat(response).isEqualTo(Mapper.map(getRepaymentSchedule()));

    }

    @Test
    public void getRepaymentScheduleExceptionTest() throws Exception {
        when(repaymentScheduleService.getRepaymentScheduleForLoan(any())).thenThrow(new RuntimeException("Bad request!"));

        var mvcResult = mockMvc.perform(post("/annuity/repayment-schedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json("{\n" +
                        "'annualInterestPercentage': 10,\n" +
                        "'initialLoan': 100,\n" +
                        "'loanTermInMonths': 36,\n" +
                        "'periodicity': 'ANNUALLY',\n" +
                        "'startDate': '2020-01-01',\n" +
                        "'timing': 'DUE', \n" +
                        "'repaymentType': 'ANNUITY'\n" +
                        "}")))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();

        var response = objectMapper.readValue(mvcResult.getContentAsString(), ErrorResponseDto.class);

        assertThat(response.getReason()).isEqualTo("Bad request!");
    }

    private Loan getLoan() {
        return getLoan(RepaymentType.ANNUITY);
    }

    private Loan getLoan(RepaymentType repaymentType) {
        return Loan.builder()
                .initialLoan(BigDecimal.TEN)
                .annualInterestPercentage(BigDecimal.ONE)
                .periodicity(Periodicity.ANNUALLY)
                .startdate(LocalDate.of(2021, 4, 1))
                .loanTerm(Period.ofMonths(12))
                .timing(Timing.IMMEDIATE)
                .repaymentType(repaymentType)
                .build();
    }

    private RepaymentSchedule getRepaymentSchedule() {
        return new RepaymentSchedule(getLoan(), List.of(LoanStatement.of(getLoan(), 1)));
    }

    private static String json(String singleQuotedJson) {
        return singleQuotedJson.replaceAll("'", "\"");
    }

}