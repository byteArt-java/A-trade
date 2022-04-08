package HandlerDDEServerQuik;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class ReadInFileData {
    //при запуске программы чтение файла, чтобы закинуть в List, уже старые закрытые сделки, записанные до этого программой
    static void readInFileData(List<Contract> bootContractList, Map<String, Contract> tempContractMap, String pathFileBoot) throws IOException, ParseException {
        //  0    1   2   3   4     5     6   7   8     9       10  11  12  13   14     15  16   17  18   19   20
        //SRH2 ,oD= Sat Dec 20 14:17:41 MSK 21 ,oP= 29407.959 ,cD= Sat Dec 20 14:17:41 MSK 21 ,lCP= 0.0 ,aCP= 0.0
        //  21   22   23   24  25  26  27   28      29  30   31    32     33     34
        // ,sD= hour ,bS= buy ,cC= 72 ,tN= 38019.0 ,cC= 72 ,isCF= false ,isOC= true
        BufferedReader fileReader = new BufferedReader(new FileReader(pathFileBoot));
        int countLines = 0;
        while (fileReader.ready()){
            String[] str = fileReader.readLine().split("\\s");

            String codeContract = str[0];
            float openPrice = Float.parseFloat(str[9]);
            float lastClosePrice = Float.parseFloat(str[18]);
            float averageClosePrice = Float.parseFloat(str[20]);
            String styleDeal = str[22];
            String buySell = str[24];
            int countContract = Integer.parseInt(str[26]);
            float totalNet = Float.parseFloat(str[28]);
            int currentCount = Integer.parseInt(str[30]);
            boolean isClosedFull = Boolean.parseBoolean(str[32]);
            boolean isOpenedCheck = Boolean.parseBoolean(str[34]);


            int openMonth = ConvertMonthToInt.convertMonth(str[3]);
            int openNumberDay = Integer.parseInt(str[4]);
            String openTime = str[5];
            String openYear = str[7];
            String strOpen = String.format("%d.%d.%s - %s",openNumberDay,openMonth,openYear,openTime);
            SimpleDateFormat simpleDateFormatOpen = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            Date date1 = simpleDateFormatOpen.parse(strOpen);
            Calendar calendarOpen = new GregorianCalendar();
            calendarOpen.setTime(date1);

            int closeMonth = ConvertMonthToInt.convertMonth(str[12]);
            int closeNumberDay = Integer.parseInt(str[13]);
            String closeTime = str[14];
            String closeYear = str[16];
            String strClose = String.format("%d.%d.%s - %s",closeNumberDay,closeMonth,closeYear,closeTime);
            SimpleDateFormat simpleDateFormatClose = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            Date date2 = simpleDateFormatClose.parse(strClose);
            Calendar calendarClose = new GregorianCalendar();
            calendarClose.setTime(date2);

            Contract contract = new Contract(codeContract,calendarOpen,openPrice,calendarClose,lastClosePrice,averageClosePrice,
                    styleDeal,buySell,countContract,totalNet,currentCount,isClosedFull,isOpenedCheck);
            contract.setCurrentCount(currentCount);

            if (!isClosedFull && isOpenedCheck){
                tempContractMap.put(codeContract,contract);
            }else {
                bootContractList.add(contract);
            }
            countLines++;
//            System.out.println("считали строку номер " + countLines);
        }
    }
}
