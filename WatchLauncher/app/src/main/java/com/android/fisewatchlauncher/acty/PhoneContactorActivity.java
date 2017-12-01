package com.android.fisewatchlauncher.acty;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.fisewatchlauncher.KApplication;
import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.adapter.ContactorAdapter;
import com.android.fisewatchlauncher.entity.dao.PhoneContractor;
import com.android.fisewatchlauncher.prenster.dao.PhoneContactorDaoUtils;
import com.android.fisewatchlauncher.utils.DpUtil;
import com.android.fisewatchlauncher.utils.LogUtils;
import com.android.fisewatchlauncher.utils.ToastUtils;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.List;

import rx.functions.Action1;

/**
 * @author mare
 * @Description:TODO 电话联系人界面
 * @csdnblog http://blog.csdn.net/mare_blue
 * @date 2017/9/5
 * @time 16:59
 */
public class PhoneContactorActivity extends BaseActivity implements RecyclerArrayAdapter.OnItemClickListener {
    private EasyRecyclerView recyclerView;
    private RecyclerArrayAdapter<PhoneContractor> adapter;
    private List<PhoneContractor> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fise_phone_contactor);
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, DpUtil.dip2px(this, 1.5f), DpUtil.dip2px(this, 5), DpUtil.dip2px(this, 5));
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapterWithProgress(adapter = new ContactorAdapter(this));
        datas = PhoneContactorDaoUtils.instance().selectAll();
        adapter.addAll(datas);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(int position) {
        TelephonyManager tm = (TelephonyManager) KApplication.sContext.getSystemService(Context.TELEPHONY_SERVICE);
        if (TelephonyManager.CALL_STATE_IDLE !=tm.getCallState()) {
            ToastUtils.showShort("正在通话或通话未结束，请稍候再拨...");
            return;
        }
        String num = datas.get(position).getNum();
        if (TextUtils.isEmpty(num)) return;
        LogUtils.e("PhoneContactorActivity:phone_number = " + num);
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
        dialIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialIntent);
    }

    private static final String[] PERMISSION = new String[]{
            Manifest.permission.CALL_PHONE
    };

    public void onBack(View v) {
        this.finish();
    }
}
