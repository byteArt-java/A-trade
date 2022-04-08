package HandlerDDEServerQuik;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

class IdealDeal {
    @Getter @Setter private Date openDate;
    @Getter @Setter private String buySell;
    @Getter @Setter private String styleDeal;
    @Getter @Setter private float openPrice;
    @Getter @Setter private int maxReversePrice;
    @Getter @Setter private int maxStopLoss;
    @Getter @Setter private int profit;
    @Getter @Setter private int volumeCandlePrev;

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

    @Override public String toString() {
        return "TxtHandler.IdealDeal{" + "openDate=" + openDate.toString() + ", buySell='" + buySell + '\'' +
                ", styleDeal='" + styleDeal + '\'' + ", openPrice=" + openPrice + ", maxReversePrice="
                + maxReversePrice + ", maxStopLoss=" + maxStopLoss + ", profit=" + profit + ", volumeCandlePrev="
                + volumeCandlePrev + '}';
    }
}
