package nl.kooi.domain;

import nl.kooi.dto.PaymentDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Payment {
    private BigDecimal totalPayment;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;


    public Payment(BigDecimal totalPayment, BigDecimal interestAmount, BigDecimal repaymentAmount) {
        this.totalPayment = totalPayment;
        this.interestAmount = interestAmount;
        this.repaymentAmount = repaymentAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return totalPayment.equals(payment.totalPayment) &&
                interestAmount.equals(payment.interestAmount) &&
                repaymentAmount.equals(payment.repaymentAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalPayment, interestAmount, repaymentAmount);
    }

    public BigDecimal getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(BigDecimal totalPayment) {
        this.totalPayment = totalPayment;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal interestAmount) {
        this.interestAmount = interestAmount;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public PaymentDto toDto(){

        PaymentDto dto = new PaymentDto();
        dto.interestAmount = interestAmount.setScale(5, RoundingMode.HALF_UP);
        dto.repaymentAmount = repaymentAmount.setScale(5, RoundingMode.HALF_UP);
        dto.totalPayment = totalPayment.setScale(5, RoundingMode.HALF_UP);
        return dto;

    }
}
