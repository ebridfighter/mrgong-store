package uk.co.ribot.androidboilerplate.data.model.net.response;

/**
 * Created by mike on 2017/11/2.
 */

public class DashBoardResponse {
    /**
     * maturityNum : 0
     * purchaseAmount : 0.160992
     * adventNum : 0
     * maturityValue : 0
     * adventValue : 125.8
     * adventNum: 0
     * totalNumber:0
     */

    private int maturityNum;
    private double purchaseAmount;
    private int adventNum;
    private double maturityValue;
    private double adventValue;
    private int totalNumber;

    public int getMaturityNum() {
        return maturityNum;
    }

    public void setMaturityNum(int maturityNum) {
        this.maturityNum = maturityNum;
    }

    public double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public int getAdventNum() {
        return adventNum;
    }

    public void setAdventNum(int adventNum) {
        this.adventNum = adventNum;
    }

    public double getMaturityValue() {
        return maturityValue;
    }

    public void setMaturityValue(double maturityValue) {
        this.maturityValue = maturityValue;
    }

    public double getAdventValue() {
        return adventValue;
    }

    public void setAdventValue(double adventValue) {
        this.adventValue = adventValue;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getTotalNumber() {
        return totalNumber;
    }
}
