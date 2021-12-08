package com.haulmont.testtask.view;

import com.haulmont.testtask.controller.Controller;
import com.haulmont.testtask.model.Client;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.concurrent.atomic.AtomicReference;

@Theme(ValoTheme.THEME_NAME)
public class ClientsWindow extends Window {

    private final ListDataProvider<Client> clientList;

    public ClientsWindow() {
        super("Клиенты");
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
        AtomicReference<Client> selectedClient = new AtomicReference<>();

        //Кнопки изменить/удалить заблокированы до выбора значения
        changeButton.setEnabled(false);
        deleteButton.setEnabled(false);

        //Установка значений в таблице
        clientList = DataProvider.ofCollection(Controller.getClientList());
        Grid<Client> grid = new Grid<>();
        grid.setDataProvider(clientList);

        //Установка колонок в таблице
        grid.setHeight("80%");
        grid.setWidth("98%");
        grid.addColumn(Client::getFullName).setCaption("ФИО");
        grid.addColumn(Client::getPhoneNumber).setCaption("Телефон");
        grid.addColumn(Client::getEmailAddress).setCaption("E-Mail");
        grid.addColumn(Client::getPassportNumber).setCaption("Паспорт");

        //Выбор элемента таблицы: если выбран какой-либо элемент
        //Есть возможность отредактировать или удалить его
        grid.asSingleSelect().addValueChangeListener(valueChangeEvent -> {
            selectedClient.set(valueChangeEvent.getValue());
            if (valueChangeEvent.getValue() != null) {
                changeButton.setEnabled(true);
                deleteButton.setEnabled(true);
                System.out.println("Выбранный клиент: " + valueChangeEvent.getValue().getId());
            } else {
                changeButton.setEnabled(false);
                deleteButton.setEnabled(false);
            }
        });

        //Логика кнопок добавления/изменения/удаления
        addButton.addClickListener(clickEvent -> {
            selectedClient.set(new Client(-1, "", "", "", ""));
            ClientEditorWindow clientEditorWindow = new ClientEditorWindow(selectedClient.get(), MainUI.OPTIONS.ADD);
            //Обновление после закрытия
            clientEditorWindow.addCloseListener(closeEvent -> {
                clientList.getItems().clear();
                clientList.getItems().addAll(Controller.getClientList());
                clientList.refreshAll();
            });
            getUI().addWindow(clientEditorWindow);
        });

        changeButton.addClickListener(clickEvent -> {
            Client client = selectedClient.get();
            ClientEditorWindow clientEditorWindow = new ClientEditorWindow(client, MainUI.OPTIONS.UPDATE);
            //Обновление после закрытия
            clientEditorWindow.addCloseListener(closeEvent -> clientList.refreshItem(client));
            getUI().addWindow(clientEditorWindow);
        });

        deleteButton.addClickListener(clickEvent -> {
            Client client = selectedClient.get();
            int errCode = Controller.deleteClient(client.getId());
            if (errCode != -1) {
                clientList.getItems().remove(client);
                clientList.refreshAll();
            } else {
                VerticalLayout errContent = new VerticalLayout();
                errContent.addComponent(new Label
                        ("Нельзя удалить клиента, для которого существует кредитное предложении или банк!"));
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