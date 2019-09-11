package com.example.imuhelper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.imuhelper.R;
import com.example.imuhelper.bean.TermBean;
import com.example.imuhelper.db.TermDBHelper;
import com.example.imuhelper.utils.TermTool;

import java.util.List;

public class TermListRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TermBean> list;
    private int selectIndex = 0;
    private Context context;

    private OnItemLongClickListener contentLongClick;
    private OnItemClickListener onContentClick;
    private OnItemClickListener onButtonClick;

    public TermListRvAdapter(List<TermBean> list, Context context) {
        this.list = list;
        this.context = context;

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == TermTool.getSelectedId()) {
                selectIndex = i;
                break;
            }
        }
    }

    public void setContentLongClick(OnItemLongClickListener contentLongClick) {
        this.contentLongClick = contentLongClick;
    }

    public void setOnContentClick(OnItemClickListener onContentClick) {
        this.onContentClick = onContentClick;
    }

    public void setOnButtonClick(OnItemClickListener onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    private class TermHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView yearTv;
        TextView monthTv;
        TextView dayTv;
        Button deleteBtn;
        ImageView Iv;
        LinearLayout baseLayout;

        public TermHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.termCard_name_Tv);
            yearTv = itemView.findViewById(R.id.termCard_year_Tv);
            monthTv = itemView.findViewById(R.id.termCard_month_Tv);
            dayTv = itemView.findViewById(R.id.termCard_day_Tv);
            deleteBtn = itemView.findViewById(R.id.termCard_delete_Btn);
            Iv=itemView.findViewById(R.id.termCard_Iv);
            baseLayout=itemView.findViewById(R.id.termCard_base_layout);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term_card, parent,false);
        return new TermHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (position == selectIndex) {
            ((TermHolder) holder).Iv.setImageResource(R.drawable.selected);
        } else {
            ((TermHolder) holder).Iv.setImageResource(R.drawable.notselected);
        }

        TermBean termBean = list.get(position);
        ((TermHolder) holder).nameTv.setText(termBean.getName());
        ((TermHolder) holder).monthTv.setText(String.valueOf(termBean.getMonth()));
        ((TermHolder) holder).dayTv.setText(String.valueOf(termBean.getDay()));
        ((TermHolder) holder).yearTv.setText(String.valueOf(termBean.getYear()));

        ((TermHolder) holder).deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onButtonClick != null)
                    onButtonClick.onClick(position);

                TermDBHelper termDBHelper = new TermDBHelper(context, null);
                termDBHelper.remove(list.get(position).getId());
                list.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();

                //如果删除的是当前显示的课程表
                if (position == selectIndex) {
                    //如果还有其他的学期，设置显示第一个学期
                    if (list.size() > 0) {
                        selectIndex = 0;
                        notifyItemChanged(0);
                        TermTool.setSelectedTerm(context, list.get(0).getId());
                    } /*没有学期，设置成没有学期*/ else {
                        TermTool.setNoSelect(context);
                        selectIndex = -1;
                    }
                }



            }
        });
        ((TermHolder) holder).baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(position);
                if (onContentClick != null)
                    onContentClick.onClick(position);
            }
        });
        ((TermHolder) holder).baseLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (contentLongClick != null)
                    contentLongClick.onLongClick(position);

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setSelect(int position) {
        TermTool.setSelectedTerm(context, list.get(position).getId());
        int lastPosition = selectIndex;
        selectIndex = position;
        notifyItemChanged(selectIndex);
        notifyItemChanged(lastPosition);
    }

    public void addNewTerm(TermBean termBean) {
        list.add(termBean);
        setSelect(list.size() - 1);
    }


    public interface OnItemLongClickListener {
        void onLongClick(int position);
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
