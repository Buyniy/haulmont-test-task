INSERT INTO Client (Client_ID, fullName, phoneNumber, emailAddress, passportNumber) VALUES (0, 'Tarasov Vadim Olegovich', '89272636870', 'vtarasow163@gmail.com', '123456');
INSERT INTO Client (Client_ID, fullName, phoneNumber, emailAddress, passportNumber) VALUES (1, 'Ivanov Ivan Ivanovich', '81234567890', 'ivanov@gmail.com', '123456');
INSERT INTO Client (Client_ID, fullName, phoneNumber, emailAddress, passportNumber) VALUES (2, 'Sidorov Sidor Sidorovich', '81234567890', 'sidorov@gmail.com', '123456');
INSERT INTO Client (Client_ID, fullName, phoneNumber, emailAddress, passportNumber) VALUES (3, 'Petrov Petr Petrovich', '81234567890', 'petrov@gmail.com', '123456');
INSERT INTO Client (Client_ID, fullName, phoneNumber, emailAddress, passportNumber) VALUES (4, 'Fedorov Fedor Fedorovich', '81234567890', 'fedorov@gmail.com', '123456');

INSERT INTO Credit (Credit_ID, creditLimit, percentRate) VALUES (0, 10000, 15);
INSERT INTO Credit (Credit_ID, creditLimit, percentRate) VALUES (1, 150000, 10);
INSERT INTO Credit (Credit_ID, creditLimit, percentRate) VALUES (2, 295000, 12);
INSERT INTO Credit (Credit_ID, creditLimit, percentRate) VALUES (3, 50000, 23);
INSERT INTO Credit (Credit_ID, creditLimit, percentRate) VALUES (4, 900000, 6);

INSERT INTO Bank (Bank_ID, bankName, Client, Credit) VALUES (0, 'Sberbank', 0, 0);
INSERT INTO Bank (Bank_ID, bankName, Client, Credit) VALUES (1, 'Sberbank', 1, 1);
INSERT INTO Bank (Bank_ID, bankName, Client, Credit) VALUES (2, 'Gazprombank', 2, 2);
INSERT INTO Bank (Bank_ID, bankName, Client, Credit) VALUES (3, 'VTB', 3, 3);
INSERT INTO Bank (Bank_ID, bankName, Client, Credit) VALUES (4, 'Alfabank', 4, 4);

INSERT INTO CreditOffer (CreditOffer_ID, Client, Credit, CreditAmount, CountPayment, DateCreditOffer, DatePayment, PaymentAmount, BodyAmount, PercentAmount) VALUES (0, 0, 0, 5750, 3, '2021-12-04', '2022-01-04', 1917, 1629 ,288);
INSERT INTO CreditOffer (CreditOffer_Id, Client, Credit, CreditAmount, CountPayment, DateCreditOffer, DatePayment, PaymentAmount, BodyAmount, PercentAmount) VALUES (1, 1, 1, 110000, 12, '2021-12-05', '2022-01-05', 9167, 8250, 917);
INSERT INTO CreditOffer (CreditOffer_Id, Client, Credit, CreditAmount, CountPayment, DateCreditOffer, DatePayment, PaymentAmount, BodyAmount, PercentAmount) VALUES (2, 2, 2, 324800, 24, '2021-12-06', '2022-01-06', 13533, 11909, 1624);
INSERT INTO CreditOffer (CreditOffer_Id, Client, Credit, CreditAmount, CountPayment, DateCreditOffer, DatePayment, PaymentAmount, BodyAmount, PercentAmount) VALUES (3, 3, 3, 30750, 6, '2021-12-07', '2022-01-07', 5125, 3946, 1179);
INSERT INTO CreditOffer (CreditOffer_Id, Client, Credit, CreditAmount, CountPayment, DateCreditOffer, DatePayment, PaymentAmount, BodyAmount, PercentAmount) VALUES (4, 4, 4, 530000, 16, '2021-12-08', '2022-01-08', 33125, 31138, 1987);