package com.example.a23__project_1.placeList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a23__project_1.R;

import java.util.List;

public class categoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context mainContext;

    // 클릭 아이템 리스너
    private OnItemClickListener onItemClickListener = null;
    // 클릭 리스너 인터페이스
    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    private List<String> dataList;
    /** 리스트 추가 예정 **/
    public categoryAdapter(Context mainContext, List<String> dataList) {
        this.mainContext = mainContext;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_second_category_item, parent, false);
        return new categoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder vh = (CategoryViewHolder) holder;
        vh.button.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private Button button;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.btn_category);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAbsoluteAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체 메서드 호출
                        if(onItemClickListener != null) {
                            onItemClickListener.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }


}
