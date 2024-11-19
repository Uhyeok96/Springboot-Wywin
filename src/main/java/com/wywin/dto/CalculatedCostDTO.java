package com.wywin.dto;



public class CalculatedCostDTO {
    private double distance;  // 운송 거리
    private double weight;    // 화물 무게
    private double volume;    // 화물 부피
    private double ratePerKg; // kg당 요금
    private double volumetricFactor; // 부피 환산 비율
    private double fixedFee;  // 고정 비용

    // Getters and Setters
    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getRatePerKg() {
        return ratePerKg;
    }

    public void setRatePerKg(double ratePerKg) {
        this.ratePerKg = ratePerKg;
    }

    public double getVolumetricFactor() {
        return volumetricFactor;
    }

    public void setVolumetricFactor(double volumetricFactor) {
        this.volumetricFactor = volumetricFactor;
    }

    public double getFixedFee() {
        return fixedFee;
    }

    public void setFixedFee(double fixedFee) {
        this.fixedFee = fixedFee;
    }
}
