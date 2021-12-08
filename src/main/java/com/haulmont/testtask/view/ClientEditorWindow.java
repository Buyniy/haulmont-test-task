package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Client;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class ClientEditorWindow extends Window {

    public ClientEditorWindow(Client client, MainUI.OPTIONS options) {
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
        Binder<Client> binder = new Binder<>(Client.class);

        //Поля редактирования
        TextField nameField = new TextField();
        nameField.setCaption("ФИО");
        TextField phoneField = new TextField();
        phoneField.setCaption("Телефон");
        TextField emailField = new TextField();
        emailField.setCaption("E-Mail");
        TextField passportField = new TextField();
        passportField.setCaption("Паспорт");

        //Кнопки
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");

        //Валидация полей для изменяемого объекта
        binder.forField(nameField)
                .withValidator(str -> str.length() <= 64, "Максимальная длина - 64 знака")
                .withValidator(str -> str.matches("^[a-zA-Zа-яА-Я]+[\\-'\\s]?[a-zA-Zа-яА-Я ]+$"),
                        "ФИО может содержать только буквы и пробелы")
                .asRequired("Обязательное значение")
                .bind("fullName");

        binder.forField(phoneField)
                .withValidator(str -> str.length() <= 16, "Максимальная длина - 16 знаков")
                .withValidator(str -> str.matches("^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$"),
                        "Телефон может содержать только цифры, пробел, +, -, (, )")
                .asRequired("Обязательное значение")
                .bind("phoneNumber");

        binder.forField(emailField)
                .withValidator(str -> str.length() <= 64, "Максимальная длина - 64 знака")
                .withValidator(str -> str.matches("^[-\\w.]+@([A-z0-9][-A-z0-9]+\\.)+[A-z]{2,4}$"),
                        "E-Mail может содержать только латинские буквы, цифры, точку, -, _, @")
                .asRequired("Обязательное значение")
                .bind("emailAddress");

        binder.forField(passportField)
                .withValidator(str -> str.length() <= 6, "Максимальная длина - 6 знаков")
                .withValidator(str -> str.matches("^([0-9]{6})?$"), "Номер паспорта может содержать только 6 цифр")
                .asRequired("Обязательное значение")
                .bind("passportNumber");

        okButton.setEnabled(false);

        binder.addStatusChangeListener(statusChangeEvent -> okButton.setEnabled(!statusChangeEvent.hasValidationErrors()));

        //Установка значений переданного объекта в поля редактирования
        binder.readBean(client);

        //Логика для кнопок
        okButton.addClickListener(clickEvent -> {
            try {
                binder.writeBean(client);
                if (options == MainUI.OPTIONS.UPDATE) Controller.updateClient(client);
                if (options == MainUI.OPTIONS.ADD) Controller.addClient(client);
                this.close();
            } catch (ValidationException e) {
                System.out.println("Возникла ошибка при добавлении клиента");
                e.printStackTrace();
            }
        });

        cancelButton.addClickListener(clickEvent -> this.close());

        //Добавление компонентов
        buttonsLayout.addComponent(okButton);
        buttonsLayout.addComponent(cancelButton);

        contentLayout.addComponent(nameField);
        contentLayout.addComponent(phoneField);
        contentLayout.addComponent(emailField);
        contentLayout.addComponent(passportField);
        contentLayout.addComponent(buttonsLayout);

        setModal(true);
        setResizable(false);
        setClosable(false);
        setContent(contentLayout);
        center();
    }
}