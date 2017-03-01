package com.vanbios.transactionviewer.products;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author Ihor Bilous
 */

public interface ProductsMVP {

    interface Model {

        Flowable<List<Product>> getProductListObservable();

        Flowable<String> getLoadRatesObservable();
    }

    interface View {

        void updateProductsList(List<ProductViewModel> list);

        void showErrorMessage(String message);
    }

    interface Presenter {

        void loadData();

        void setView(ProductsMVP.View view);
    }
}