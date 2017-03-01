package com.vanbios.transactionviewer.products;

import com.vanbios.transactionviewer.common.repository.Repository;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author Ihor Bilous
 */

class ProductsModel implements ProductsMVP.Model {

    private Repository repository;

    ProductsModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Flowable<List<Product>> getProductListObservable() {
        return repository.getListProductObservable();
    }

    @Override
    public Flowable<String> getLoadRatesObservable() {
        return repository.getLoadRatesObservable();
    }
}