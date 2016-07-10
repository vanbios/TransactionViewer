package com.vanbios.transactionviewer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vanbios.transactionviewer.enums.FrgEnum;
import com.vanbios.transactionviewer.fragment.FrgProducts;
import com.vanbios.transactionviewer.fragment.FrgTransactions;
import com.vanbios.transactionviewer.util.ToastUtil;

import butterknife.BindView;

import static butterknife.ButterKnife.bind;

/**
 * @author Ihor Bilous
 */
public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    private FragmentManager fragmentManager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static long backPressExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initToolbar(getString(R.string.products_header), FrgEnum.PRODUCTS);

        fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        addFragment(new FrgProducts(), FrgEnum.PRODUCTS.name());
    }

    public void addFragment(Fragment f, String tag) {
        treatFragment(f, tag, true);
    }

    public Fragment getTopFragment() {
        return fragmentManager.findFragmentById(R.id.fragment_container);
    }

    private void treatFragment(Fragment f, String tag, boolean addToBackStack) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, f, tag);
        if (addToBackStack) ft.addToBackStack(tag);
        ft.commit();
    }

    public void popFragment() {
        fragmentManager.popBackStack();
    }


    private void initToolbar(String title, FrgEnum mode) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
            switch (mode) {
                case PRODUCTS:
                    toolbar.setNavigationIcon(R.drawable.ic_home_white_24dp);
                    break;
                case TRANSACTIONS:
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
                    break;
            }
        }
    }

    @Override
    public void onBackStackChanged() {
        String tag = getTopFragment().getTag();
        if (tag != null) {
            switch (FrgEnum.valueOf(tag)) {
                case PRODUCTS:
                    initToolbar(getString(R.string.products_header), FrgEnum.PRODUCTS);
                    break;
                case TRANSACTIONS:
                    FrgTransactions frg = (FrgTransactions) getTopFragment();
                    String productName = frg.getArguments().getString(FrgTransactions.PRODUCT);
                    initToolbar(String.format(
                            getString(R.string.transactions_header_placeholder),
                            productName),
                            FrgEnum.TRANSACTIONS);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        String tag = getTopFragment().getTag();
        if (tag != null) {
            switch (FrgEnum.valueOf(tag)) {
                case TRANSACTIONS:
                    popFragment();
                    break;
                default:
                    if (backPressExitTime + 2000 > System.currentTimeMillis()) {
                        this.finish();
                    } else {
                        ToastUtil.showClosableToast(this, getString(R.string.press_again_to_exit), ToastUtil.SHORT);
                        backPressExitTime = System.currentTimeMillis();
                    }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String tag = getTopFragment().getTag();
                if (tag != null) {
                    switch (FrgEnum.valueOf(tag)) {
                        case TRANSACTIONS:
                            popFragment();
                            break;
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
