package com.haulmont.testtask.view;

import com.haulmont.testtask.model.CreditOffer;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Window;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentPlanWindow extends Window {

    public PaymentPlanWindow(CreditOffer creditOffer) {
        super("График платежей");
        setHeight("400px");
        setWidth("600px");

        AbsoluteLayout contentLayout = new AbsoluteLayout();
        contentLayout.setSizeFull();

        Button okButton = new Button("Закрыть");
        okButton.addClickListener(clickEvent -> PaymentPlanWindow.this.close());

        Grid<PaymentPlan> grid = new Grid<>();

        List<PaymentPlan> planList = new ArrayList<>();
        for (int i = 0; i < creditOffer.getCountPayment(); i++) {
            LocalDate localDate = creditOffer.getDatePayment().plusMonths(i + 1);
            planList.add(new PaymentPlan(localDate,
                    creditOffer.getPaymentAmount(),
                    creditOffer.getBodyAmount(),
                    creditOffer.getPercentAmount())
            );
        }

        grid.setDataProvider(new ListDataProvider<>(planList));

        grid.setHeight("84%");
        grid.setWidth("98%");
        grid.addColumn(PaymentPlan::getDatePayment).setCaption("Дата платежа");
        grid.addColumn(PaymentPlan::getPaymentAmount).setCaption("Сумма платежа");
        grid.addColumn(PaymentPlan::getBodyAmount).setCaption("Основной долг");
        grid.addColumn(PaymentPlan::getPercentAmount).setCaption("Проценты");

        contentLayout.addComponent(grid, "top: 2%; left: 2%;");
        contentLayout.addComponent(okButton, "top: 87%; left: 42%");
        setModal(true);
        setResizable(false);
        center();
        setContent(contentLayout);
    }

    public static class PaymentPlan {

        final LocalDate datePayment;
        final double paymentAmount;
        final double bodyAmount;
        final double percentAmount;

        public PaymentPlan(LocalDate datePayment, double paymentAmount, double bodyAmount, double percentAmount) {
            this.datePayment = datePayment;
            this.paymentAmount = paymentAmount;
            this.bodyAmount = bodyAmount;
            this.percentAmount = percentAmount;
        }

        public LocalDate getDatePayment() {
            return datePayment;
        }

        public double getPaymentAmount() {
            return paymentAmount;
        }

        public double getBodyAmount() {
            return bodyAmount;
        }

        public double getPercentAmount() {
            return percentAmount;
        }
    }
}
