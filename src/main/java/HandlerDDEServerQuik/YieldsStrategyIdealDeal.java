package HandlerDDEServerQuik;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class YieldsStrategyIdealDeal extends ResourcesStatic{
    static void yieldsIdealDeal() throws IOException, ParseException {
        ReadInFileData.readInFileData(bootContractList,tempContractMap,pathFileBoot);
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
}
