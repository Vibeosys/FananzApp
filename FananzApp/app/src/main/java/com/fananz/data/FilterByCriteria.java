package com.fananz.data;

import com.fananz.data.responsedata.PortfolioResponse;

import java.util.ArrayList;

/**
 * Created by akshay on 08-02-2017.
 */
public interface FilterByCriteria {
    ArrayList<PortfolioResponse> meetCriteria(int value, ArrayList<PortfolioResponse> data);
}
