package com.android.fisewatchlauncher.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.android.fisewatchlauncher.entity.settings.CommonItem;
import com.android.fisewatchlauncher.holder.CommonSettingsItemHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/18
 * @time 9:29
 */
public class CommonItemAdapter extends RecyclerArrayAdapter<CommonItem> {

    public CommonItemAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommonSettingsItemHolder(parent);
    }
}
