package console;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import console.common.HttpsUtil;
import org.apache.commons.lang3.StringUtils;

import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static console.common.JsonUtils.stringToJsonNode;
import static console.common.JsonUtils.toList;

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

    @Test
    public void httpsTest() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        String argValue = "json(https://api.kraken.com/0/public/Ticker?pair=ETHXBT).result.XETHXXBT.c.0";
       // String argValue = "json(https://api.apiopen.top/EmailSearch?number=1012002).result";

     //   String argValue = "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)";

        int left = argValue.indexOf("(");
        int right = argValue.indexOf(")");
        String header = argValue.substring(0, left);
        String url = argValue.substring(left + 1, right);
        List<String> httpResultIndexList = subFiledValueForHttpResultIndex(argValue);
        System.out.println("********* begin" );
        String result = HttpsUtil.get(url);
        System.out.println("*********" + result);
       Object o =  getValueByKeys(result, httpResultIndexList);
        System.out.println(o);
    }


    private List<String> subFiledValueForHttpResultIndex(String argValue) {
        if (StringUtils.isBlank(argValue) || argValue.endsWith(")")) {
            return Collections.EMPTY_LIST;
        }

        String resultIndex = argValue.substring(argValue.indexOf(").") + 2);

        String[] resultIndexArr = resultIndex.split("\\.");
        List resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        return resultList;
    }

    private Object getValueByKeys(String resultString, List<String> keyList) {
        if (resultString == null || keyList == null || keyList.size() == 0) return resultString;

        Object finalResult = stringToJsonNode(resultString);
        for (String key : keyList) {
            finalResult = getValueByKey(finalResult, key);
        }
        return finalResult;
    }

    private Object getValueByKey(Object jsonNode, String key) {
        if (jsonNode instanceof ArrayNode) {
            List<Object> jsonArray = toList(jsonNode);
            return jsonArray.get(Integer.valueOf(String.valueOf(key)));
        }
        try {
          JsonNode jsonNode1 = (JsonNode)jsonNode;
            return jsonNode1.get(key);
        } catch (Exception ex) {
            return jsonNode;
        }
    }

}
