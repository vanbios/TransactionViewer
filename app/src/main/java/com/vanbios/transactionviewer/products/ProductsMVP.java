package com.vanbios.transactionviewer.products;

import java.util.List;

import rx.Observable;

/**
 * @author Ihor Bilous
 */

public interface ProductsMVP {

    interface Model {

        Observable<List<Product>> getProductListObservable();

        Observable<String> getLoadRatesObservable();
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
