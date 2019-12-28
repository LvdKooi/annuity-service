package nl.kooi.domain;

import nl.kooi.dto.PeriodicPaymentDto;
import nl.kooi.dto.RepaymentScheduleDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepaymentSchedule {
    private Loan loan;
    private List<PeriodicPayment> paymentsList;

    public RepaymentSchedule(Loan loan) {
        this.loan = loan;
        setPaymentList(loan);
    }

    public List<PeriodicPayment> getPaymentsList() {
        return paymentsList;
    }

    public Loan getLoan(){
        return this.loan;
    }

    private void setPaymentList(Loan loan) {
        paymentsList = new ArrayList<>();
        int numberOfPayments = PeriodicPayment.of(loan, 1).getNumberOfPayments();

        for (int i = 1; i <= numberOfPayments; i++) {
            paymentsList.add(PeriodicPayment.of(loan, i));
        }
    }

    public RepaymentScheduleDto toDto() {
        RepaymentScheduleDto dto = new RepaymentScheduleDto();
        List<PeriodicPaymentDto> periodicPaymentDtoList = paymentsList.stream().map(PeriodicPayment::toDto).collect(Collectors.toList());

        dto.loan = loan.toDto();
        dto.paymentsList = periodicPaymentDtoList;

        return dto;

    }
}