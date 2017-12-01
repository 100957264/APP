package com.android.fisewatchlauncher.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.fisewatchlauncher.R;
import com.android.fisewatchlauncher.client.msg.MsgType;
import com.android.fisewatchlauncher.listener.OnTagItemClickListener;
import com.android.fisewatchlauncher.net.subscriber.TaskSubscriber;
import com.android.fisewatchlauncher.task.CommonTask;
import com.android.fisewatchlauncher.task.TaskBean;
import com.android.fisewatchlauncher.task.TaskManager;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by fanyang on 2017/8/7.
 */
public class TagService implements TaskBean.TaskCallback, TagFlowLayout.OnTagClickListener, TagFlowLayout.OnSelectListener {
    Context context;
    TagFlowLayout mFlowLayout;//标签云对象
    TextView result;
    List<String> list = new ArrayList<>();//标签云数据的集合
    TagAdapter tagAdapter;
    OnTagItemClickListener listener;

    public TagService(final Context ctx, final View root, final OnTagItemClickListener listener) {
        TagService.this.context = ctx;
        TaskManager.instance().exeTask(new CommonTask() {
            @Override
            public void exeTask() {
                super.exeTask();
                //给集合添加数据
                HashMap<String, String> tags = MsgType.instance().getmHeaderContent();
                TagService.this.listener = listener;
                for (Map.Entry<String, String> entry : tags.entrySet()) {
                    System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
                    list.add(entry.getValue());
                }
                mFlowLayout = (TagFlowLayout) root.findViewById(R.id.tag_view);
                result = (TextView) root.findViewById(R.id.tag_result);

                tagAdapter = new TagAdapter<String>(list) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        View root = View.inflate(context, R.layout.tag_item, null);
                        TextView tv = (TextView) root.findViewById(R.id.tag_text);
                        ImageView iv = (ImageView) root.findViewById(R.id.tag_remove);
                        if (null != list && list.size() > 0) {
                            tv.setText(s);
                            iv.setVisibility(View.GONE);
                        }
                        return root;
                    }
                };
                mFlowLayout.setOnTagClickListener(TagService.this);
                mFlowLayout.setOnSelectListener(TagService.this);
            }
        }, new TaskSubscriber(context, this));
    }

    @Override
    public void doTaskBack() {
        mFlowLayout.setAdapter(tagAdapter);
    }

    @Override
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        String tag = list.get(position);
        result.setText("当前选中的是：" + tag);
        listener.onItemClick(tag, position);
//        LogUtils.e("当前点击的是：" + tag);
        return true;
    }

    @Override
    public void onSelected(Set<Integer> selectPosSet) {
//        String tag = list.get(position);
//        result.setText("当前选中的是：" + tag);
//        listener.onItemClick(tag, position);
//        LogUtils.e("当前选中的是：" + tag);
    }
}
