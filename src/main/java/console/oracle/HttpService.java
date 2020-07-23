package console.oracle;


import console.common.HttpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import static console.common.JsonUtils.toJSONString;

/**
 * service for http request.
 */
@Slf4j
public class HttpService {

    private RestTemplate restTemplate;

    public HttpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * get result by url,and get value from the result by keyList.
     *
     * @param url
     * @param resultKeyList
     * @return
     */
    public Object getObjectByUrlAndKeys(String url, List<Object> resultKeyList) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException, IOException {
        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//            headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//            HttpEntity<String> entity = new HttpEntity<String>("parameters",headers);
 //           Object result = restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
           Object result = restTemplate.getForObject(url, Object.class);
            Object result1 =     HttpsUtil.get(url);
           // return getValueByKeys(result, resultKeyList);
            return null;
        } catch (Exception ex) {
            log.error("getObjectByUrlAndKeys error, url:{} resultKeyList:{}", url, toJSONString(resultKeyList), ex);
            System.out.println("get off-chain result failed, please check your url or try later!");
            throw ex;
        }
    }


    /**
     * get value from object by keyList.
     *
     * @param object
     * @param keyList
     * @return
     */
    private Object getValueByKeys(String object, List<Object> keyList) {
        if (object == null || keyList == null || keyList.size() == 0) return object;

        Object finalResult = object;
        for (Object key : keyList) {
            finalResult = getValueByKey((String) finalResult, key);
        }
        return finalResult;
    }


    /**
     * get value by key.
     *
     * @param jsonString
     * @param key
     * @return
     */
    private Object getValueByKey(String jsonString, Object key) {
//        if (obj instanceof List) {
//            JSONArray jsonArray = JSON.parseArray(jsonString);
//            return jsonArray.get(Integer.valueOf(String.valueOf(key)));
//        }
//        try {
//            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(obj));
//            return jsonObject.get(key);
//        } catch (Exception ex) {
//            log.warn("parse {} to object fail", JSON.toJSONString(obj));
//            return obj;
//        }
        return null;
    }

}
