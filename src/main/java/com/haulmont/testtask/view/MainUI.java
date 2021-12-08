package com.haulmont.testtask.view;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
    public enum OPTIONS {
        UPDATE,
        ADD
    }

    @Override
    protected void init(VaadinRequest request) {
        AbsoluteLayout contentLayout = new AbsoluteLayout();
        contentLayout.setSizeFull();

        Label BankApp = new Label("Банковское приложение");
        BankApp.addStyleName(ValoTheme.LABEL_H1);

        VerticalLayout Buttons = new VerticalLayout();
        Button bankButton = new Button("Банк");
        Button clientsButton = new Button("Клиенты");
        Button creditsButton = new Button("Кредиты");
        Button creditOfferButton = new Button("Кредитное предложение");

        //Логика кнопок
        bankButton.addClickListener(clickEvent -> addWindow(new BanksWindow()));

        clientsButton.addClickListener(clickEvent -> addWindow(new ClientsWindow()));

        creditsButton.addClickListener(clickEvent -> addWindow(new CreditsWindow()));

        creditOfferButton.addClickListener(clickEvent -> addWindow(new CreditOffersWindow()));

        //Добавление компонентов
        contentLayout.addComponent(BankApp, "left: 50px");
        Buttons.addComponent(bankButton);
        Buttons.addComponent(clientsButton);
        Buttons.addComponent(creditsButton);
        Buttons.addComponent(creditOfferButton);
        contentLayout.addComponent(Buttons, "left: 50px; top: 130px");

        setContent(contentLayout);
    }
}