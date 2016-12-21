package com.vanbios.transactionviewer.transactions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Ihor Bilous
 */

@Getter
@AllArgsConstructor
public class Transaction {

    private String currency;
    private double amount;
    @Setter
    private double gbpAmount;
}
