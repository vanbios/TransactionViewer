package com.vanbios.transactionviewer.products;

import android.content.Context;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.vanbios.transactionviewer.R;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Ihor Bilous
 */

public class ProductsPresenter implements ProductsMVP.Presenter {

    private ProductsMVP.View view;
    private ProductsMVP.Model model;
    private Context context;


    public ProductsPresenter(ProductsMVP.Model model, Context context) {
        this.model = model;
        this.context = context;
    }

    @Override
    public void loadData() {
        model.getProductListObservable()
                .map(this::productListToViewModelList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Subscriber<List<ProductViewModel>>() {
                            @Override
                            public void onNext(List<ProductViewModel> items) {
                                if (view != null) {
                                    view.updateProductsList(items);
                                }
                                loadRates();
                            }

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                handleError(e);
                            }
                        }
                );
    }

    @Override
    public void setView(ProductsMVP.View view) {
        this.view = view;
    }

    private void loadRates() {
        model.getLoadRatesObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onNext(String s) {

                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleError(e);
                    }
                });
    }

    private void handleError(Throwable e) {
        if (view != null) {
            view.showErrorMessage(e.getMessage());
        }
    }

    private List<ProductViewModel> productListToViewModelList(List<Product> productList) {
        return Stream.of(productList).map(this::convertProductToViewModel).collect(Collectors.toList());
    }

    private ProductViewModel convertProductToViewModel(Product product) {
        String title = product.getName();
        String subTitle = String.format(
                context.getString(R.string.transactions_count_placeholder),
                product.getTransactionsList().size());
        return new ProductViewModel(product, title, subTitle);
    }
}