package nl.kooi.domain;

import nl.kooi.dto.PeriodicPaymentDto;

import java.math.BigDecimal;

public class PeriodicPayment {
    private static BigDecimal totalPayment;
    private Integer period;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;
    private Loan loan;

    public PeriodicPayment(Loan loan, Integer period) {
        this.period = period;
        this.loan = loan;
        setTotalPayment();
    }

//    TODO: implement me!
    private void determineInterestAndRepaymentOfPeriod() {


    }

    //TODO: refactor me!
    private void setTotalPayment() {
        if (totalPayment == null) {
            BigDecimal cashValue = loan.getInitialLoan().divide((BigDecimal.ONE.divide(loan.getAnnualInterestRate())));
            BigDecimal discountRate = loan.getAnnualInterestRate().divide(loan.getAnnualInterestRate().add(BigDecimal.ONE));
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
