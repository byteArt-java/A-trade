package HandlerDDEServerQuik;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;


class Contract {
    @Getter @Setter private String codeContract;
    @Getter @Setter private Calendar openDate;
    @Getter @Setter private float openPrice;
    @Getter @Setter private Calendar closeDate;
    @Getter @Setter private float lastClosePrice;
    @Getter @Setter private float averageClosePrice;
    @Getter @Setter private String styleDeal;
    @Getter @Setter private String buySell;
    @Getter @Setter private int countContracts;
    @Getter @Setter private float totalNet;
    @Getter @Setter private int currentCount;
    @Getter @Setter private boolean isClosedFull;
    @Getter @Setter private boolean isOpenedCheck;
    @Getter @Setter private float accumulatedPrice;
    @Getter @Setter private int accumulatedCount;
    @Getter @Setter private boolean decrementHour = false;

    public Contract(String codeContract){
        this.codeContract = codeContract;
    }

    public Contract(String codeContract, Calendar openDate, float openPrice, Calendar closeDate,
                    float lastClosePrice,float averageClosePrice,String styleDeal, String buySell, int countContracts,
                    float totalNet, int currentCount,boolean isClosedFull, boolean isOpenedCheck) {
        this.codeContract = codeContract;
        this.openDate = openDate;
        this.openPrice = openPrice;
        this.closeDate = closeDate;
        this.lastClosePrice = lastClosePrice;
        this.averageClosePrice = averageClosePrice;
        this.styleDeal = styleDeal;
        this.buySell = buySell;
        this.countContracts = countContracts;
        this.totalNet = totalNet;
        this.currentCount += countContracts;
        this.isClosedFull = isClosedFull;
        this.isOpenedCheck = isOpenedCheck;
    }

    public Contract(String codeContract, Calendar openDate, float openPrice, Calendar closeDate,
                    float lastClosePrice, float averageClosePrice, String styleDeal, String buySell, int countContracts,
                    float totalNet) {
        this.codeContract = codeContract;
        this.openDate = openDate;
        this.openPrice = openPrice;
        this.closeDate = closeDate;
        this.lastClosePrice = lastClosePrice;
        this.averageClosePrice = averageClosePrice;
        this.styleDeal = styleDeal;
        this.buySell = buySell;
        this.countContracts = countContracts;
        this.totalNet = totalNet;
        this.currentCount += countContracts;
        this.isClosedFull = true;
        this.isOpenedCheck = false;
    }


    @Override
    public String toString() {
        return "codeContract=" + codeContract + " ,openDate=" + openDate.getTime() + " ,openPrice=" + openPrice +
                " ,closeDate=" + closeDate.getTime() + " ,lastClosePrice=" + lastClosePrice +
                " ,averageClosePrice=" + averageClosePrice + " ,styleDeal=" + styleDeal + " ,buySell=" + buySell +
                " ,countContracts=" + countContracts + " ,totalNet=" + totalNet + " ,currentCount=" + currentCount +
                " ,isClosedFull=" + isClosedFull + " ,isOpenedCheck=" + isOpenedCheck + "\n";
    }

    public String writeBootFromJavaDeals(){
        return codeContract + " ,oD= " + openDate.getTime().toString() + " ,oP= " + openPrice +
                " ,cD= " + closeDate.getTime().toString() + " ,lCP= " + lastClosePrice +
                " ,aCP= " + averageClosePrice + " ,sD= " + styleDeal + " ,bS= " + buySell +
                " ,cC= " + countContracts + " ,tN= " + totalNet + " ,cC= " + currentCount +
                " ,isCF= " + isClosedFull + " ,isOC= " + isOpenedCheck + "\n";
    }

}
