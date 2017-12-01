package com.android.fisewatchlauncher.acty;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.adapter.CommonItemAdapter;
import com.android.fisewatchlauncher.entity.settings.CommonItem;
import com.android.fisewatchlauncher.utils.DpUtil;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 16:59
 */
public class SettingsActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener {
    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<CommonItem> adapter;

    String[] itemNames = new String[]{
            "蓝牙设置", "wifi设置", "音量设置", "亮度设置", "关于APP", "系统设置"
    };

    int[] itemIds = new int[]{
            R.drawable.ic_light, R.drawable.ic_light, R.drawable.ic_light, R.drawable.ic_light, R.drawable.ic_light,
            R.drawable.ic_light
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fise_phone_contactor);
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, DpUtil.dip2px(this, 0.5f), DpUtil.dip2px(this, 72), 0);
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new CommonItemAdapter(this);
        recyclerView.setAdapterWithProgress(adapter);
        List<CommonItem> items = initDatas();
        adapter.addAll(items);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(this);
    }

    private List<CommonItem> initDatas() {
        CommonItem item;
        List<CommonItem> items = new ArrayList<>();
        int len = itemNames.length;
        for (int i = 0; i < len; i++) {
            item = new CommonItem(itemNames[i], itemIds[i]);
            items.add(item);
        }
        return items;
    }

    @Override
    public void onItemClick(int position) {
        if (position == itemNames.length - 1) {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
