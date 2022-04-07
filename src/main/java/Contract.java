package main.java;

import java.util.Calendar;
import java.util.Date;

public class Contract {
    private String codeContract;
    private Calendar openDate;
    private float openPrice;
    private Calendar closeDate;
    private float lastClosePrice;
    private float averageClosePrice;
    private String styleDeal;
    private String buySell;
    private int countContracts;
    private float totalNet;
    private int currentCount;
    private boolean isClosedFull;
    private boolean isOpenedCheck;
    private float accumulatedPrice;
    private int accumulatedCount;
    private boolean decrementHour = false;

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

    public boolean getDecrementHour() {
        return decrementHour;
    }

    public void setDecrementHour(boolean decrementHour) {
        this.decrementHour = decrementHour;
    }

    public float getAverageClosePrice() {
        return averageClosePrice;
    }

    public void setAverageClosePrice(float averageClosePrice) {
        this.averageClosePrice = averageClosePrice;
    }

    public float getAccumulatedPrice() {
        return accumulatedPrice;
    }

    public void setAccumulatedPrice(float accumulatedPrice) {
        this.accumulatedPrice = accumulatedPrice;
    }

    public int getAccumulatedCount() {
        return accumulatedCount;
    }

    public void setAccumulatedCount(int accumulatedCount) {
        this.accumulatedCount = accumulatedCount;
    }

    public boolean getOpenedCheck() {
        return isOpenedCheck;
    }

    public void setOpenedCheck(boolean openedCheck) {
        isOpenedCheck = openedCheck;
    }

    public boolean getClosedFull() {
        return isClosedFull;
    }

    public void setClosedFull(boolean closedFull) {
        isClosedFull = closedFull;
    }

    public int getCurrentCount() {
        return currentCount;
    }

    public void setCurrentCount(int currentCount) {
        this.currentCount = currentCount;
    }

    public String getCodeContract() {
        return codeContract;
    }

    public void setCodeContract(String codeContract) {
        this.codeContract = codeContract;
    }

    public Calendar getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Calendar openDate) {
        this.openDate = openDate;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public Calendar getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Calendar closeDate) {
        this.closeDate = closeDate;
    }

    public float getLastClosePrice() {
        return lastClosePrice;
    }

    public void setLastClosePrice(float lastClosePrice) {
        this.lastClosePrice = lastClosePrice;
    }

    public String getStyleDeal() {
        return styleDeal;
    }

    public void setStyleDeal(String styleDeal) {
        this.styleDeal = styleDeal;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    public int getCountContracts() {
        return countContracts;
    }

    public void setCountContracts(int countContracts) {
        this.countContracts = countContracts;
    }

    public float getTotalNet() {
        return totalNet;
    }

    public void setTotalNet(float totalNet) {
        this.totalNet = totalNet;
    }

    private String absentValueStr(String s){
        if (s == null){
            return "absent";
        }
        return s;
    }

    @Override public String toString() {
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

//    this.codeContract = codeContract;
//        this.openDate = openDate; //Sat Dec 20 14:17:41 MSK 21              к примеру
//        this.openPrice = openPrice;
//        this.closeDate = closeDate;//Sat Dec 20 14:17:41 MSK 21              к примеру
//        this.lastClosePrice = lastClosePrice;
//        this.averageClosePrice = averageClosePrice;
//        this.styleDeal = styleDeal;
//        this.buySell = buySell;
//        this.countContracts = countContracts;
//        this.totalNet = totalNet;
//        this.currentCount += countContracts;
//        this.isClosedFull = isClosedFull;
//        this.isOpenedCheck = isOpenedCheck;

}
