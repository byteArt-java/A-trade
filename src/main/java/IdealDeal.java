import java.util.Date;

public class IdealDeal {
    private Date openDate;
    private String buySell;
    private String styleDeal;
    private float openPrice;
    private int maxReversePrice;
    private int maxStopLoss;
    private int profit;
    private int volumeCandlePrev;

    public IdealDeal(Date openDate, String buySell, String styleDeal, float openPrice, int maxReversePrice,
                     int maxStopLoss, int profit, int volumeCandlePrev) {
        this.openDate = openDate;
        this.buySell = buySell;
        this.styleDeal = styleDeal;
        this.openPrice = openPrice;
        this.maxReversePrice = maxReversePrice;
        this.maxStopLoss = maxStopLoss;
        this.profit = profit;
        this.volumeCandlePrev = volumeCandlePrev;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    public String getStyleDeal() {
        return styleDeal;
    }

    public void setStyleDeal(String styleDeal) {
        this.styleDeal = styleDeal;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public int getMaxReversePrice() {
        return maxReversePrice;
    }

    public void setMaxReversePrice(int maxReversePrice) {
        this.maxReversePrice = maxReversePrice;
    }

    public int getMaxStopLoss() {
        return maxStopLoss;
    }

    public void setMaxStopLoss(int maxStopLoss) {
        this.maxStopLoss = maxStopLoss;
    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    public int getVolumeCandlePrev() {
        return volumeCandlePrev;
    }

    public void setVolumeCandlePrev(int volumeCandlePrev) {
        this.volumeCandlePrev = volumeCandlePrev;
    }

    @Override public String toString() {
        return "IdealDeal{" + "openDate=" + openDate.toString() + ", buySell='" + buySell + '\'' +
                ", styleDeal='" + styleDeal + '\'' + ", openPrice=" + openPrice + ", maxReversePrice="
                + maxReversePrice + ", maxStopLoss=" + maxStopLoss + ", profit=" + profit + ", volumeCandlePrev="
                + volumeCandlePrev + '}';
    }
}
