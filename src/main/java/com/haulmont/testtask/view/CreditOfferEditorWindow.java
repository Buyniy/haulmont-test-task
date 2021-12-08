package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Client;
import com.haulmont.testtask.model.Credit;
import com.haulmont.testtask.model.CreditOffer;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.ui.*;

public class CreditOfferEditorWindow extends Window {

    public CreditOfferEditorWindow(CreditOffer creditOffer, MainUI.OPTIONS options) {
        super();
        setHeight("390px");
        setWidth("450px");

        //Установка заголовка
        String caption;
        if (options == MainUI.OPTIONS.ADD) caption = "Добавить";
        else caption = "Изменить";
        setCaption(caption);

        //Установка разметки содержимого
        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        HorizontalLayout firstLayout = new HorizontalLayout();
        HorizontalLayout secondLayout = new HorizontalLayout();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        //Binder для переданного объекта
        Binder<CreditOffer> binder = new Binder<>(CreditOffer.class);

        //Поля редактирования
        ComboBox<Client> clientComboBox = new ComboBox();
        clientComboBox.setCaption("Клиент");
        ComboBox<Credit> creditComboBox = new ComboBox();
        creditComboBox.setCaption("Кредит");
        DateField dateCreditOfferField = new DateField();
        dateCreditOfferField.setCaption("Дата кредитования");
        TextField creditAmountField = new TextField();
        creditAmountField.setCaption("Сумма кредита");
        TextField countPaymentField = new TextField();
        countPaymentField.setCaption("Срок кредитования (мес.)");

        //Кнопки
        Button okButton = new Button("OK");
        Button cancelButton = new Button("Отмена");

        //Списки элементов для всех ComboBox
        clientComboBox.setItems(Controller.getClientList());
        clientComboBox.setItemCaptionGenerator(Client::getFullName);
        clientComboBox.setEmptySelectionAllowed(true);
        clientComboBox.setEmptySelectionCaption("---");
        creditComboBox.setItems(Controller.getCreditList());
        creditComboBox.setItemCaptionGenerator(item -> item.getCreditLimit() + " " + item.getPercentRate() + "%");
        creditComboBox.setEmptySelectionAllowed(true);
        creditComboBox.setEmptySelectionCaption("---");

        //Валидация полей для изменяемого объекта
        binder.forField(clientComboBox)
                .asRequired("Обязательное значение")
                .bind("client");

        binder.forField(creditComboBox)
                .asRequired("Обязательное значение")
                .bind("credit");

        binder.forField(dateCreditOfferField)
                .asRequired("Обязательное значение")
                .bind("dateCreditOffer");

        binder.forField(creditAmountField)
                .withConverter(Double::valueOf, String::valueOf)
                .withValidator(str -> str >= 0, "Сумма кредита не может быть отрицательной")
                .asRequired("Обязательное значение")
                .bind("creditAmount");

        binder.forField(countPaymentField)
                .withConverter(Integer::valueOf, String::valueOf)
                .withValidator(str -> str >= 1, "Срок кредитования не меньше 1 месяца, только целое число")
                .asRequired("Обязательное значение")
                .bind("countPayment");

        okButton.setEnabled(false);

        binder.addStatusChangeListener(statusChangeEvent -> okButton.setEnabled(!statusChangeEvent.hasValidationErrors()));

        //Установка значений переданного объекта в поля редактирования
        binder.readBean(creditOffer);

        //Логика для кнопок
        okButton.addClickListener(clickEvent -> {
            try {
                binder.writeBean(creditOffer);
                if (creditOffer.getCreditAmount() > creditOffer.getCreditLimit()) {
                    VerticalLayout errContent = new VerticalLayout();
                    errContent.addComponent(new Label("Сумма кредита не может быть больше лимита кредита!"));
                    Window err = new Window("Ошибка");
                    err.setContent(errContent);
                    err.setModal(true);
                    err.center();
                    getUI().addWindow(err);
                } else {
                    //Расчет итоговой суммы кредита с процентами
                    creditOffer.setCreditAmount(creditOffer.getCreditAmount() +
                            ((creditOffer.getCreditAmount() * creditOffer.getCredit().getPercentRate()) / 100));
                    //Дата следующего платежа
                    creditOffer.setDatePayment(creditOffer.getDateCreditOffer().plusMonths(1));
                    //Расчет суммы платежа
                    creditOffer.setPaymentAmount(creditOffer.getCreditAmount() / creditOffer.getCountPayment());
                    //Расчет суммы гашения тела кредита
                    creditOffer.setBodyAmount(creditOffer.getPaymentAmount() - (creditOffer.getPaymentAmount() *
                            (creditOffer.getCredit().getPercentRate() / 100)));
                    //Расчет суммы гашения тела процентов
                    creditOffer.setPercentAmount(creditOffer.getPaymentAmount() *
                            (creditOffer.getCredit().getPercentRate() / 100));
                    if (options == MainUI.OPTIONS.UPDATE) Controller.updateCreditOffer(creditOffer);
                    if (options == MainUI.OPTIONS.ADD) Controller.addCreditOffer(creditOffer);
                    this.close();
                }
            } catch (ValidationException e) {
                System.out.println("Возникла ошибка при добавлении кредитного предложения");
                e.printStackTrace();
            }
        });

        cancelButton.addClickListener(clickEvent -> this.close());

        //Добавление компонентов
        buttonsLayout.addComponent(okButton);
        buttonsLayout.addComponent(cancelButton);

        firstLayout.addComponent(clientComboBox);
        firstLayout.addComponent(dateCreditOfferField);

        secondLayout.addComponent(creditComboBox);
        secondLayout.addComponent(creditAmountField);

        contentLayout.addComponent(firstLayout);
        contentLayout.addComponent(secondLayout);
        contentLayout.addComponent(countPaymentField);
        contentLayout.addComponent(buttonsLayout);

        setModal(true);
        setResizable(false);
        setClosable(false);
        setContent(contentLayout);
        center();
    }
}