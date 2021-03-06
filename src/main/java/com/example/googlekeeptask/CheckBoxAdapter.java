package com.example.googlekeeptask;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CheckBoxAdapter extends RecyclerView.Adapter<CheckBoxAdapter.CheckBoxHolder>{

    private Context context;
    private ArrayList<RemainderItems> notes;
    private NotesClickListener listener;

    public CheckBoxAdapter(Context context,ArrayList<RemainderItems> notes){
        this.context = context;
        this.notes = notes;
    }
    public void setListener(NotesClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public CheckBoxHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cell_for_notes,parent,false);
        CheckBoxHolder holder = new CheckBoxHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckBoxHolder holder, int position) {
        final RemainderItems currentNotes = notes.get(position);
        holder.mTvTitle.setText(currentNotes.title);
        ArrayList<Items> viewItems = RemainderItems.convertItemsStringToArrayList(currentNotes.items);

        holder.mLlItemHolder.removeAllViews();
        for (final Items items : viewItems){
            View view = LayoutInflater.from(context).inflate(R.layout.cell_item_view,null);
            TextView mTvView = view.findViewById(R.id.tv_view_item);
            CheckBox mChkView = view.findViewById(R.id.chk_view_item);
            RelativeLayout mRlroot = view.findViewById(R.id.rl_view_root);

            if(items.isChecked){
                mChkView.setChecked(true);
                mTvView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                mChkView.setChecked(false);
            }


            mTvView.setText(items.itemName);
            mChkView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    listener.onTaskUpdate(currentNotes,items,isChecked);
                }
            });
            holder.mLlItemHolder.addView(view);
        }


        holder.mIvTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onDeleteClicked(currentNotes);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class CheckBoxHolder extends RecyclerView.ViewHolder{
        private TextView mTvTitle;
        private LinearLayout mLlItemHolder;
        private ImageView mIvTrash;

        public CheckBoxHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mLlItemHolder = itemView.findViewById(R.id.ll_item_holder);
            mIvTrash = itemView.findViewById(R.id.iv_trash);
        }
    }

    public interface NotesClickListener{
        void onTaskUpdate(RemainderItems remainderItems,Items items,boolean checked);

        void onDeleteClicked(RemainderItems notes);
    }
}
