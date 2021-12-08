package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Credit;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

@Theme(ValoTheme.THEME_NAME)
public class CreditEditorWindow extends Window {

    public CreditEditorWindow(Credit credit, MainUI.OPTIONS options) {
        super();
        setHeight("270px");
        setWidth("220px");

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
        Binder<Credit> binder = new Binder<>(Credit.class);

        //Поля редактирования
        TextField creditLimitField = new TextField();
        creditLimitField.setCaption("Лимит по кредиту");
        creditLimitField.setPlaceholder("₽");
        TextField percentRateField = new TextField();
        percentRateField.setCaption("Процентная ставка");
        percentRateField.setPlaceholder("%");

        //Кнопки
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");

        //Валидация полей для изменяемого объекта
        binder.forField(creditLimitField)
                .withConverter(Double::valueOf, String::valueOf)
                .withValidator(str -> str >= 0, "Лимит по кредиту не может быть отрицательным")
                .withValidator(str -> str < 10000000, "Лимит по кредиту не может быть больше 10 миллионов")
                .asRequired("Обязательное значение")
                .bind("creditLimit");

        binder.forField(percentRateField)
                .withConverter(Float::valueOf, String::valueOf)
                .withValidator(str -> str >= 0, "Процентная ставка не может быть отрицательной")
                .withValidator(str -> str <= 100, "Процентная ставка не может быть больше 100%")
                .asRequired("Обязательное значение")
                .bind("percentRate");

        okButton.setEnabled(false);

        binder.addStatusChangeListener(statusChangeEvent -> okButton.setEnabled(!statusChangeEvent.hasValidationErrors()));

        //Установка значений переданного объекта в поля редактирования
        binder.readBean(credit);

        //Логика для кнопок
        okButton.addClickListener(clickEvent -> {
            try {
                binder.writeBean(credit);
                if (options == MainUI.OPTIONS.UPDATE) Controller.updateCredit(credit);
                if (options == MainUI.OPTIONS.ADD) Controller.addCredit(credit);
                this.close();
            } catch (ValidationException e) {
                System.out.println("Возникла ошибка при добавлении кредита");
                e.printStackTrace();
            }
        });

        cancelButton.addClickListener(clickEvent -> this.close());

        //Добавление компонентов
        buttonsLayout.addComponent(okButton);
        buttonsLayout.addComponent(cancelButton);

        contentLayout.addComponent(creditLimitField);
        contentLayout.addComponent(percentRateField);
        contentLayout.addComponent(buttonsLayout);

        setModal(true);
        setResizable(false);
        setClosable(false);
        setContent(contentLayout);
        center();
    }
}