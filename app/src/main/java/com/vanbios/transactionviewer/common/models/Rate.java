package com.vanbios.transactionviewer.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ihor Bilous
 */

@Getter
@AllArgsConstructor
public class Rate {

    private String from;
    private String to;
    private String rate;
}