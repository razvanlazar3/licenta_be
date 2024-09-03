package com.example.demo.crypto.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cryptocurrency {
    private String name;
    private String symbol;
    private double price;
    private double percentChange24h;
    private double marketCap;
    private double volume24h;
    private int rank;
    private double circulatingSupply;
    private String trendingStatus;
}