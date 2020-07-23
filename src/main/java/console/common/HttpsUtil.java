package console.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static console.common.JsonUtils.toJSONString;

public class HttpsUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);

    private static final String UTF_8 = "UTF-8";
    // 连接超时时间
    private static final int CONNECTION_TIMEOUT = 60000;//3秒
    // 读数据超时时间
    private static final int READ_DATA_TIMEOUT = 60000;//6秒

    private static PoolingHttpClientConnectionManager connManager = null;
    private static CloseableHttpClient httpclient = null;

    private static final NoopHostnameVerifier NO_OP = new NoopHostnameVerifier();

    static {
        connManager = new PoolingHttpClientConnectionManager();
        httpclient = HttpClients.custom().setConnectionManager(connManager).build();
    }

    /**
     * sslClient
     *
     * @return
     */
    public static CloseableHttpClient createSSLClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NO_OP);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Post请求（默认超时时间）
     *
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, Map<String, Object> data) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return post(url, CONNECTION_TIMEOUT, READ_DATA_TIMEOUT, data, UTF_8);
    }
    public static String post(String url, String data) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return post(url, READ_DATA_TIMEOUT, READ_DATA_TIMEOUT, data, UTF_8);
    }

    public static String post(String url, Map<String, Object> data, String encoding) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return post(url, CONNECTION_TIMEOUT, READ_DATA_TIMEOUT, data, encoding);
    }

    public static String post(String url, int timeout, Map<String, Object> data, String encoding) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return post(url, timeout, timeout, data, encoding);
    }

    public static String post(String url, int connectTimeout, int readTimeout, Object data, String encoding) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpPost post = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(readTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout)
                    .setExpectContinueEnabled(false)
                    .build();
            post.setConfig(requestConfig);
            if( data instanceof String){
                String stringData = (String)data;
                if(StringUtils.isNotBlank(stringData)) {
                    HttpEntity httpEntity = new StringEntity(stringData,encoding);
                    post.setEntity(httpEntity);
                }
            }else if( data instanceof Map){
                Map<Object,Object> mapData = (Map<Object,Object>) data;
                if (null != mapData && !mapData.isEmpty()) {
                    List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                    for (Object key : mapData.keySet()) {
                        formparams.add(new BasicNameValuePair(key.toString(), mapData.get(key).toString()));
                    }
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, encoding);
                    post.setEntity(formEntity);
                }
            }else{
                throw new RuntimeException("Unsupported data type by https post util.");
            }

            if (url.startsWith("https")) {//https
                response = createSSLClient().execute(post);
            } else {
                response = httpclient.execute(post);
            }

            entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, encoding);
            }
        } finally {
            if (entity != null) {
                entity.getContent().close();
            }
            if (response != null) {
                response.close();
            }
            logger.info("send POST request : url:[{}],params:[{}], result :[{}]",
                    url,
                    data instanceof String ? data : toJSONString(data),
                    result);
        }
        return result;
    }


    /**
     * 如果失败尝试3次
     *
     * @param url
     * @param encoding
     * @return
     */
    public static Object tryGet(String url, String encoding) {
        Object resultStr = "";
        for (int i = 0; i < 3; i++) {
            try {
                resultStr = get(url, encoding);
                break;
            } catch (Exception e) {
                logger.error("请求异常count:{} ", i, e);
            }
        }
        return resultStr;
    }

    /**
     * Post请求（默认超时时间）
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String get(String url) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return get(url, null, CONNECTION_TIMEOUT, READ_DATA_TIMEOUT, UTF_8);
    }

    public static String get(String url, String encoding) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return get(url, null, CONNECTION_TIMEOUT, READ_DATA_TIMEOUT, encoding);
    }

    public static String get(String url, Map<String, String> cookies, String encoding) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return get(url, cookies, CONNECTION_TIMEOUT, READ_DATA_TIMEOUT, encoding);
    }

    public static String get(String url, Map<String, String> cookies, int timeout, String encoding) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return get(url, cookies, timeout, timeout, encoding);
    }

    public static String get(String url, Map<String, String> cookies, int connectTimeout, int readTimeout, String encoding) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpEntity entity = null;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            HttpGet get = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(readTimeout)
                    .setConnectTimeout(connectTimeout)
                    .setConnectionRequestTimeout(connectTimeout)
                    .setExpectContinueEnabled(false).build();
            get.setConfig(requestConfig);
            if (cookies != null && !cookies.isEmpty()) {
                StringBuilder buffer = new StringBuilder(128);
                for (String cookieKey : cookies.keySet()) {
                    buffer.append(cookieKey).append("=").append(cookies.get(cookieKey)).append("; ");
                }
                // 设置cookie内容
                get.setHeader(new BasicHeader("Cookie", buffer.toString()));
            }
            if (url.startsWith("https")) {//https
                response = createSSLClient().execute(get);
            } else {
                response = httpclient.execute(get);
            }
            entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, encoding);
            }
        } finally {
            if (entity != null) {
                entity.getContent().close();
            }
            if (response != null) {
                response.close();
            }
            logger.info("send GET request : url:[{}],result :[{}]",
                    url,
                    result);
        }
        return result;
    }

    /**
     * sslClient
     *
     * @return
     */
    public static String postBody(String url, String body, String encoding, int connectTimeout, int readTimeout) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(readTimeout)
                .setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectTimeout)
                .setExpectContinueEnabled(false).build();
        post.setConfig(requestConfig);

        if (StringUtils.isNotBlank(body)) {
            StringEntity formEntity = new StringEntity(body, encoding);
            post.setEntity(formEntity);
        }

        CloseableHttpResponse response = null;
        if (url.startsWith("https") || url.startsWith("HTTPS")) {//https
            response = createSSLClient().execute(post);
        } else {
            response = httpclient.execute(post);
        }

        HttpEntity entity = response.getEntity();
        String result = null;
        try {
            if (entity != null) {
                result = EntityUtils.toString(entity, encoding);
            }
        } finally {
            if (entity != null) {
                entity.getContent().close();
            }
            if (response != null) {
                response.close();
            }
            logger.info("send POST request with Body: url:[{}],body:[{}], result :[{}]",
                    url,
                    body,
                    result);
        }
        return result;
    }

    /**
     * map转成queryStr
     *
     * @param paramMap
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String mapToQueryStr(Map<String, String> paramMap) {
        StringBuffer strBuff = new StringBuffer();
        for (String key : paramMap.keySet()) {
            strBuff.append(key).append("=").append(paramMap.get(key)).append("&");
        }
        return strBuff.substring(0, strBuff.length() - 1);
    }

    public static void main(String[] args) {
        try {
            System.out.println(HttpsUtil.get("https://api.kraken.com/0/public/Ticker?pair=ETHXBT"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

}