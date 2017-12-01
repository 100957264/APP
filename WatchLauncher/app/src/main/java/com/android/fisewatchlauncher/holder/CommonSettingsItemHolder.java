package com.android.fisewatchlauncher.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.entity.settings.CommonItem;
import com.android.fisewatchlauncher.glide.config.CropCircleTransformation;
import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * @author mare
 * @Description:TODO
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 17:44
 */
public class CommonSettingsItemHolder extends BaseViewHolder<CommonItem> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_sign;

    public CommonSettingsItemHolder(ViewGroup parent) {
        super(parent, R.layout.item_phone_contactor);
        mTv_name = $(R.id.contactor_name);
        mTv_sign = $(R.id.contactor_sign);
        mImg_face = $(R.id.contactor_face);
    }

    @Override
    public void setData(final CommonItem person) {
        mTv_name.setText(person.itemName);
        mTv_sign.setVisibility(View.GONE);
        Glide.with(getContext())
                .load(person.iconId)
                .placeholder(R.drawable.default_male_avatar)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mImg_face);
    }
}
