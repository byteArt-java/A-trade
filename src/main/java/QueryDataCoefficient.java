package main.java;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryDataCoefficient {
    public static Map<String,String> link = new HashMap<>();

    public static float getCoefficient(String codeContractShort){
        addLink();

        Document doc = null;
        try {
            doc = Jsoup.connect(link.get(codeContractShort.toUpperCase())).userAgent("Mozilla").get();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("getCoefficient МЕТОД, Проблемы с соединением");
        }
        Element div = doc.getElementById("contractTables");
        String htmlTables = div.html();

        //=====находим строку Стоимость шага цены, меняем ее на sK$,чтобы было проще разбирать.И находим ставку по инстр
        StringBuilder sb = new StringBuilder();
        String newLineForRate = htmlTables.replace("Стоимость шага цены","sK$");
        for (int i = 0; i < newLineForRate.length() - 3; i++) {
            if (newLineForRate.charAt(i) == 's' && newLineForRate.charAt(i + 1) == 'K' &&
                    newLineForRate.charAt(i + 2) == '$'){
                String numberRate = newLineForRate.substring(i,i + 70);
                for (int j = 0; j < numberRate.length(); j++) {
                    if (Character.isDigit(numberRate.charAt(j))){
                        sb.append(numberRate.charAt(j));
                    }else if (numberRate.charAt(j) == ','){
                        sb.append(".");
                    }
                }
                break;
            }
        }
        //==============================================================================================================
        return Float.parseFloat(sb.toString());
    }

    private static void addLink(){
        Date currentDate = new Date();
        String date = currentDate.toString();
        String shortYear = date.substring(26,28);

        link.put("RI","https://www.moex.com/ru/contract.aspx?code=RTS-6." + shortYear);

        link.put("SF","https://www.moex.com/ru/contract.aspx?code=SPYF-6." + shortYear);

        link.put("ED","https://www.moex.com/ru/contract.aspx?code=ED-6." + shortYear);
    }

}
