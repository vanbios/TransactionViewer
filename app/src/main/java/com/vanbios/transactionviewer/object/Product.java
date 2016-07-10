package com.vanbios.transactionviewer.object;

import android.support.annotation.NonNull;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Ihor Bilous
 */

@Getter
@AllArgsConstructor
public class Product implements Comparable {

    private String name;
    private List<Transaction> transactionsList;

    @Override
    public int compareTo(@NonNull Object o) {
        return name.compareTo(((Product) o).getName());
    }
}
