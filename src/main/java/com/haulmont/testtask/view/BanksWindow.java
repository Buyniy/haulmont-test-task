package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Bank;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;

import java.util.concurrent.atomic.AtomicReference;

public class BanksWindow extends Window {

    private final ListDataProvider<Bank> bankList;

    public BanksWindow() {
        super("Банк");
        setHeight("400px");
        setWidth("800px");

        //Содержимое окна: кнопки и таблица
        AbsoluteLayout contentLayout = new AbsoluteLayout();
        contentLayout.setSizeFull();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button addButton = new Button("Добавить");
        Button changeButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");

        //Текущий выбранный объект
        AtomicReference<Bank> selectedBank = new AtomicReference<>();

        //Кнопки изменить/удалить заблокированы до выбора значения
        changeButton.setEnabled(false);
        deleteButton.setEnabled(false);

        //Установка значений в таблице
        bankList = DataProvider.ofCollection(Controller.getBankList());
        Grid<Bank> grid = new Grid<>();
        grid.setDataProvider(bankList);

        //Установка колонок в таблице
        grid.setHeight("80%");
        grid.setWidth("98%");
        grid.addColumn(Bank::getBankName).setCaption("Банк");
        grid.addColumn(Bank::getClientName).setCaption("Клиент");
        grid.addColumn(Bank::getCreditLimitRate).setCaption("Кредит");

        //Выбор элемента таблицы: если выбран какой-либо элемент
        //Есть возможность отредактировать или удалить его
        grid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            selectedBank.set(valueChangeEvent.getValue());
            if (valueChangeEvent.getValue() != null) {
                changeButton.setEnabled(true);
                deleteButton.setEnabled(true);
                System.out.println("Выбранный банк: " + valueChangeEvent.getValue().getId());
            } else {
                changeButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        //Логика кнопок добавления/изменения/удаления
        addButton.addClickListener(clickEvent -> {
            selectedBank.set(new Bank(-1, "", null, null));
            BankEditorWindow bankEditorWindow = new BankEditorWindow(selectedBank.get(), MainUI.OPTIONS.ADD);
            //Обновление после закрытия
            bankEditorWindow.addCloseListener(closeEvent -> {
                bankList.getItems().clear();
                bankList.getItems().addAll(Controller.getBankList());
                bankList.refreshAll();
            });
            getUI().addWindow(bankEditorWindow);
        });

        changeButton.addClickListener(clickEvent -> {
            Bank bank = selectedBank.get();
            BankEditorWindow bankEditorWindow = new BankEditorWindow(bank, MainUI.OPTIONS.UPDATE);
            //Обновление после закрытия
            bankEditorWindow.addCloseListener(closeEvent -> bankList.refreshItem(bank));
            getUI().addWindow(bankEditorWindow);
        });

        deleteButton.addClickListener(clickEvent -> {
            Bank bank = selectedBank.get();
            Controller.deleteBank(bank.getId());
            bankList.getItems().remove(bank);
            bankList.refreshAll();
        });

        //Установка содержимого
        contentLayout.addComponent(grid, "top: 2%; left: 2%;");
        buttonsLayout.addComponent(addButton);
        buttonsLayout.addComponent(changeButton);
        buttonsLayout.addComponent(deleteButton);
        contentLayout.addComponent(buttonsLayout, "top: 86%; left: 2%;");

        center();
        setContent(contentLayout);
    }
}
