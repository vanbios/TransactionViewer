package com.vanbios.transactionviewer.util.rates.converter;

import com.vanbios.transactionviewer.util.rates.util.BigDecimalUtil;

import java.math.BigDecimal;


final class CrossRateCalculator {

    static FxRate calculateCross(final CurrencyPair targetPair, final FxRate fx1, final FxRate fx2, final int precision,
                                 final int precisionForInverseFxRate, final MajorCurrencyRanking ranking, final int bidRounding, final int askRounding) {
        final String crossCcy = fx1.getCurrencyPair().findCommonCcy(fx2.getCurrencyPair());

        if (targetPair.containsCcy(crossCcy))
            throw new IllegalArgumentException("The target currency pair " + targetPair + " contains the common ccy " + crossCcy);

        final String fx1Ccy1 = fx1.getCurrencyPair().getCcy1();
        final String fx2Ccy1 = fx2.getCurrencyPair().getCcy1();
        final String fx1Ccy2 = fx1.getCurrencyPair().getCcy2();
        final String fx2Ccy2 = fx2.getCurrencyPair().getCcy2();
        final boolean shouldDivide = fx1Ccy1.equals(crossCcy) && fx2Ccy1.equals(crossCcy) || fx1Ccy2.equals(crossCcy) && fx2Ccy2.equals(crossCcy);

        BigDecimal rate;

        if (shouldDivide) {
            final FxRate numeratorFx = targetPair.getCcy1().equals(fx2Ccy2) || targetPair.getCcy1().equals(fx1Ccy1) ? fx1 : fx2;
            final FxRate denominatorFx = numeratorFx == fx1 ? fx2 : fx1;

            rate = BigDecimalUtil.divide(precision, numeratorFx.getRate(), denominatorFx.getRate(), bidRounding);
        } else {
            final boolean inverse = targetPair.getCcy1().equals(fx2Ccy2) || targetPair.getCcy1().equals(fx1Ccy2);
            if (inverse) {
                rate = BigDecimalUtil.setScale(
                        BigDecimalUtil.inverse(BigDecimalUtil.multiply(fx1.getRate(), fx2.getRate()), precisionForInverseFxRate, askRounding),
                        precision);
            } else
                rate = BigDecimalUtil.setScale(BigDecimalUtil.multiply(fx1.getRate(), fx2.getRate()), precision);
        }
        return new FxRateImpl(targetPair, crossCcy, ranking.isMarketConvention(targetPair), rate);
    }
}
