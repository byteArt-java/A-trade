package HandlerDDEServerQuik;

public class ConvertMonthToInt {
    public static int convertMonth(String s){
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
}
