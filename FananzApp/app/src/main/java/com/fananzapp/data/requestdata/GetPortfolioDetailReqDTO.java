package com.fananzapp.data.requestdata;

import com.fananzapp.data.BaseDTO;

/**
 * Created by akshay on 04-01-2017.
 */
public class GetPortfolioDetailReqDTO extends BaseDTO {

    private int portfolioId;

    public GetPortfolioDetailReqDTO(int portfolioId) {
        this.portfolioId = portfolioId;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(int portfolioId) {
        this.portfolioId = portfolioId;
    }
}
