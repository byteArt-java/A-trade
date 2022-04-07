package main.java;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Run extends RateInstrumentHandler{

    private static final String pathFileBoot = "F://Фондовый рынок/A-Trade/BootFromJavaDeals.txt";
    private static final String pathFileDDEServer = "F://Фондовый рынок/A-Trade/pathFileDDEServer.txt";
    //путь к файлу в кот. находится данные с Quik DDESERVER


    private static List<Contract> bootContractList = new ArrayList<>();//здесь хранятся все загруж закрытые сделки
    private static Map<String, Contract> tempContractMap = new HashMap<>();//здесь хранятся незакрытые времен сделки
    private static Map<String,Float> rateInstrument = new HashMap<>();//на какой коэф умножать
    private static final String pathFileReportBroker = "F://Фондовый рынок//A-Trade//Все сделки из отчета Брокера по 28Декабря 2021года.txt";//путь к файлу в кот. находится данные и личного журнала сделок
    private static final String pathFileIdealDeal = "F://Фондовый рынок//A-Trade//3.txt";//путь к файлу в кот. находится данные и личного журнала сделок
    private static int strokesNumber = 0;//счетчик считывание строки метода

    public static void main(String[] args) throws ParseException, IOException{
        long start = System.currentTimeMillis();
        boolean isZeroYield = false;

        //загрузка сделок через Quik DDE Server.
        // Дата указана для того, чтобы помечать сделки проведенные по Стратегии(Идеальная сделка)
        loadDealsFromDDEServer("09.03.2022 07:00:00","SI",isZeroYield);

//        addContractReportBroker(pathFileReportBroker);//загрузка сделок из Брокерского отчета
//
//        yieldsIdealDeal();//доходность по стратегии идеальной сделки
//
//        yieldsForPeriodOPENDATE(2022);//доходность за определенный период
//        yieldsForPeriodOPENDATE(2022,2);//доходность за определенный период
//        yieldsForPeriodOPENDATE("ED",2021,11);//доходность за определенный период
//        yieldsForPeriodOPENDATE(2022,"SI","RI","BR","GD","ED");//доходность за определенный период
//
//        analyzeAverageDaysBetweenIdealDeal(pathFileIdealDeal);//среднее время между идеальными сделками
//
//        analyzeIdealDealBetweenStopFixAndFloatingStop("F://Фондовый рынок/A-Trade//idealDeal.txt",40,
//                140);//анализ прибыльности при различных параметрах стопа по идеальной стратегии

        long past = System.currentTimeMillis();
        System.out.println("Прошло миллисекунд  = " + (past - start));
    }//main

    //в конце программы запись всех закрытых и незакрытых сделок в файл
    private static void writeOutFileData(List<Contract> bootContractList,Map<String, Contract> tempContractMap,
                                         String pathFileBoot, Map<String,String> dataContracts,
                                         String timeAfter,boolean isZeroYield) throws IOException, ParseException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(pathFileBoot));
        for (Contract contract : bootContractList) {
            contract.setCurrentCount(0);
            if (dataContracts.containsKey(contract.getCodeContract())){
                SimpleDateFormat simpleAfter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date dateAfterCorrelation = simpleAfter.parse(timeAfter);

                //Mon Dec 20 14:17:41 MSK 2021
                String[] partsList = contract.getOpenDate().getTime().toString().split("\\s");
                partsList[1] = String.valueOf(convertMonth(partsList[1]));
                String s = partsList[1] + " " + partsList[2] + " " + partsList[3] + " " + partsList[5];
                SimpleDateFormat sdf1 = new SimpleDateFormat("MM dd HH:mm:ss yy");
                Date dateContract = sdf1.parse(s);

                //  0   1    2      3    4  5  6   7    8      9    10
                //"Sep 15 11:59:01 MSK 2024 - Dec 15 11:59:00 MSK 2024"
                String[] partsMap = dataContracts.get(contract.getCodeContract()).split("\\s");

                partsMap[0] = String.valueOf(convertMonth(partsMap[0]));
                String sBefore = partsMap[0] + " " + partsMap[1] + " " + partsMap[2] + " " + partsMap[4];
                SimpleDateFormat sdfBefore = new SimpleDateFormat("MM dd HH:mm:ss yyyy");
                Date dateBefore = sdfBefore.parse(sBefore);

                partsMap[6] = String.valueOf(convertMonth(partsMap[6]));
                String sAfter = partsMap[6] + " " + partsMap[7] + " " + partsMap[8] + " " + partsMap[10];
                SimpleDateFormat sdfAfter = new SimpleDateFormat("MM dd HH:mm:ss yyyy");
                Date dateAfter = sdfAfter.parse(sAfter);

                if (dateBefore.getTime() < dateContract.getTime() && dateAfter.getTime() > dateContract.getTime() &&
                dateContract.getTime() > dateAfterCorrelation.getTime()){
                    contract.setStyleDeal("idealDeal");
                }else {
                    contract.setStyleDeal("hour");
                }
            }
            fileWriter.write(contract.writeBootFromJavaDeals());
        }
        for (Contract value : tempContractMap.values()) {
            if (isZeroYield){
                value.setTotalNet(0);//это чтобы правильно считал прибыль, тк addContractDDEServer,учитывает прибыль от цены
                // закрытия и открытия, а addContractReportBroker учитывает накопленную маржу за каждый день.
            }
            fileWriter.write(value.writeBootFromJavaDeals());
        }
        fileWriter.flush();
        fileWriter.close();
    }

    //при запуске программы чтение файла, чтобы закинуть в List, уже старые закрытые сделки, записанные до этого программой
    private static void readInFileData(List<Contract> bootContractList,Map<String, Contract> tempContractMap, String pathFileBoot) throws IOException, ParseException {
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


            int openMonth = convertMonth(str[3]);
            int openNumberDay = Integer.parseInt(str[4]);
            String openTime = str[5];
            String openYear = str[7];
            String strOpen = String.format("%d.%d.%s - %s",openNumberDay,openMonth,openYear,openTime);
            SimpleDateFormat simpleDateFormatOpen = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            Date date1 = simpleDateFormatOpen.parse(strOpen);
            Calendar calendarOpen = new GregorianCalendar();
            calendarOpen.setTime(date1);

            int closeMonth = convertMonth(str[12]);
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


    //метод ниже добавл сделки из личного журнала, где все сделки обратно закр. Где lastClosePrice = AverageClosePrice
    // и дата закрытия всегда = дата открытия. В listContractsDealNoCorrelation не попадают сделки из стратегии по Корреляции
    private static void addContractReportBroker(String pathFileReportBroker) throws ParseException, IOException {//rate 1.487f,1f,7.36f
        BufferedReader fileReader = new BufferedReader(new FileReader(pathFileReportBroker));
        while (fileReader.ready()){
            strokesNumber++;
            String line = fileReader.readLine();
            if (line.isEmpty()){
                System.out.println("Загрузка месяца произошла");
                continue;
            }
            String[] lineArray = line.split("\\s");

            String codeContract = lineArray[0].toUpperCase();//Код контракта

            float yields = 0;
            float price = 0;
            String time = null;
            int countContract = 0;
            String date = null;

            if (!lineArray[4].isEmpty()){//RTS-3.19M210319CA130000
                float f = 0;
                try {
                    f = tempContractMap.get(codeContract).getTotalNet();
                }catch (NullPointerException e){
                    System.out.println("У этого контракта не загружена предыдущая сделка");
                }
                String l = lineArray[10];
                if (!lineArray[11].isEmpty()){
                    l = l + lineArray[11];
                }
                String u = l.replace(",",".");
                if (u.isEmpty()){
                    u = "0";
                }
                float g = Float.parseFloat(u);
                tempContractMap.get(codeContract).setTotalNet(f + g);
                continue;
            }

            String buySell = convertOperationReportBroker(lineArray[5],lineArray[8]);//пытаемся понять, покупка или продажа
            if (buySell.equals("buy")){
                if (lineArray[6].contains(",")){//если цена не содержит пробел
                    String a = lineArray[6].replace(",",".");
                    price = Float.parseFloat(a);
                    String lines;
                    if (lineArray[11].contains(",")){//если вар.маржа содержит пробел
                        lines = lineArray[10] + lineArray[11];
                        String b = lines.replace(",",".");
                        if (b.isEmpty()){
                            b = "0";
                        }
                        yields = Float.parseFloat(b);
                    }else {
                        String c = lineArray[10].replace(",",".");
                        if (c.isEmpty()){
                            c = "0";
                        }
                        yields = Float.parseFloat(c);
                    }
                }else if (lineArray[7].contains(",")){//если цена содержит пробел
                    String g = lineArray[6] + lineArray[7];
                    String d = g.replace(",",".");
                    price = Float.parseFloat(d);
                    String lines;
                    if (lineArray[11].contains(",")){//если прибыль не содержит пробел
                        String c = lineArray[11].replace(",",".");
                        if (c.isEmpty()){
                            c = "0";
                        }
                        yields = Float.parseFloat(c);
                    }else {//если прибыль содержит пробел
                        lines = lineArray[11] + lineArray[12];
                        String b = lines.replace(",",".");
                        if (b.isEmpty()){
                            b = "0";
                        }
                        yields = Float.parseFloat(b);
                    }
                }
                countContract = Integer.parseInt(lineArray[5]);//количество контрактов
                date = lineArray[1];
                time = lineArray[3];//время сделки

            }else if (buySell.equals("sell")){
                if (lineArray[8].contains(",")){//если цена сделки в EDZ
                    String a = lineArray[8].replace(",",".");
                    price = Float.parseFloat(a);
                    if (!lineArray[11].isEmpty()){
                        String lines = lineArray[10] + lineArray[11];
                        String parse = lines.replace(",",".");
                        yields = Float.parseFloat(parse);
                    }else {
                        String parse = lineArray[10].replace(",",".");
                        if (parse.isEmpty()){
                            parse = "0";
                        }
                        yields = Float.parseFloat(parse);
                    }
                }else if (lineArray[9].contains(",")){
                    if (!lineArray[12].equals("")){
                        String lineTotal = lineArray[11] + lineArray[12].replace(",",".");
                        if (lineTotal.isEmpty()){
                            lineTotal = "0";
                        }
                        yields = Float.parseFloat(lineTotal);
                    }else {
                        String line11 = lineArray[11].replace(",",".");
                        if (line11.isEmpty()){
                            line11 = "0";
                        }
                        yields = Float.parseFloat(line11);
                    }
                    String b = lineArray[9].replace(",",".");
                    String a = lineArray[8];
                    String total = a + b;
                    price = Float.parseFloat(total);
                }else {
                    price = Float.parseFloat(lineArray[8]);//цена сделки
                }
                countContract = Integer.parseInt(lineArray[7]);//количество контрактов
                date = lineArray[1];
                time = lineArray[3];//время сделки
            }

            String d = String.format("%s - %s",date,time);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            Date date1 = simpleDateFormat.parse(d);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date1);

            System.out.println(d + " = " + codeContract + " = " + buySell + " = " + price + " = " + countContract +
                    " = " + yields);

            //открываем новую позицию, и закидываем во временный файл, до тех пор, пока не будет обратно закрыта
            if (!tempContractMap.containsKey(codeContract)){
                openNewReport(countContract,codeContract,calendar,price,time,buySell,yields);
            }
            //если уже открыт контракт и мы наращиваем еще открытую позицию на продажу или покупку
            else if (tempContractMap.containsKey(codeContract)
                    && tempContractMap.get(codeContract).getBuySell().equals(buySell) &&
                    !tempContractMap.get(codeContract).getClosedFull()){
                addPositionReport(price,countContract,time,codeContract,yields);
            }
            //если уже открыт контракт и идет встречная сделка на закрытие
            else if (tempContractMap.containsKey(codeContract)
                    && !tempContractMap.get(codeContract).getBuySell().equals(buySell) &&
                    !tempContractMap.get(codeContract).getClosedFull()){
                //полное закрытие, когда открытых контрактов = приходящих контрактов
                if (tempContractMap.containsKey(codeContract) &&
                        tempContractMap.get(codeContract).getCurrentCount() == countContract){
                    closeMethodReport(countContract,codeContract,calendar,price,time,yields);
                }
                //если контрактов меньше, чем открытых контрактов// Example: currentCount=10 countContract=5
                else if (tempContractMap.containsKey(codeContract) &&
                        tempContractMap.get(codeContract).getCurrentCount() > countContract){
                    partsCloseReport(countContract,codeContract,calendar,price,time,yields);
                }
                //если контрактов больше, чем открытых контрактов, нужно перевернуться
                else if (tempContractMap.containsKey(codeContract) &&
                        tempContractMap.get(codeContract).getCurrentCount() < countContract){
                    partsCloseOverReport(countContract,codeContract,calendar,price,
                            time,buySell,yields);
                }
            }
        }

    }//addContractReportBroker

    //метод ниже добавл сделки в ArrayList, с учетом того, что сделка может быть не закрыта
    //0         1          2          3     4       5         6       7     8
    //39	20.12.2021	11:17:41	SRH2 [FORTS: Фьючерсы]	Купля	29476	3
    private static void addContractDDEServer(String pathFile) throws ParseException, IOException {//rate 1.487f,1f,7.36f
        BufferedReader fileReader = new BufferedReader(new FileReader(pathFile));
        while (fileReader.ready()){
            Calendar calendar = new GregorianCalendar();
            strokesNumber++;
            String[] line = fileReader.readLine().split("\\s");
            String codeContract = line[3].toUpperCase();//Код контракта

            //=====если в карте коэфф по коду контракта нет, то загрузить, оптимизация лучше,зачем каждый раз загружать
            //=====коэффициенты с Московской биржи, если из фала только контракты с фиксированным коэффициентом
            if (!rateInstrument.containsKey(codeContract.substring(0,2))){
                rateInstrument.putAll(addRateInstrument(codeContract.substring(0,2)));
            }
            //=========================================================================================================

            float price;
            String buySell = convertOperationDDE(line[6]);//конвертируем наимен операции под единый стандарт buy или sell
            if (line[7].contains(",")){//если цена сделки в EDZ
                String a = line[7].replace(",",".");
                price = Float.parseFloat(a);
            }else {
                price = Float.parseFloat(line[7]);//цена сделки
            }
            int countContract = Integer.parseInt(line[8]);//количество контрактов

            String date = line[1];
            String s = line[2];

            String d = String.format("%s - %s",date,s);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            Date date1 = simpleDateFormat.parse(d);
            calendar.setTime(date1);
            System.out.println(d + " = " + codeContract + " = " + buySell + " = " + price + " = " + countContract);

            //открываем новую позицию, и закидываем во временный файл, до тех пор, пока не будет обратно закрыта
            if (!tempContractMap.containsKey(codeContract)){
                openNewDDE(countContract,codeContract,calendar,price,s,buySell);
            }
            //если уже открыт контракт и мы наращиваем еще открытую позицию на продажу или покупку
            else if (tempContractMap.containsKey(codeContract)
                    && tempContractMap.get(codeContract).getBuySell().equals(buySell) &&
                    !tempContractMap.get(codeContract).getClosedFull()){
                addPositionDDE(price,countContract,s,codeContract);
            }
            //если уже открыт контракт и идет встречная сделка на закрытие
            else if (tempContractMap.containsKey(codeContract)
                    && !tempContractMap.get(codeContract).getBuySell().equals(buySell) &&
                    !tempContractMap.get(codeContract).getClosedFull()){
                //полное закрытие, когда открытых контрактов = приходящих контрактов
                if (tempContractMap.containsKey(codeContract) &&
                        tempContractMap.get(codeContract).getCurrentCount() == countContract){
                    closeMethodDDE(countContract,codeContract,calendar,price,s);
                }
                //если контрактов меньше, чем открытых контрактов// Example: currentCount=10 countContract=5
                else if (tempContractMap.containsKey(codeContract) &&
                        tempContractMap.get(codeContract).getCurrentCount() > countContract){
                    partsCloseDDE(countContract,codeContract,calendar,price,s);
                }
                //если контрактов больше, чем открытых контрактов, нужно перевернуться
                else if (tempContractMap.containsKey(codeContract) &&
                        tempContractMap.get(codeContract).getCurrentCount() < countContract){
                    partsCloseOverDDE(countContract,codeContract,calendar,price,s,buySell);
                }
            }
        }

    }//addContractDDEServer

    private static void openNewDDE(int countContract, String codeContract, Calendar date, float price,
                                   String s, String buySell){
        Contract contract = new Contract(codeContract,date,price,date, 0, 0,
                "hour",buySell,countContract,0,countContract,false,
                true);
        tempContractMap.put(codeContract,contract);
        System.out.println("добавили позицию  = " + strokesNumber + " Код контракта " + codeContract);
    }

    private static void addPositionDDE(float price, int countContract, String s, String codeContract){
        float oldDeal = tempContractMap.get(codeContract).getOpenPrice() * tempContractMap.get(codeContract).getCurrentCount();
        float newDeal = price * countContract;
        float totalCount = tempContractMap.get(codeContract).getCurrentCount() + countContract;
        float totalPrice = (oldDeal + newDeal) / totalCount;
        tempContractMap.get(codeContract).setOpenPrice(totalPrice);
        tempContractMap.get(codeContract).setCountContracts((int) totalCount);
        tempContractMap.get(codeContract).setCurrentCount((int) totalCount);
        System.out.println("добавили к существующей позиции = " + strokesNumber + " время = " + s);
    }

    private static void partsCloseOverDDE(int countContract, String codeContract, Calendar date, float price,
                                          String s, String buySell){
        int closeCount = tempContractMap.get(codeContract).getCurrentCount();
        int openCount = countContract - closeCount;
        closeMethodDDE(closeCount,codeContract,date,price,s);
        openNewDDE(openCount,codeContract,date,price,s,buySell);
        tempContractMap.get(codeContract).setOpenedCheck(true);
    }
    //нам для закрытия не нужна средняя цена закрытия, она нужна только для учета, для фиксации фин.рез. можно каждый раз
    //делать новую цену закрытия
    private static void partsCloseDDE(int countContract, String codeContract, Calendar date, float price, String s){
        float averageClosePrice = ((tempContractMap.get(codeContract).getAccumulatedCount() *
                tempContractMap.get(codeContract).getAccumulatedPrice()) +
                (countContract * price)) / (tempContractMap.get(codeContract).getAccumulatedCount() + countContract);
        tempContractMap.get(codeContract).setAccumulatedCount(tempContractMap.get(codeContract).getAccumulatedCount() + countContract);
        tempContractMap.get(codeContract).setAccumulatedPrice(averageClosePrice);
        tempContractMap.get(codeContract).setCloseDate(date);
        tempContractMap.get(codeContract).setAverageClosePrice(averageClosePrice);
        tempContractMap.get(codeContract).setLastClosePrice(price);
        float marga = tempContractMap.get(codeContract).getTotalNet();
        String codeSub = codeContract.substring(0,codeContract.length() - 2);
        float yields = tempContractMap.get(codeContract).getBuySell().equals("buy") ?
                ((price - tempContractMap.get(codeContract).getOpenPrice()) * countContract)
                        * rateInstrument.get(codeSub) : ((tempContractMap.get(codeContract).getOpenPrice() - price) *
                countContract) * rateInstrument.get(codeSub);
        tempContractMap.get(codeContract).setCurrentCount(tempContractMap.get(codeContract).getCurrentCount() - countContract);
        yields = yields + marga;
        tempContractMap.get(codeContract).setTotalNet(yields);
        tempContractMap.get(codeContract).setClosedFull(false);
        System.out.println("Закрыли позицию частично = " + strokesNumber + " время = " + s +
                " осталось контрактов " + tempContractMap.get(codeContract).getCurrentCount());
    }

    private static void closeMethodDDE(int countContract, String codeContract, Calendar date, float price, String s){
        float averageClosePrice = ((tempContractMap.get(codeContract).getAccumulatedCount() *
                tempContractMap.get(codeContract).getAccumulatedPrice()) +
                (countContract * price)) / (tempContractMap.get(codeContract).getAccumulatedCount() + countContract);
        tempContractMap.get(codeContract).setAverageClosePrice(averageClosePrice);
        tempContractMap.get(codeContract).setCloseDate(date);
        tempContractMap.get(codeContract).setLastClosePrice(price);
        float marga = tempContractMap.get(codeContract).getTotalNet();
        String codeSub = codeContract.substring(0,codeContract.length() - 2);
        float yields = tempContractMap.get(codeContract).getBuySell().equals("buy") ?
                ((price - tempContractMap.get(codeContract).getOpenPrice()) * countContract)
                        * rateInstrument.get(codeSub) : ((tempContractMap.get(codeContract).getOpenPrice() - price) *
                countContract) * rateInstrument.get(codeSub);
        yields = yields + marga;
        tempContractMap.get(codeContract).setTotalNet(yields);
        tempContractMap.get(codeContract).setClosedFull(true);
        tempContractMap.get(codeContract).setOpenedCheck(false);
        tempContractMap.get(codeContract).setCurrentCount(0);
        bootContractList.add(tempContractMap.get(codeContract));
        tempContractMap.remove(codeContract);
        for (Map.Entry<String, Contract> pair : tempContractMap.entrySet()) {
            System.out.println(pair.toString() + " осталось в tempContractList");
        }
        System.out.println("Закрыли позицию полностью = " + strokesNumber + " время = " + s);
    }

    private static String convertOperationDDE(String s){
        if (s.equals("Купля")){
            return "buy";
        }else if (s.equals("Продажа")){
            return "sell";
        }else {
            return "null";
        }
    }

    private static String convertOperationReportBroker(String s,String d){//5 7
        if (s.equals("")){
            return "sell";
        }else if (d.equals("")){
            return "buy";
        }else {
            return "null";
        }

    }

    private static int convertMonth(String s){
        int month;
        switch (s){
            case "Dec":month = 12;break;
            case "Nov":month = 11;break;
            case "Oct":month = 10;break;
            case "Sep":month = 9;break;
            case "Aug":month = 8;break;
            case "Jul":month = 7;break;
            case "Jun":month = 6;break;
            case "May":month = 5;break;
            case "Apr":month = 4;break;
            case "Mar":month = 3;break;
            case "Feb":month = 2;break;
            case "Jan":month = 1;break;
            default:month = 0;break;
        }
        return month;
    }

//    private static String signIdealDeal(TextField codeContractText, String codeContact){
//            if (codeContractText.getText().equals(codeContact)){
//                return "idealDeal";
//            }
//        return "hour";
//    }

    private static void openNewReport(int countContract, String codeContract, Calendar date, float price,
                                      String s, String buySell,float yields){
        Contract contract = new Contract(codeContract,date,price,date, 0, 0,
                "hour",buySell,countContract,yields,countContract,false,
                true);
        tempContractMap.put(codeContract,contract);
        System.out.println("добавили позицию  = " + strokesNumber + " Код контракта " + codeContract);
    }

    private static void addPositionReport(float price, int countContract, String s, String codeContract,float yields){
        float oldDeal = tempContractMap.get(codeContract).getOpenPrice() * tempContractMap.get(codeContract).getCurrentCount();
        float newDeal = price * countContract;
        float totalCount = tempContractMap.get(codeContract).getCurrentCount() + countContract;
        float totalPrice = (oldDeal + newDeal) / totalCount;
        float yield = tempContractMap.get(codeContract).getTotalNet() + yields;
        tempContractMap.get(codeContract).setTotalNet(yield);
        tempContractMap.get(codeContract).setOpenPrice(totalPrice);
        tempContractMap.get(codeContract).setCountContracts((int) totalCount);
        tempContractMap.get(codeContract).setCurrentCount((int) totalCount);
        System.out.println("добавили к существующей позиции = " + strokesNumber + " время = " + s);
    }

    private static void partsCloseOverReport(int countContract, String codeContract, Calendar date, float price,
                                          String s, String buySell, float yields){
        int closeCount = tempContractMap.get(codeContract).getCurrentCount();
        int openCount = countContract - closeCount;
        float yieldClose = ((closeCount + openCount) / yields) * closeCount;
        float yieldOpen = yields - yieldClose;
        closeMethodReport(closeCount,codeContract,date,price,s,yieldClose);
        openNewReport(openCount,codeContract,date,price,s,buySell,yieldOpen);
        tempContractMap.get(codeContract).setOpenedCheck(true);
    }
    //нам для закрытия не нужна средняя цена закрытия, она нужна только для учета, для фиксации фин.рез. можно каждый раз
    //делать новую цену закрытия
    private static void partsCloseReport(int countContract, String codeContract, Calendar date, float price, String s,float yields){
        float averageClosePrice = ((tempContractMap.get(codeContract).getAccumulatedCount() *
                tempContractMap.get(codeContract).getAccumulatedPrice()) +
                (countContract * price)) / (tempContractMap.get(codeContract).getAccumulatedCount() + countContract);
        tempContractMap.get(codeContract).setAccumulatedCount(tempContractMap.get(codeContract).getAccumulatedCount() + countContract);
        tempContractMap.get(codeContract).setAccumulatedPrice(averageClosePrice);
        tempContractMap.get(codeContract).setCloseDate(date);
        tempContractMap.get(codeContract).setAverageClosePrice(averageClosePrice);
        tempContractMap.get(codeContract).setLastClosePrice(price);
        float marga = tempContractMap.get(codeContract).getTotalNet();
        float yield = marga + yields;
        tempContractMap.get(codeContract).setCurrentCount(tempContractMap.get(codeContract).getCurrentCount() - countContract);
        tempContractMap.get(codeContract).setTotalNet(yield);
        tempContractMap.get(codeContract).setClosedFull(false);
        System.out.println("Закрыли позицию частично = " + strokesNumber + " время = " + s +
                " осталось контрактов " + tempContractMap.get(codeContract).getCurrentCount());
    }

    private static void closeMethodReport(int countContract, String codeContract, Calendar date, float price, String s,float yields){
        float averageClosePrice = ((tempContractMap.get(codeContract).getAccumulatedCount() *
                tempContractMap.get(codeContract).getAccumulatedPrice()) +
                (countContract * price)) / (tempContractMap.get(codeContract).getAccumulatedCount() + countContract);
        tempContractMap.get(codeContract).setAverageClosePrice(averageClosePrice);
        tempContractMap.get(codeContract).setCloseDate(date);
        tempContractMap.get(codeContract).setLastClosePrice(price);
        float marga = tempContractMap.get(codeContract).getTotalNet();
        float yield = marga + yields;
        tempContractMap.get(codeContract).setTotalNet(yield);
        tempContractMap.get(codeContract).setClosedFull(true);
        tempContractMap.get(codeContract).setOpenedCheck(false);
        tempContractMap.get(codeContract).setCurrentCount(0);
        bootContractList.add(tempContractMap.get(codeContract));
        tempContractMap.remove(codeContract);
        System.out.println("Закрыли позицию полностью = " + strokesNumber + " время = " + s);
    }

    //этот метод не добавляет идеальные сделки, он анализирует соотношение стопов к взятию прибыли
    private static void analyzeIdealDealBetweenStopFixAndFloatingStop(String pathFile, float stopMin, float standartStop) throws IOException, ParseException {
        List<IdealDeal> list = new ArrayList<>();
        BufferedReader fileReader = new BufferedReader(new FileReader(pathFile));
        while (fileReader.ready()){
            String[] line = fileReader.readLine().split("\\s");
            String date = line[0].trim();
            String time = line[1].trim();
            String buySell = line[2].trim();
            String styleDeal = line[3].trim();
            float openPrice = Float.parseFloat(line[4]);
            int maxReversePrice = Integer.parseInt(line[5]);
            int maxStopLoss = Integer.parseInt(line[6]);
            int profit = Integer.parseInt(line[8]);
            int volumeCandlePrev = Integer.parseInt(line[11]);

            String format = String.format("%s - %s:00",date,time);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss");
            list.add(new IdealDeal(simpleDateFormat.parse(format),buySell,styleDeal,openPrice,maxReversePrice,
                    maxStopLoss,profit,volumeCandlePrev));
        }

        float count = 0;
        for (IdealDeal idealDeal : list) {
            if (idealDeal.getMaxReversePrice() < stopMin){
                count++;
            }
        }
        float deal = (list.size() - count) / list.size();//%сделок кот. не прошли
        float stop = (standartStop - stopMin) / standartStop;//%уменьшения стопа
        System.out.println("Количество сделок кот. прошли = " + count + " Полный размер = " + list.size() +
                " %сделок кот. не прошли = " +
                deal + " %уменьшения стопа на = " + stop + "\n, выгода составляет = " + (stop - deal));
    }

    private static void analyzeAverageDaysBetweenIdealDeal(String pathName) throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader(pathName));
        int count = 0;//число дней
        int total = 0;//сумма всех дней
        int temp = 0;//число из предыд шага
        while (fileReader.ready()){
            String line = fileReader.readLine();
            int numberDay = Integer.parseInt(line);
            if (count == 0){
                temp = numberDay;
            }else {
                if (numberDay < temp){
                    total += (30 - temp) + numberDay;
                }else {
                    total += numberDay - temp;
                }
                temp = numberDay;
            }
            count++;
        }
        System.out.println(total/count + " среднее количество дней между идеальной сделкой");
    }

    private static Map<Integer, Float> mapMonthYields2020(){
        Map<Integer, Float> map = new HashMap<>();
        map.put(1,-11617.05f);
        map.put(2,81639.02f);
        map.put(3,154775.25f);
        map.put(4,-119724.41f);
        map.put(5,-13319.14f);
        map.put(6,-58223.23f);
        map.put(7,22569.38f);
        map.put(8,2844.92f);
        map.put(9,5149.40f);
        map.put(10,77198.89f);
        map.put(11,22432.14f);
        map.put(12,-21675.13f);
        float a = 0;
        for (Float value : map.values()) {
            a = a + value;
        }
        System.out.println("Доходность за 2020 год из отчета брокера = " + a);
        return map;
    }

    private static Map<Integer, Float> mapMonthYields2021(){
        Map<Integer, Float> map = new HashMap<>();
        map.put(1,72716.13f);
        map.put(2,3951.35f);
        map.put(3,-19786.27f);
        map.put(4,138389.79f);
        map.put(5,63534.65f);
        map.put(6,30987.00f);
        map.put(7,294414.17f);
        map.put(8,44481.87f);
        map.put(9,-427700.00f);
        map.put(10,-367177.17f);
        map.put(11,935395.38f);
        map.put(12,0f);
        float a = 0;
        for (Float value : map.values()) {
            a = a + value;
        }
        System.out.println("Доходность за 2021 год из отчета брокера = " + a);
        return map;
    }

    private static String decrementHour3(String s){
        String[] str = s.split(":");
        int hour = Integer.parseInt(str[0]);
        hour -= 3;
        return String.format("%d:%s:%s",hour,str[1],str[2]);
    }

    private static void yieldsForPeriodOPENDATE(int yearIn) throws IOException, ParseException {
        readInFileData(bootContractList,tempContractMap,pathFileBoot);
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

    private static void yieldsForPeriodOPENDATE(int yearIn, int monthIn) throws IOException, ParseException {
        readInFileData(bootContractList,tempContractMap,pathFileBoot);
        float yields = 0;
        for (Contract contract : bootContractList) {//Mon Jan 16 14:54:14 MSK 2021
            String line = contract.getOpenDate().getTime().toString();
            String[] parseStr = line.split("\\s");
            int month = convertMonth(parseStr[1]);
            int yearLong = Integer.parseInt(parseStr[5]) + 2000;
            int yearShort = Integer.parseInt(parseStr[5]);
            if (yearShort == yearIn && month == monthIn || yearLong == yearIn && month == monthIn){
                yields = yields + contract.getTotalNet();
            }
        }
        System.out.printf("Yields for period %d year and %d month = %.2f RUB\n",yearIn,monthIn,yields);
    }

    private static void yieldsForPeriodOPENDATE(String codeContract,int yearIn, int monthIn) throws IOException, ParseException {
        readInFileData(bootContractList,tempContractMap,pathFileBoot);
        float yields = 0;
        for (Contract contract : bootContractList) {//Mon Jan 16 14:54:14 MSK 2021
            String code = contract.getCodeContract().substring(0,contract.getCodeContract().length() - 2).toUpperCase();
            String line = contract.getOpenDate().getTime().toString();
            String[] parseStr = line.split("\\s");
            int month = convertMonth(parseStr[1]);
            int yearLong = Integer.parseInt(parseStr[5]) + 2000;
            int yearShort = Integer.parseInt(parseStr[5]);
            if (yearShort == yearIn && month == monthIn && code.equals(codeContract.toUpperCase())
                    || yearLong == yearIn && month == monthIn && code.equals(codeContract.toUpperCase())){
                yields = yields + contract.getTotalNet();
            }
        }
        System.out.printf("Yields for period %d year and %d month.Contract Code %s = %.2f RUB\n",
                yearIn,monthIn,codeContract,yields);
    }

    private static void yieldsForPeriodOPENDATE(int yearIn, String... codeContract) throws IOException, ParseException {
        for (String s : codeContract) {
            readInFileData(bootContractList,tempContractMap,pathFileBoot);
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
            System.out.printf("Yields for period %d year.Contract Code %s = %.2f RUB\n",
                    yearIn,s,yields);
        }
    }

    private static void yieldsIdealDeal() throws IOException, ParseException {
        readInFileData(bootContractList,tempContractMap,pathFileBoot);
        List<Integer> list = new ArrayList<>();
        float yields = 0;
        int countFail = 0;
        for (Contract contract : bootContractList) {//Mon Jan 16 14:54:14 MSK 21
            if (contract.getStyleDeal().equals("idealDeal")){
                if (contract.getTotalNet() < 0){
                    countFail++;
                }else {
                    list.add(countFail);
                    countFail = 0;
                }
                yields = yields + contract.getTotalNet();
            }
        }
        list.add(countFail);
        int maxFails = list.stream().max(Comparator.comparing(Integer::intValue)).get();
        int minFails = list.stream().min(Comparator.comparing(Integer::intValue)).get();
        System.out.println(yields + " yieldsIdealDeal");
        System.out.println("Максимальное количество неудачных попыток входов в идельную сделку = " + maxFails);
        System.out.println("Минимальное количество неудачных попыток входов в идельную сделку = " + minFails);
    }

    //===здесь описаны временные промежутки. Если контракт совпадает и попадает во временной промежуток карт ниже,
    //то сделка помечается как проведенная по Стратегии(Идеальная сделка)
    private static Map<String,String> dataContractsIdealDeal(String codeContractShort){
        Map<String,String> map = new HashMap<>();
        if (codeContractShort.startsWith("RI")){
            for (int i = 2; i < 9; i++) {
                map.put("RIH" + i,"Dec 15 11:59:01 MSK 202" + i + " - Mar 17 11:59:00 MSK 202" + i);
                map.put("RIM" + i,"Mar 17 11:59:01 MSK 202" + i + " - Jun 16 11:59:00 MSK 202" + i);
                map.put("RIU" + i,"Jun 16 11:59:01 MSK 202" + i + " - Sep 15 11:59:00 MSK 202" + i);
                map.put("RIZ" + i,"Sep 15 11:59:01 MSK 202" + i + " - Dec 15 11:59:00 MSK 202" + i);
            }
        }else if (codeContractShort.startsWith("SI")){
            for (int i = 2; i < 9; i++) {
                map.put("SIH" + i,"Dec 15 11:59:01 MSK 202" + i + " - Mar 17 11:59:00 MSK 202" + i);
                map.put("SIM" + i,"Mar 17 11:59:01 MSK 202" + i + " - Jun 16 11:59:00 MSK 202" + i);
                map.put("SIU" + i,"Jun 16 11:59:01 MSK 202" + i + " - Sep 15 11:59:00 MSK 202" + i);
                map.put("SIZ" + i,"Sep 15 11:59:01 MSK 202" + i + " - Dec 15 11:59:00 MSK 202" + i);
            }
        }

        return map;
    }

    private static void loadDealsFromDDEServer(String timeAfter,String codeContractForIdealDeal,boolean isZeroYield)
            throws IOException, ParseException {
        isZeroYield = false;
        readInFileData(bootContractList,tempContractMap,pathFileBoot);
        addContractDDEServer(pathFileDDEServer);
        writeOutFileData(bootContractList,tempContractMap,pathFileBoot,dataContractsIdealDeal(codeContractForIdealDeal),
                timeAfter,isZeroYield);
    }
}
