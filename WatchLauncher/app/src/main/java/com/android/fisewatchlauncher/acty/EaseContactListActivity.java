package com.android.fisewatchlauncher.acty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.entity.dao.EaseFriend;
import com.android.fisewatchlauncher.event.EaseFriendUpdate;
import com.android.fisewatchlauncher.parser.MsgParser;
import com.android.fisewatchlauncher.prenster.dao.EaseFriendDaoUtils;
import com.android.fisewatchlauncher.utils.ActivityUtils;
import com.android.fisewatchlauncher.utils.DpUtil;
import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * project : WatchLauncher
 * describe : 环信联系人列表
 *
 * @author : ChenJP
 * @date : 2017/11/14  15：51
 */
public class EaseContactListActivity extends AppCompatActivity implements RecyclerArrayAdapter.OnItemClickListener {
    private EasyRecyclerView                 recyclerView;
    private RecyclerArrayAdapter<EaseFriend> adapter;
    private List<EaseFriend>                 datas;
    public static final String EXTRA_KEY_EASE_FRIEND = "extra_key_ease_friend";

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, EaseFunctionActivity.class);
        intent.putExtra(EXTRA_KEY_EASE_FRIEND,datas.get(position));
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fise_phone_contactor);
        findViewById(R.id.voice_intercom).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.contact_title)).setText("微聊好友");
        recyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        EventBus.getDefault().register(this);
        DividerDecoration itemDecoration = new DividerDecoration(Color.GRAY, DpUtil.dip2px(this, 1.5f),
                DpUtil.dip2px(this, 5), DpUtil.dip2px(this, 5));
        itemDecoration.setDrawLastItem(false);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapterWithProgress(adapter = new EaseContactListAdapter(this));
        datas = EaseFriendDaoUtils.instance().selectAll();
        adapter.addAll(datas);
        MsgParser.instance().sendMsgByType(MsgType.MEMBERS);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    class EaseContactListAdapter extends RecyclerArrayAdapter<EaseFriend> {

        public EaseContactListAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
            return new EaseFriendHolder(parent);
        }
    }

    public void onBack(View v) {
        this.finish();
    }

    public void onVoiceIntercomClick(View v) {
        ActivityUtils.startActivity(FiseChatMainActivity.class);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateContactList(EaseFriendUpdate event) {
        datas = EaseFriendDaoUtils.instance().selectAll();
        adapter.clear();
        adapter.addAll(datas);
//        recyclerView.notify();
    }

    public class EaseFriendHolder extends BaseViewHolder<EaseFriend> {
        private TextView  mTv_name;
        private ImageView mImg_face;
        private TextView  mTv_sign;


        public EaseFriendHolder(ViewGroup parent) {
            super(parent, R.layout.item_phone_contactor);
            mTv_name = $(R.id.contactor_name);
            mTv_sign = $(R.id.contactor_sign);
            mImg_face = $(R.id.contactor_face);
        }

        @Override
        public void setData(final EaseFriend person) {
            String name = person.getNick();
            String toDisplayName = TextUtils.isEmpty(name) ? person.getFriendId() : name;
            mTv_name.setText(toDisplayName);
            mTv_name.setTextColor(Color.WHITE);
            String phNum = person.getPhone();
            mTv_sign.setVisibility(TextUtils.isEmpty(phNum) ? View.GONE : View.VISIBLE);
            mTv_sign.setText(phNum);
            mImg_face.setImageDrawable(getDrawable(R.drawable.img_baba));
        }
    }

}
