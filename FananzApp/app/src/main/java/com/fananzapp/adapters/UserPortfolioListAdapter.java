package com.fananzapp.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
public class UserPortfolioListAdapter extends BaseAdapter {

    private ArrayList<PortfolioResponse> mData;
    private Context mContext;
    private ImageLoader mImageLoader;
    private OnItemClick onItemClick;

    public UserPortfolioListAdapter(ArrayList<PortfolioResponse> mData, Context mContext) {
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
            row = theLayoutInflator.inflate(R.layout.row_user_portfolio_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtCategory = (TextView) row.findViewById(R.id.txtCategory);
            viewHolder.txtArtistName = (TextView) row.findViewById(R.id.txtArtistName);
            viewHolder.txtMinMaxPrice = (TextView) row.findViewById(R.id.txtMinMaxPrice);
            viewHolder.btnShowDetails = (TextView) row.findViewById(R.id.btn_show_details);
            viewHolder.btnRequestNow = (TextView) row.findViewById(R.id.btn_request_now);
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
        String url = "";
        try {
            url = portfolioResponse.getCoverImageUrl();
        } catch (NullPointerException e) {
            url = "";
        }

        if (url == "null" || url == null || url.equals("") || url == "") {
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
        viewHolder.btnShowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick != null) {
                    onItemClick.onShowDetailsClickListener(portfolioResponse, position);
                }
            }
        });

        viewHolder.btnRequestNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClick != null) {
                    onItemClick.onRequestNowClickListener(portfolioResponse, position);
                }
            }
        });
        return row;
    }

    private class ViewHolder {
        TextView txtCategory, txtArtistName, txtMinMaxPrice;
        NetworkImageView coverImg;
        TextView btnShowDetails, btnRequestNow;
    }

    public interface OnItemClick {
        public void onShowDetailsClickListener(PortfolioResponse portfolioResponse, int position);

        public void onRequestNowClickListener(PortfolioResponse portfolioResponse, int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
}
