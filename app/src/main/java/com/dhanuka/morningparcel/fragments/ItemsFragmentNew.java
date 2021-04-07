package com.dhanuka.morningparcel.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dhanuka.morningparcel.Helper.ItemMasterhelper;
import com.dhanuka.morningparcel.R;

import com.dhanuka.morningparcel.Helper.Preferencehelper;
import com.dhanuka.morningparcel.adapter.HomeStoreAdapter;
import com.dhanuka.morningparcel.adapter.ProductsAdapter;
import com.dhanuka.morningparcel.beans.StoreBean;
import com.dhanuka.morningparcel.customViews.CartCountView;
import com.dhanuka.morningparcel.database.DatabaseManager;
import com.dhanuka.morningparcel.events.SearchTextChangedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 *
 */
public class ItemsFragmentNew extends Fragment {
    private String type;
    private RecyclerView lvProducts;
    private ProgressBar pbLoading;
    private LinearLayout linearContinue;
    private EditText etSearch;
    private Context context;
    private ProductsAdapter adapter;
    DatabaseManager dbManager;
    CartCountView countView;

    public static ItemsFragmentNew newInstance() {
        Bundle args = new Bundle();
        ItemsFragmentNew fragment = new ItemsFragmentNew();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itesm, container, false);
        init(view);
        return view;
    }

    SharedPreferences prefs;

    //    ArrayList<MainCatBean> mListCatMain;
    ArrayList<StoreBean> mListCat;
    ArrayList<StoreBean> mListCatFilter;
    //   ArrayList<MainCatBean.CatBean> mListCategories;
    //   ArrayList<ItemMasterhelper> mListProductsAll = new ArrayList<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    private void init(View view) {
        mListCat = new ArrayList<>();
        mListCatFilter = new ArrayList<>();
        type = getArguments().getString("type");
        mListCat = (ArrayList<StoreBean>) getArguments().getSerializable("mListStoreData");
        etSearch = (EditText) view.findViewById(R.id.etSearch);
         lvProducts = (RecyclerView) view.findViewById(R.id.lvProducts);
         lvProducts.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        lvProducts.setAdapter(new HomeStoreAdapter(getActivity(), mListCat,type));
        mListCatFilter=mListCat;
        linearContinue = (LinearLayout) view.findViewById(R.id.linearContinue);
        pbLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
        context = lvProducts.getContext();
        countView = (CartCountView) LayoutInflater.from(context).inflate(R.layout.action_cart, null);
        dbManager = DatabaseManager.getInstance(context);
        if (mListCat.size() > 0) {
            lvProducts.setVisibility(View.VISIBLE);
            linearContinue.setVisibility(View.GONE);
            pbLoading.setVisibility(View.GONE);
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void filter(String text) {
        text = text.toLowerCase();
        Log.e("my_search987",mListCat.size()+" == "+text);
        mListCat.clear();
        mListCatFilter = (ArrayList<StoreBean>) getArguments().getSerializable("mListStoreData");  if (text.length() < 1) {
            mListCat.addAll(mListCatFilter);
        } else {
            for (StoreBean product : mListCatFilter) {
                Log.e("my_search11",product.getmBean().getStrName()+" == "+text);
                for (ItemMasterhelper mItem : product.getmListProducts()){
                Log.e("my_search",mItem.getItemName()+" == "+text);
                  if (mItem.getItemName().toLowerCase().contains(text) ) {
                      mListCat.add(product);
                  }
              }

            }
        }
        Log.e("mListCat",mListCat.size()+" = "+mListCatFilter.size());
        lvProducts.setAdapter(new HomeStoreAdapter(getActivity(), mListCat,type));
    }

    @Override
    public void onResume() {
        super.onResume();

        pbLoading.setVisibility(View.GONE);


    }

    @Override
    public void onPause() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchTextChanged(SearchTextChangedEvent event) {
        if (adapter != null) {
            adapter.filter(event.getText());
            lvProducts.invalidate();
        }
    }


    public interface onSomeEventListener {
        //  public void someEvent(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //   someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    // onSomeEventListener someEventListener;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cart, menu);
        MenuItem item = menu.findItem(R.id.cart);
        item.setActionView(countView);
        Preferencehelper prefs4=new Preferencehelper(getActivity());
        if (prefs4.getPREFS_trialuser().equalsIgnoreCase("0")) {
            item.setVisible(false);

        }
        else
        {
            item.setVisible(true);

        }
    }


}
