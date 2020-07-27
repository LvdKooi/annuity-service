package nl.kooi.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import nl.kooi.dto.PaymentDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class Payment {
    private BigDecimal totalPayment;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;

    public PaymentDto toDto(){

        var dto = new PaymentDto();
        dto.interestAmount = interestAmount.setScale(5, RoundingMode.HALF_UP);
        dto.repaymentAmount = repaymentAmount.setScale(5, RoundingMode.HALF_UP);
        dto.totalPayment = totalPayment.setScale(5, RoundingMode.HALF_UP);
        return dto;

    }
}
