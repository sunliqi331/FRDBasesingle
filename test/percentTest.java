import java.math.BigDecimal;
import java.text.DecimalFormat;

public class percentTest {

    public static void main(String[] args) {
//        System.out.println("1233分123秒".lastIndexOf("分"));
//        System.out.println("1233分123秒".substring(0, "1233分123秒".lastIndexOf("分")));
        
        int a = 2429;
        int b = 7238;

        int f1 = new BigDecimal((float)a/b).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100L)).intValue();
        System.out.println(f1);
    }
}
