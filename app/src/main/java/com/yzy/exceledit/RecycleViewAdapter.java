package com.yzy.exceledit;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bigkoo.alertview.AlertView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by 1015156849 on 2017/4/18.
 */

public class RecycleViewAdapter extends BaseQuickAdapter<String> {

    public RecycleViewAdapter(List<String> data) {
        super(R.layout.list_view_layout, data);

    }

    @Override
    protected void convert(final BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.editText, s);

        ShowAndGone(baseViewHolder);
        setOnClick(baseViewHolder);

    }

    private void ShowAndGone(BaseViewHolder baseViewHolder) {
        if (baseViewHolder.getAdapterPosition() == getData().size() - 1 && getData().size() > 1) {
            baseViewHolder.setVisible(R.id.itemAdd, true);
            baseViewHolder.setVisible(R.id.itemRemove, true);
        } else if (baseViewHolder.getAdapterPosition() != getData().size() && getData().size() > 1) {
            baseViewHolder.setVisible(R.id.itemAdd, false);
            baseViewHolder.setVisible(R.id.itemRemove, true);
        } else if (getData().size() == 1) {
            baseViewHolder.setVisible(R.id.itemAdd, true);
            baseViewHolder.setVisible(R.id.itemRemove, false);
        }
    }

    private void setOnClick(final BaseViewHolder baseViewHolder) {
        baseViewHolder.getView(R.id.itemAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = ((EditText) baseViewHolder.getView(R.id.editText)).getText().toString();
                if (str.isEmpty()||str.length() == 0) {

                    new AlertView("提示", "请输入内容", "我知道了", null, null, baseViewHolder.getConvertView().getContext(), AlertView.Style.Alert, null).show();

                    return;

                }if(str.trim().length()==0){
                    new AlertView("提示", "请输入有用的内容", "我知道了", null, null, baseViewHolder.getConvertView().getContext(), AlertView.Style.Alert, null).show();

                    return;
                }

                getData().set(baseViewHolder.getAdapterPosition(), str.trim());
                add(getData().size(), "");
                notifyDataSetChanged();
            }
        });

        baseViewHolder.getView(R.id.itemRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove(baseViewHolder.getAdapterPosition());
                notifyItemRemoved(baseViewHolder.getLayoutPosition());
                notifyDataSetChanged();
            }
        });

        ((EditText) (baseViewHolder.getView(R.id.editText))).addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getData().set(baseViewHolder.getAdapterPosition(),s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }




}
