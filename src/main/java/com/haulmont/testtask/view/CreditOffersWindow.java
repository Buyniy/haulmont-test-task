package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.CreditOffer;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.concurrent.atomic.AtomicReference;

@Theme(ValoTheme.THEME_NAME)
public class CreditOffersWindow extends Window {

    private final Grid<CreditOffer> grid = new Grid<>();
    private final ListDataProvider<CreditOffer> creditOfferList;

    public CreditOffersWindow() {
        super("Кредитное предложение");
        setHeight("400px");
        setWidth("800px");

        //Содержимое окна: кнопки и таблица
        AbsoluteLayout contentLayout = new AbsoluteLayout();
        contentLayout.setSizeFull();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button addButton = new Button("Добавить");
        Button changeButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");
        Button planButton = new Button("График платежей");

        //Текущий выбранный объект
        AtomicReference<CreditOffer> selectedCreditOffer = new AtomicReference<>();

        //Кнопки изменить/удалить заблокированы до выбора значения
        changeButton.setEnabled(false);
        deleteButton.setEnabled(false);
        planButton.setEnabled(false);

        //Установка значений в таблице
        creditOfferList = DataProvider.ofCollection(Controller.getCreditOfferList());
        grid.setDataProvider(creditOfferList);

        //Установка колонок в таблице
        grid.setHeight("80%");
        grid.setWidth("98%");
        grid.addColumn(CreditOffer::getClientName).setCaption("Клиент");
        grid.addColumn(CreditOffer::getCreditLimitRate).setCaption("Кредит");
        grid.addColumn(CreditOffer::getCreditAmount).setCaption("Сумма кредита");
        grid.addColumn(CreditOffer::getDateCreditOffer).setCaption("Дата кредитования");

        //Выбор элемента таблицы: если выбран какой-либо элемент
        //Есть возможность отредактировать или удалить его
        grid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            selectedCreditOffer.set(valueChangeEvent.getValue());
            if (valueChangeEvent.getValue() != null) {
                changeButton.setEnabled(true);
                deleteButton.setEnabled(true);
                planButton.setEnabled(true);
                System.out.println("Выбранное кредитное предложение: " + valueChangeEvent.getValue().getId());
            } else {
                changeButton.setEnabled(false);
                deleteButton.setEnabled(false);
                planButton.setEnabled(false);
            }
        });

        //Логика кнопок добавления/изменения/удаления
        addButton.addClickListener(clickEvent -> {
            selectedCreditOffer.set(new CreditOffer(-1, null, null, 0, 0,
                    null, null, 0, 0, 0));
            CreditOfferEditorWindow creditOfferEditorWindow = new CreditOfferEditorWindow(selectedCreditOffer.get(),
                    MainUI.OPTIONS.ADD);
            //Обновление после закрытия
            creditOfferEditorWindow.addCloseListener(closeEvent -> {
                creditOfferList.getItems().clear();
                creditOfferList.getItems().addAll(Controller.getCreditOfferList());
                creditOfferList.refreshAll();
            });
            getUI().addWindow(creditOfferEditorWindow);
        });

        changeButton.addClickListener(clickEvent -> {
            CreditOffer creditOffer = selectedCreditOffer.get();
            CreditOfferEditorWindow creditOfferEditorWindow = new CreditOfferEditorWindow(creditOffer,
                    MainUI.OPTIONS.UPDATE);
            //Обновление после закрытия
            creditOfferEditorWindow.addCloseListener(closeEvent -> creditOfferList.refreshItem(creditOffer));
            getUI().addWindow(creditOfferEditorWindow);
        });

        deleteButton.addClickListener(clickEvent -> {
            CreditOffer creditOffer = selectedCreditOffer.get();
            Controller.deleteCreditOffer(creditOffer.getId());
            creditOfferList.getItems().remove(creditOffer);
            creditOfferList.refreshAll();
        });

        planButton.addClickListener(clickEvent -> getUI().addWindow(new PaymentPlanWindow(grid.asSingleSelect().getValue())));

        //Установка содержимого
        contentLayout.addComponent(grid, "top: 2%; left: 2%;");
        buttonsLayout.addComponent(addButton);
        buttonsLayout.addComponent(changeButton);
        buttonsLayout.addComponent(deleteButton);
        buttonsLayout.addComponent(planButton);
        contentLayout.addComponent(buttonsLayout, "top: 86%; left: 2%;");

        center();
        setContent(contentLayout);
    }
}
