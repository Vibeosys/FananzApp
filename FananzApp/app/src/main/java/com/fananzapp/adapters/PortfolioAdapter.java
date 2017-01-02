package com.fananzapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fananzapp.R;
import com.fananzapp.data.responsedata.PortfolioResponse;
import com.fananzapp.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by akshay on 02-01-2017.
 */
public class PortfolioAdapter extends BaseAdapter {

    private ArrayList<PortfolioResponse> mData;
    private Context mContext;
    private ImageLoader mImageLoader;
    private OnItemClick onItemClick;

    public PortfolioAdapter(ArrayList<PortfolioResponse> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View row = convertView;
        ViewHolder viewHolder = null;
        if (row == null) {

            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_portfolio_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtCategory = (TextView) row.findViewById(R.id.txtCategory);
            viewHolder.txtArtistName = (TextView) row.findViewById(R.id.txtArtistName);
            viewHolder.txtMinMaxPrice = (TextView) row.findViewById(R.id.txtMinMaxPrice);
            viewHolder.btnInactive = (TextView) row.findViewById(R.id.btnInactive);
            viewHolder.btnModify = (TextView) row.findViewById(R.id.btnModify);
            viewHolder.coverImg = (NetworkImageView) row.findViewById(R.id.coverImg1);
            row.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();
        final PortfolioResponse portfolioResponse = mData.get(position);
        String category = portfolioResponse.getCategory();
        String artistName = portfolioResponse.getSubscriberName();
        double maxPrice = portfolioResponse.getMaxPrice();
        double minPrice = portfolioResponse.getMinPrice();
        String maxMinPrice = String.format("%.0f-%.0f", minPrice, maxPrice);
        viewHolder.txtCategory.setText(category);
        viewHolder.txtArtistName.setText(artistName);
        viewHolder.txtMinMaxPrice.setText(maxMinPrice);
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();
        final String url = portfolioResponse.getCoverImageUrl();
        if (url == "null" || url.isEmpty() || url == null || url.equals("") || url == "") {
            viewHolder.coverImg.setDefaultImageResId(R.drawable.default_img);
        } else if (TextUtils.isEmpty(url)) {
            viewHolder.coverImg.setImageResource(R.drawable.default_img);
        } else if (url != null && !url.isEmpty()) {
            try {
                mImageLoader.get(url, ImageLoader.getImageListener(viewHolder.coverImg,
                        R.drawable.default_img, R.drawable.default_img));
                viewHolder.coverImg.setImageUrl(url, mImageLoader);
            } catch (Exception e) {
                viewHolder.coverImg.setImageResource(R.drawable.default_img);
            }
        } else {
            viewHolder.coverImg.setImageResource(R.drawable.default_img);
        }
        viewHolder.btnInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick != null) {
                    onItemClick.onInactiveClickListener(portfolioResponse, position);
                }
            }
        });
        viewHolder.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick != null) {
                    onItemClick.onModifyClickListener(portfolioResponse, position);
                }
            }
        });
        return row;
    }

    private class ViewHolder {
        TextView txtCategory, txtArtistName, txtMinMaxPrice;
        NetworkImageView coverImg;
        TextView btnInactive, btnModify;
    }

    public interface OnItemClick {
        public void onInactiveClickListener(PortfolioResponse portfolioResponse, int position);

        public void onModifyClickListener(PortfolioResponse portfolioResponse, int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}