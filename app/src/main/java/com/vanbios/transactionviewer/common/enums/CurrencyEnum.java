package com.vanbios.transactionviewer.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ihor Bilous
 */

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum CurrencyEnum {

    GBP("£", 1),
    USD("$", 0.77),
    AUD("A$", 1.2),
    CAD("CA$", 0.73),
    EUR("€", 0.89);

    private final String title;
    @Setter
    private double rate;


    public static void updateRate(String currency, Double rate) {
        switch (valueOf(currency)) {
            case USD:
                USD.setRate(rate);
                break;
            case EUR:
                EUR.setRate(rate);
                break;
            case AUD:
                AUD.setRate(rate);
                break;
            case CAD:
                CAD.setRate(rate);
                break;
        }
    }

    public static double getRateByCurrency(String currency) {
        switch (valueOf(currency)) {
            case USD:
                return USD.getRate();
            case EUR:
                return EUR.getRate();
            case AUD:
                return AUD.getRate();
            case CAD:
                return CAD.getRate();
        }
        return 1;
    }

    public static String getTitleByCurrency(String currency) {
        switch (valueOf(currency)) {
            case USD:
                return USD.getTitle();
            case EUR:
                return EUR.getTitle();
            case AUD:
                return AUD.getTitle();
            case CAD:
                return CAD.getTitle();
        }
        return GBP.getTitle();
    }
}
