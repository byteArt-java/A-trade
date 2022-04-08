package HandlerDDEServerQuik;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourcesStatic {
    //=========Путь файла, где находятся закрытые сделки за все время
    protected static final String pathFileBoot = "F://Фондовый рынок/A-Trade/BootFromJavaDeals.txt";

    //=========Путь файла, данные из QUIK DDEServer
    protected static final String pathFileDDEServer = "F://Фондовый рынок/A-Trade/pathFileDDEServer.txt";

    //здесь хранятся все загруж закрытые сделки, при запуске программы. Считанной из pathFileBoot
    protected static List<Contract> bootContractList = new ArrayList<>();

    //здесь хранятся незакрытые времен сделки, при запуске программы.
    protected static Map<String, Contract> tempContractMap = new HashMap<>();

    //Карта хранения коэфф. на какой коэф умножать
    protected static Map<String,Float> rateInstrument = new HashMap<>();

    //путь к файлу в кот. находится данные и личного журнала сделок, кот. выгружены с личного кабинета сделок
    protected static final String pathFileReportBroker = "F://Фондовый рынок//A-Trade//" +
            "Все сделки из отчета Брокера по 28Декабря 2021года.txt";

    //путь к файлу в кот. находится данные и личного журнала сделок
    protected static final String pathFileIdealDeal = "F://Фондовый рынок//A-Trade//3.txt";

    //счетчик считывание строки метода
    protected static int strokesNumber = 0;
}
