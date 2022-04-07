package TxtHandler;

import TxtHandler.Contract;
import TxtHandler.ReadInFileData;
import TxtHandler.ResourcesStatic;

import java.io.IOException;
import java.text.ParseException;

public class YieldsPeriod extends ResourcesStatic {
    static void yieldsForPeriodOPENDATE(int yearIn) throws IOException, ParseException {
        ReadInFileData.readInFileData(bootContractList,tempContractMap,pathFileBoot);
        float yields = 0;
        for (Contract contract : bootContractList) {//Mon Jan 16 14:54:14 MSK 21
            String line = contract.getOpenDate().getTime().toString();
            String[] parseStr = line.split("\\s");
            int yearLong = Integer.parseInt(parseStr[5]) + 2000;
            int yearShort = Integer.parseInt(parseStr[5]);
            if (yearShort == yearIn || yearLong == yearIn){
                yields = yields + contract.getTotalNet();
            }
        }
        System.out.printf("Yields for period %d year  = %.2f RUB\n",yearIn,yields);
    }

    static void yieldsForPeriodOPENDATE(int yearIn, int monthIn) throws IOException, ParseException {
        ReadInFileData.readInFileData(bootContractList,tempContractMap,pathFileBoot);
        float yields = 0;
        for (Contract contract : bootContractList) {//Mon Jan 16 14:54:14 MSK 2021
            String line = contract.getOpenDate().getTime().toString();
            String[] parseStr = line.split("\\s");
            int month = ConvertMonthToInt.convertMonth(parseStr[1]);
            int yearLong = Integer.parseInt(parseStr[5]) + 2000;
            int yearShort = Integer.parseInt(parseStr[5]);
            if (yearShort == yearIn && month == monthIn || yearLong == yearIn && month == monthIn){
                yields = yields + contract.getTotalNet();
            }
        }
        System.out.printf("Yields for period %d year and %d month = %.2f RUB\n",yearIn,monthIn,yields);
    }

    static void yieldsForPeriodOPENDATE(String codeContract,int yearIn, int monthIn) throws IOException, ParseException {
        ReadInFileData.readInFileData(bootContractList,tempContractMap,pathFileBoot);
        float yields = 0;
        for (Contract contract : bootContractList) {//Mon Jan 16 14:54:14 MSK 2021
            String code = contract.getCodeContract().substring(0,contract.getCodeContract().length() - 2).toUpperCase();
            String line = contract.getOpenDate().getTime().toString();
            String[] parseStr = line.split("\\s");
            int month = ConvertMonthToInt.convertMonth(parseStr[1]);
            int yearLong = Integer.parseInt(parseStr[5]) + 2000;
            int yearShort = Integer.parseInt(parseStr[5]);
            if (yearShort == yearIn && month == monthIn && code.equals(codeContract.toUpperCase())
                    || yearLong == yearIn && month == monthIn && code.equals(codeContract.toUpperCase())){
                yields = yields + contract.getTotalNet();
            }
        }
        System.out.printf("Yields for period %d year and %d month.TxtHandler.Contract Code %s = %.2f RUB\n",
                yearIn,monthIn,codeContract,yields);
    }

    static void yieldsForPeriodOPENDATE(int yearIn, String... codeContract) throws IOException, ParseException {
        for (String s : codeContract) {
            ReadInFileData.readInFileData(bootContractList,tempContractMap,pathFileBoot);
            float yields = 0;
            for (Contract contract : bootContractList) {//Mon Jan 16 14:54:14 MSK 2021
                String code = contract.getCodeContract().substring(0,contract.getCodeContract().length() - 2).toUpperCase();
                String line = contract.getOpenDate().getTime().toString();
                String[] parseStr = line.split("\\s");
                int yearLong = Integer.parseInt(parseStr[5]) + 2000;
                int yearShort = Integer.parseInt(parseStr[5]);
                if (yearShort == yearIn && code.equals(s.toUpperCase())
                        || yearLong == yearIn && code.equals(s.toUpperCase())){
                    yields = yields + contract.getTotalNet();
                }
            }
            System.out.printf("Yields for period %d year.TxtHandler.Contract Code %s = %.2f RUB\n",
                    yearIn,s,yields);
        }
    }
}
