package TxtHandler;

import java.util.HashMap;
import java.util.Map;

//Этот класс отвечает за правильный расчет коэффициентов которые умножаются на полученную прибыль
public class RateInstrumentHandler {

    static Map<String,Float> addRateInstrument(String codeContract){
        Map<String,Float> map = new HashMap<>();

        if (codeContract.toUpperCase().equals("RI")){
            float riRate = QueryDataCoefficient.getCoefficient("RI") * 0.1f;
            map.put("RI",riRate);
            System.out.println("addRateInstrument() RI = " + riRate);
        }else if (codeContract.toUpperCase().equals("ED")){
            float ED_GD_BR = QueryDataCoefficient.getCoefficient("ED");//единый коэф для 3 разных контрактов
            map.put("BR",ED_GD_BR * 100);
            map.put("GD",ED_GD_BR * 10);
            map.put("ED",ED_GD_BR * 10000);
            System.out.println("addRateInstrument() ED = " + ED_GD_BR);
        }else if (codeContract.toUpperCase().equals("SF")){
            float sfRate = QueryDataCoefficient.getCoefficient("SF");
            map.put("SF",sfRate * 100);
            System.out.println("addRateInstrument() SF = " + sfRate);
        }else {
            map.put("SI",1f);
            map.put("AF",1f);
            map.put("VB",1f);
            map.put("SR",1f);
            map.put("GZ",1f);
            map.put("MN",1f);
            map.put("LK",1f);
            map.put("YN",1f);
            map.put("ML",1f);
            map.put("NG",1f);
            map.put("MX",1f);
            map.put("MM",1f);
            map.put("HO",1f);
        }
        return map;
    }
}
