package com.example.shouye.accounting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shouye.R;

import java.util.List;

/**
 * Created by ThinkPad on 2017/12/8.
 */

public class AmountAdapter extends RecyclerView.Adapter<AmountAdapter.AmountViewHolder> {

    private List<Amount> mData;
    private Context mContext;

    public AmountAdapter(List<Amount> mData,Context mContext){
        this.mData = mData;
        this.mContext = mContext;
    }

    class AmountViewHolder extends RecyclerView.ViewHolder{
        ImageView aIcon;
        TextView aContent;
        TextView aAmount;
        public AmountViewHolder(final View itemView){
            super(itemView);
            aIcon = (ImageView)itemView.findViewById(R.id.img_icon);
            aContent = (TextView)itemView.findViewById(R.id.txt_aContent);
            aAmount = (TextView)itemView.findViewById(R.id.txt_aAmount);
        }
    }

    @Override
    public AmountAdapter.AmountViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View layout=LayoutInflater.from(mContext).inflate(R.layout.item_list_amount,parent,false);
        AmountViewHolder vh = new AmountViewHolder(layout);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AmountAdapter.AmountViewHolder personViewHolder, final int i){
        int typeIcon = R.drawable.laugh;
        if (mData.get(i).getType() < 0) {
            typeIcon = R.drawable.cry;
        }
        personViewHolder.aIcon.setImageResource(typeIcon);
        personViewHolder.aAmount.setText(String.valueOf(mData.get(i).getAmount()));
        personViewHolder.aContent.setText(mData.get(i).getContent());
    }

    @Override
    public int getItemCount(){
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public void removeData(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void addData(int position,Amount amount){
        mData.add(position,amount);
        notifyItemInserted(position);
    }

    public void changeData(int position){
        mData.set(position,new Amount());
        notifyItemChanged(position);
    }
}