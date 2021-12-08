package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Client;
import com.haulmont.testtask.model.Credit;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

@Theme(ValoTheme.THEME_NAME)
public class CreditsWindow extends Window {

    private final Grid<Credit> grid = new Grid<>();
    private ListDataProvider<Credit> creditList;

    public CreditsWindow() {
        super("Кредиты");
        setHeight("350px");
        setWidth("700px");

        //Содержимое окна: кнопки и таблица
        AbsoluteLayout contentLayout = new AbsoluteLayout();
        contentLayout.setSizeFull();
        HorizontalLayout buttonsLayout = new HorizontalLayout();

        Button addButton = new Button("Добавить");
        Button changeButton = new Button("Изменить");
        Button deleteButton = new Button("Удалить");

        //Текущий выбранный объект
        AtomicReference<Credit> selectedCredit = new AtomicReference<>();

        //Кнопки изменить/удалить заблокированы до выбора значения
        changeButton.setEnabled(false);
        deleteButton.setEnabled(false);

        //Установка значений в таблице
        creditList = DataProvider.ofCollection(Controller.getCreditList());
        grid.setDataProvider(creditList);

        //NumberFormat.getIntegerInstance(Locale.US)
        //Установка колонок в таблице
        grid.setHeight("80%");
        grid.setWidth("98%");
        grid.addColumn(Credit::getCreditLimit).setCaption("Лимит по кредиту");
        grid.addColumn(Credit::getPercentRate).setCaption("Процентная ставка");

        //Выбор элемента таблицы: если выбран какой-либо элемент
        //Есть возможность отредактировать или удалить его
        grid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            selectedCredit.set(valueChangeEvent.getValue());
            if (valueChangeEvent.getValue() != null) {
                changeButton.setEnabled(true);
                deleteButton.setEnabled(true);
                System.out.println("Выбранный кредит: " + valueChangeEvent.getValue().getId());
            } else {
                changeButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        //Логика кнопок добавления/изменения/удаления
        addButton.addClickListener(clickEvent -> {
            selectedCredit.set(new Credit(-1, 0, 0));
            CreditEditorWindow creditEditorWindow = new CreditEditorWindow(selectedCredit.get(), MainUI.OPTIONS.ADD);
            //Обновление после закрытия
            creditEditorWindow.addCloseListener(closeEvent -> {
                creditList.getItems().clear();
                creditList.getItems().addAll(Controller.getCreditList());
                creditList.refreshAll();
            });
            getUI().addWindow(creditEditorWindow);
        });

        changeButton.addClickListener(clickEvent -> {
            Credit credit = selectedCredit.get();
            CreditEditorWindow creditEditorWindow = new CreditEditorWindow(credit, MainUI.OPTIONS.UPDATE);
            //Обновление после закрытия
            creditEditorWindow.addCloseListener(closeEvent -> creditList.refreshItem(credit));
            getUI().addWindow(creditEditorWindow);
        });

        deleteButton.addClickListener(clickEvent -> {
            Credit credit = selectedCredit.get();
            int errCode = Controller.deleteCredit(credit.getId());
            if (errCode != -1) {
                creditList.getItems().remove(credit);
                creditList.refreshAll();
            } else {
                VerticalLayout errContent = new VerticalLayout();
                errContent.addComponent(new Label
                        ("Нельзя удалить кредит, для которого существует кредитное предложении или банк!"));
                Window err = new Window("Ошибка");
                err.setContent(errContent);
                err.setModal(true);
                err.center();
                getUI().addWindow(err);
            }
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