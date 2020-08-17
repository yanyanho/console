package console.oracle;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import console.common.HttpsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static console.common.JsonUtils.*;

/**
 * service for http request.
 */
public class HttpService {
    private static final Logger log = LoggerFactory.getLogger(HttpService.class);

    public static Object getObjectByUrlAndKeys(String url,String formate,  List<String> resultKeyList) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
 //           Object result = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
       //    Object result = restTemplate.getForObject(url, Object.class);
            String result =     HttpsUtil.get(url);
            log.info("http result: {}",result );
            if(formate.equals("json")) {
                JsonNode jsonNode = stringToJsonNode(result);
                return getValueByKeys(jsonNode, resultKeyList);
            }
            //if(formate.equals("plain"))
            else  {
                return result.split("\n")[0];
            }
        } catch (Exception ex) {
            log.error("getObjectByUrlAndKeys error, url:{} resultKeyList:{}", url, toJSONString(resultKeyList), ex);
            System.out.println("get off-chain result failed, please check your url or try later!");
            throw ex;
        }
    }


    /**
     * get value from object by keyList.
     *
     * @param jsonNode
     * @param keyList
     * @return
     */
    private static  Object getValueByKeys(JsonNode jsonNode, List<String> keyList) {
        if (jsonNode == null || keyList == null || keyList.size() == 0) return jsonNode;
        Object finalResult = jsonNode;
        for (String key : keyList) {
            finalResult = getValueByKey(finalResult, key);
        }
        return finalResult;
    }


    /**
     * get value by key.
     *
     * @param jsonNode
     * @param key
     * @return
     */
    private static Object getValueByKey(Object jsonNode, String key) {
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
