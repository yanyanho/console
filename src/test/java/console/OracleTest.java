package console;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OracleTest {

    @Test
    public void stringTest(){
        String argValue = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        int left = argValue.indexOf("(");
        int right = argValue.indexOf(")");
        String header = argValue.substring(0,left);
        String url = argValue.substring(left+1,right);
        System.out.println(header);
        System.out.println(url);

        String argValue1 = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";
        if (StringUtils.isBlank(argValue1) || argValue1.endsWith(")")) {
            System.out.println("*******");

        }
        String resultIndex = argValue.substring(argValue.indexOf(").") + 2);

        String[] resultIndexArr = resultIndex.split("\\.");
        List resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        System.out.println( resultList);
       // List<Object> httpResultIndexList = subFiledValueForHttpResultIndex(argValue);


    }
//
//    @Test
//    public void htmlParseTest() throws IOException {
//        Document document = Jsoup.connect("http://blog.beifengtz.com/").get();
//        System.out.println(document);
//    }
}
