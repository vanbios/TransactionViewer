package com.vanbios.transactionviewer.products;

import com.vanbios.transactionviewer.common.repository.Repository;

import java.util.List;

import rx.Observable;

/**
 * @author Ihor Bilous
 */

public class ProductsModel implements ProductsMVP.Model {

    private Repository repository;

    public ProductsModel(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<List<Product>> getProductListObservable() {
        return repository.getListProductObservable();
    }

    @Override
    public Observable<String> getLoadRatesObservable() {
        return repository.getLoadRatesObservable();
    }
}
