CREATE TABLE IF NOT EXISTS Client (
    Client_ID bigint IDENTITY PRIMARY KEY NOT NULL,
    FullName varchar(64) not null,
    PhoneNumber varchar(16) not null,
    EmailAddress varchar(64) not null,
    PassportNumber varchar(6) not null
);

CREATE TABLE IF NOT EXISTS Credit (
    Credit_ID bigint IDENTITY PRIMARY KEY NOT NULL,
    CreditLimit double not null,
    PercentRate float not null
);

CREATE TABLE IF NOT EXISTS Bank (
    Bank_ID bigint IDENTITY PRIMARY KEY NOT NULL,
    BankName varchar(64) not null,
    Client bigint REFERENCES Client (Client_ID),
    Credit bigint REFERENCES Credit (Credit_ID)
);

CREATE TABLE IF NOT EXISTS CreditOffer (
    CreditOffer_ID bigint IDENTITY PRIMARY KEY NOT NULL,
    Client bigint REFERENCES Client (Client_ID),
    Credit bigint REFERENCES Credit (Credit_ID),
    CreditAmount bigint not null,
    CountPayment int not null,
    DateCreditOffer date not null,
    DatePayment date not null,
    PaymentAmount double not null,
    BodyAmount double not null,
    PercentAmount double not null
);