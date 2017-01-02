package com.fananzapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fananzapp.R;
import com.fananzapp.data.responsedata.CategoryResponseDTO;

import java.util.List;

/**
 * Created by akshay on 2-01-2017.
 */
public class CategorySpinnerAdapter extends BaseAdapter {
    private Context mContext;
    List<CategoryResponseDTO> ageDatas;

    public CategorySpinnerAdapter(Context mContext, List<CategoryResponseDTO> ageDatas) {
        this.mContext = mContext;
        this.ageDatas = ageDatas;
    }

    @Override
    public int getCount() {
        return ageDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return ageDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;
        if (row == null) {

            LayoutInflater theLayoutInflater = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflater.inflate(R.layout.spinner_child_element, null);
            viewHolder = new ViewHolder();
            viewHolder.spinnerChild = (TextView) row.findViewById(R.id.txtSpinnerChild);
            row.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();
        CategoryResponseDTO categoryResponseDTO = ageDatas.get(position);
        viewHolder.spinnerChild.setText(categoryResponseDTO.getCategory());

        return row;
    }

    private class ViewHolder {
        TextView spinnerChild;
    }
}