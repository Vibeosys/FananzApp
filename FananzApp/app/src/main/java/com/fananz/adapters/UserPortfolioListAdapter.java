package com.fananz.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fananz.R;
import com.fananz.data.FilterByCategory;
import com.fananz.data.FilterByCriteria;
import com.fananz.data.FilterByPrice;
import com.fananz.data.FilterBySubCategory;
import com.fananz.data.responsedata.PortfolioResponse;
import com.fananz.utils.CustomVolleyRequestQueue;

import java.util.ArrayList;
import java.util.Collections;

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
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();
        if (row == null) {

            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_user_portfolio_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtCategory = (TextView) row.findViewById(R.id.txtCategory);
            viewHolder.txtArtistName = (TextView) row.findViewById(R.id.txtArtistName);
            viewHolder.txtMinMaxPrice = (TextView) row.findViewById(R.id.txtMinMaxPrice);
            viewHolder.txtSubCategory = (TextView) row.findViewById(R.id.txtSubCategory);
            viewHolder.btnShowDetails = (TextView) row.findViewById(R.id.btn_show_details);
            viewHolder.btnRequestNow = (TextView) row.findViewById(R.id.btn_request_now);
            viewHolder.coverImg = (NetworkImageView) row.findViewById(R.id.coverImg1);
            viewHolder.subLay = (LinearLayout) row.findViewById(R.id.subLay);
            row.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.coverImg.setImageUrl(null, mImageLoader);
        }
        final PortfolioResponse portfolioResponse = mData.get(position);
        String category = portfolioResponse.getCategory();
        String subCategory = portfolioResponse.getSubcategory();
        String artistName = portfolioResponse.getSubscriberName();
        double maxPrice = portfolioResponse.getMaxPrice();
        double minPrice = portfolioResponse.getMinPrice();
        String maxMinPrice = String.format("%.0f - %.0f", minPrice, maxPrice);
        viewHolder.txtCategory.setText(category);
        viewHolder.txtCategory.setText(category);
        viewHolder.txtArtistName.setText(artistName);
        viewHolder.txtMinMaxPrice.setText(maxMinPrice);

        try {
            if (!subCategory.isEmpty()) {
                viewHolder.txtSubCategory.setText(" - " + subCategory);
            }
        } catch (NullPointerException e) {
            viewHolder.txtSubCategory.setText("");
            viewHolder.subLay.setVisibility(View.GONE);
        } catch (Exception e) {
            viewHolder.txtSubCategory.setText("");
            viewHolder.subLay.setVisibility(View.GONE);
        }
        String url = portfolioResponse.getCoverImageUrl();

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
        TextView btnShowDetails, btnRequestNow, txtSubCategory;
        LinearLayout subLay;
    }

    public interface OnItemClick {
        public void onShowDetailsClickListener(PortfolioResponse portfolioResponse, int position);

        public void onRequestNowClickListener(PortfolioResponse portfolioResponse, int position);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void sortAdapter(int condition) {
        if (condition == 1) {
            Collections.sort(mData, Collections.reverseOrder(new PortfolioResponse.PriceComparator()));
        } else {
            Collections.sort(mData, new PortfolioResponse.PriceComparator());
        }
        notifyDataSetChanged();
    }

    public void filterAdapter(int selectedPrice, int categoryId, int subCategoryId) {
        FilterByCriteria filterByPrice = new FilterByPrice();
        FilterByCriteria filterByCategory = new FilterByCategory();
        FilterByCriteria filterBySubCategory = new FilterBySubCategory();
        ArrayList<PortfolioResponse> filterData = new ArrayList<>();
        if (selectedPrice > 1000 && categoryId != 0) {
            filterData = filterByPrice.meetCriteria(selectedPrice, mData);
            filterData = filterByCategory.meetCriteria(categoryId, filterData);
            if (subCategoryId != 0) {
                filterData = filterBySubCategory.meetCriteria(subCategoryId, filterData);
            }
            this.mData = filterData;
            notifyDataSetChanged();
        } else if (selectedPrice > 1000) {
            filterData = filterByPrice.meetCriteria(selectedPrice, mData);
            this.mData = filterData;
            notifyDataSetChanged();
        } else if (categoryId != 0) {
            filterData = filterByCategory.meetCriteria(categoryId, mData);
            if (subCategoryId != 0) {
                filterData = filterBySubCategory.meetCriteria(subCategoryId, mData);
            }
            this.mData = filterData;
            notifyDataSetChanged();
        }

    }

    public ArrayList<PortfolioResponse> getData() {
        return mData;
    }

    public void setData(ArrayList<PortfolioResponse> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }
}
