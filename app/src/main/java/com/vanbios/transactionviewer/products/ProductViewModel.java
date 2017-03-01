package com.vanbios.transactionviewer.products;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ihor Bilous
 */

@AllArgsConstructor
@Getter
class ProductViewModel {

    private Product product;
    private String title;
    private String subTitle;
}