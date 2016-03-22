package com.yitingche.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.yitingche.demo.R;
import com.yitingche.demo.adapter.SearchAdapter;
import com.yitingche.demo.view.ClearEditText;
import com.yitingche.demo.view.SearchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lvxia on 16/3/5.
 */
public class SearchActivity extends Activity implements AdapterView.OnItemClickListener{
    ListView mListView;
    SearchAdapter mAdapter;
    ClearEditText mEditText;
    SuggestionSearch mSuggestionSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
    }

    private void initView(){
        mSuggestionSearch = SuggestionSearch.newInstance();
        mEditText = (ClearEditText) findViewById(R.id.search_edit_text);
        mEditText.setTextChangeListener(new ClearEditText.TextChangeListener() {
            @Override
            public void onTextChanged(Editable s) {
                SuggestionSearchOption option = new SuggestionSearchOption();
                option.keyword(s.toString());
                option.city("北京");
                mSuggestionSearch.requestSuggestion(option);
            }
        });
        mListView = (ListView)findViewById(R.id.asso_list);

        mAdapter = new SearchAdapter(this, null);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
            @Override
            public void onGetSuggestionResult(SuggestionResult suggestionResult) {
                if (suggestionResult != null && suggestionResult.getAllSuggestions() != null) {
                    List<SearchItem> list = new ArrayList<SearchItem>();
                    for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
                        SearchItem item = new SearchItem();
                        item.searchname = info.key;
                        item.type = 2;
                        item.latLng = info.pt;
                        list.add(item);
                    }
                    mAdapter.setDataList(list);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        SearchItem item = mAdapter.getItem(position);
        if(item.latLng != null) {
            intent.putExtra("lng", item.latLng.longitude);
            intent.putExtra("lat", item.latLng.latitude);
        }
        setResult(0, intent);
        finish();
    }
}
