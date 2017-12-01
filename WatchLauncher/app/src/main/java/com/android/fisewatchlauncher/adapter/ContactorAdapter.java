package com.android.fisewatchlauncher.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.android.fisewatchlauncher.entity.dao.PhoneContractor;
import com.android.fisewatchlauncher.holder.ContractorViewHolder;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 17:42
 */
public class ContactorAdapter extends RecyclerArrayAdapter<PhoneContractor> {

    public ContactorAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContractorViewHolder(parent);
    }
}
