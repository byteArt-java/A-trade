import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        String newLineForRateStart = htmlTables.replace("Стоимость шага цены","sK$");
        String newLineForRateEnd = newLineForRateStart.replace("Нижний лимит","eK$");

        int start = 0;
        int end = 0;
        String numberRate = "";
        for (int i = 0; i < newLineForRateEnd.length() - 3; i++) {
            if (newLineForRateEnd.charAt(i) == 's' && newLineForRateEnd.charAt(i + 1) == 'K' &&
                    newLineForRateEnd.charAt(i + 2) == '$'){
                start = i;
            }else if (newLineForRateEnd.charAt(i) == 'e' && newLineForRateEnd.charAt(i + 1) == 'K' &&
                    newLineForRateEnd.charAt(i + 2) == '$'){
                end = i;
            }
        }
        numberRate = newLineForRateEnd.substring(start,end);//строка между sK$ и eK$

        for (int j = 0; j < numberRate.length(); j++) {
            if (Character.isDigit(numberRate.charAt(j))){
                sb.append(numberRate.charAt(j));
            }else if (numberRate.charAt(j) == ','){
                sb.append(".");
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
