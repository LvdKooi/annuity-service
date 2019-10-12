package nl.kooi.domain;

import nl.kooi.dto.LoanDto;
import nl.kooi.dto.PeriodicPaymentDto;

import java.math.BigDecimal;

public class PeriodicPayment {
    private static BigDecimal totalPayment;
    private Integer period;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;
    private LoanDto loan;

    public PeriodicPayment(LoanDto loan, Integer period) {
        this.period = period;
        this.loan = loan;
        setTotalPayment();
    }

    private void determineInterestAndRepayment() {


    }

    private void setTotalPayment() {
        if (totalPayment == null) {
            BigDecimal interest = loan.interestPercentage.divide(new BigDecimal(100));
            BigDecimal cashValue = loan.initialLoan.divide((BigDecimal.ONE.divide(interest)));
            BigDecimal discountRate = interest.divide(interest.add(BigDecimal.ONE));
            totalPayment = cashValue.divide(discountRate);
        }
    }

    public PeriodicPaymentDto toDto() {
        PeriodicPaymentDto dto = new PeriodicPaymentDto() {
        };
        dto.period = period;
        dto.interestAmount = interestAmount;
        dto.repaymentAmount = repaymentAmount;
        return dto;
    }


}

