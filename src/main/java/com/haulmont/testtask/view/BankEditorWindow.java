package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Bank;
import com.haulmont.testtask.model.Client;
import com.haulmont.testtask.model.Credit;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;

public class BankEditorWindow extends Window {

    public BankEditorWindow(Bank bank, MainUI.OPTIONS options) {
        super();
        setHeight("390px");
        setWidth("225px");

        //Установка заголовка
        String caption;
        if (options == MainUI.OPTIONS.ADD) caption = "Добавить";
        else caption = "Изменить";
        setCaption(caption);

        //Установка разметки содержимого
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        //Binder для переданного объекта
        Binder<Bank> binder = new Binder<>(Bank.class);

        //Поля редактирования
        TextField bankField = new TextField();
        bankField.setCaption("Банк");
        ComboBox<Client> clientComboBox = new ComboBox();
        clientComboBox.setCaption("Клиент");
        ComboBox<Credit> creditComboBox = new ComboBox();
        creditComboBox.setCaption("Кредит");

        //Кнопки
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");

        //Списки элементов для всех ComboBox
        clientComboBox.setItems(Controller.getClientList());
        clientComboBox.setItemCaptionGenerator(client -> client.getFullName());
        clientComboBox.setEmptySelectionAllowed(true);
        clientComboBox.setEmptySelectionCaption("---");
        creditComboBox.setItems(Controller.getCreditList());
        creditComboBox.setItemCaptionGenerator(item -> item.getCreditLimit() + " " + item.getPercentRate() + "%");
        creditComboBox.setEmptySelectionAllowed(true);
        creditComboBox.setEmptySelectionCaption("---");

        //Валидация полей для изменяемого объекта
        binder.forField(bankField)
                .withValidator(str -> str.length() <= 64, "Максимальная длина - 64 знака")
                .withValidator(str -> str.matches("[a-zA-Zа-яА-Я]+"),
                        "Название банка может содержать только буквы")
                .asRequired("Обязательное значение")
                .bind("bankName");

        binder.forField(clientComboBox)
                .asRequired("Обязательное значение")
                .bind("client");

        binder.forField(creditComboBox)
                .asRequired("Обязательное значение")
                .bind("credit");

        okButton.setEnabled(false);

        binder.addStatusChangeListener(statusChangeEvent -> okButton.setEnabled(!statusChangeEvent.hasValidationErrors()));

        //Установка значений переданного объекта в поля редактирования
        binder.readBean(bank);

        //Логика для кнопок
        okButton.addClickListener(clickEvent -> {
            try {
                binder.writeBean(bank);
                if (options == MainUI.OPTIONS.UPDATE) Controller.updateBank(bank);
                if (options == MainUI.OPTIONS.ADD) Controller.addBank(bank);
                this.close();
            } catch (ValidationException e) {
                System.out.println("Возникла ошибка при добавлении банка");
                e.printStackTrace();
            }
        });

        cancelButton.addClickListener(clickEvent -> this.close());

        //Добавление компонентов
        buttonsLayout.addComponent(okButton);
        buttonsLayout.addComponent(cancelButton);

        contentLayout.addComponent(bankField);
        contentLayout.addComponent(clientComboBox);
        contentLayout.addComponent(creditComboBox);
        contentLayout.addComponent(buttonsLayout);

        setModal(true);
        setResizable(false);
        setClosable(false);
        setContent(contentLayout);
        center();
    }
}
