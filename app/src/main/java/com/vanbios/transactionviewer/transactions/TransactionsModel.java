package com.vanbios.transactionviewer.transactions;

import com.vanbios.transactionviewer.common.repository.Repository;
import com.vanbios.transactionviewer.products.Product;

import java.util.List;

/**
 * @author Ihor Bilous
 */

public class TransactionsModel implements TransactionsMVP.Model {

    private Repository repository;

    public TransactionsModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<Transaction> getTransactionsList(String productName) {
        Product product = repository.getProductByName(productName);
        return product != null ? product.getTransactionsList() : null;
    }
}