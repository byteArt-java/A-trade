package HandlerDDEServerQuik;

import java.util.HashMap;
import java.util.Map;

public class TimeRangeForIdealDeal {
    //===здесь описаны временные промежутки. ≈сли контракт совпадает и попадает во временной промежуток карт ниже,
    //то сделка помечаетс€ как проведенна€ по —тратегии(»деальна€ сделка)
    static Map<String,String> dataContractsIdealDeal(String codeContractShort){
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
}
