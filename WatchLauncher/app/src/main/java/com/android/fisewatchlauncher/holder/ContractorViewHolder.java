package com.android.fisewatchlauncher.holder;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.entity.dao.PhoneContractor;
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
public class ContractorViewHolder extends BaseViewHolder<PhoneContractor> {
    private TextView mTv_name;
    private ImageView mImg_face;
    private TextView mTv_sign;


    public ContractorViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_phone_contactor);
        mTv_name = $(R.id.contactor_name);
        mTv_sign = $(R.id.contactor_sign);
        mImg_face = $(R.id.contactor_face);
    }

    @Override
    public void setData(final PhoneContractor person) {
        String name = person.getName();
        String toDisplayName = TextUtils.isEmpty(name) ? person.getNum() : name;
        mTv_name.setText(toDisplayName);
        mTv_name.setTextColor(Color.WHITE);
        String phNum = person.getNum();
        mTv_sign.setVisibility(TextUtils.isEmpty(phNum) ? View.GONE : View.VISIBLE);
        mTv_sign.setText(phNum);
        Glide.with(getContext())
                .load(person.getAvatar())
                .placeholder(R.drawable.img_baba)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(mImg_face);
    }
}
