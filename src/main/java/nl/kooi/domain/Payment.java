package nl.kooi.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
@Setter
public class Payment {
    private BigDecimal totalPayment;
    private BigDecimal interestAmount;
    private BigDecimal repaymentAmount;

}
