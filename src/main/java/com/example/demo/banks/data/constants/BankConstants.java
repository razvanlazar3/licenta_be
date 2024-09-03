package com.example.demo.banks.data.constants;

import java.time.Period;

public class BankConstants {
    public static final String BANKS_TABLE = "banks";
    public static final String BANK_OFFERS_TABLE = "bank_offers";
    public static final String BANK_DEPOSITS_TABLE = "bank_deposits";

    public static final String BT_BANK = "BT";
    public static final String BCR_BANK = "BCR";
    public static final String ING_BANK = "ING";
    public static final String BRD_BANK = "BRD";

    public static final String BT_BANK_ADDRESS = "Str.Constantin Brancusi, nr. 74 - 76, etaj 3, Cluj-Napoca";
    public static final String BCR_BANK_ADDRESS = "Șoseaua Orhideelor nr. 15D, Clădirea The Bridge 1, " +
            "etajul 2, Sector 6, București";
    public static final String ING_BANK_ADDRESS = "Expo Business Park, strada Aviator Popișteanu, nr. 54A, " +
            "Clădirea nr. 3, 012095, Bucureşti";
    public static final String BRD_BANK_ADDRESS = "BRD Tower. Bd. Ion Mihalache nr. 1-7, " +
            "sector 1. 011171 București";

    public static final Period PERIOD_6M = Period.ofMonths(6);
    public static final Period PERIOD_1Y = Period.ofYears(1);
    public static final Period PERIOD_3Y = Period.ofYears(3);
    public static final Period PERIOD_5Y = Period.ofYears(5);

    public static final Integer DISCOUNT_3 = 3;
    public static final Integer DISCOUNT_4 = 4;
    public static final Integer DISCOUNT_5 = 5;
    public static final Integer DISCOUNT_7 = 7;
    public static final Integer DISCOUNT_8 = 8;
    public static final Integer DISCOUNT_12 = 12;
    public static final Integer DISCOUNT_15 = 15;
}
