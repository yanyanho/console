package console.oracle;

import console.ConsoleInitializer;
import console.common.DecodeOutputUtils;
import console.oracle.contract.OracleCore;
import console.oracle.contract.TemplateOracle;
import console.oracle.event.callback.ContractEventCallback;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.event.filter.EventLogUserParams;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
    import org.fisco.bcos.web3j.protocol.channel.ChannelEthereumService;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.fisco.bcos.web3j.utils.Numeric;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static console.common.JsonUtils.toJSONString;
import static console.oracle.contract.OracleCore.LOG1_EVENT;

@Slf4j
public class OracleService {

    private Web3j web3j;
    private Credentials credentials;
    private static BigInteger gasPrice = new BigInteger("1");
    private static BigInteger gasLimit = new BigInteger("2100000000");
    private static ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);

    private static RestTemplate restTemplate = new RestTemplate();


    public static String ORACLE_CORE_ADDRESS;

    public OracleService(Web3j web3j, Credentials credentials) {
        this.web3j = web3j;
        this.credentials = credentials;
    }

    public void oracleCoreDeploy() throws Exception {
        OracleCore oracleCore = OracleCore.deploy(web3j, credentials,contractGasProvider).send();
        ORACLE_CORE_ADDRESS = oracleCore.getContractAddress();
        if (ORACLE_CORE_ADDRESS == null) {
            System.out.println("oracle deploy failed!");;
        }
        System.out.println("oracle core start successfully!");
        System.out.println("oracle core contract address is:" + ORACLE_CORE_ADDRESS);
        System.out.println("oracle service user address  is:" + credentials.getAddress());
        registerContractEvent();
        System.out.println("oracle core event listening register successfully!");
        System.out.println("now ,please deploy your own oracle contract!");
    }

    public void registerContractEvent() {
        // 传入abi作decoder:
        TransactionDecoder decoder = new TransactionDecoder(OracleCore.ABI);
        // init EventLogUserParams for register
        EventLogUserParams params = initSingleEventLogUserParams();
        ContractEventCallback callBack = new ContractEventCallback(this, decoder);
        org.fisco.bcos.channel.client.Service service = ConsoleInitializer.context.getBean(Service.class);
        service.registerEventLogFilter(params, callBack);
        ((AbstractRefreshableApplicationContext) ConsoleInitializer.context).refresh();
    }


    public void getDataFromUrlAndUpChain( String contractAddress, byte[] logId, String url, String formate, List<String> httpResultIndexList) throws Exception {
        System.out.println("oracle service begins get off-chain result: wating....... " );
       //System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
       // restTemplate.getMessageConverters().add(new PlainMappingJackson2HttpMessageConverter());
     //   HttpService  httpService = new HttpService(restTemplate);
        System.out.println("the http url is: "+ url);
        Object httpResult = HttpService.getObjectByUrlAndKeys(url,formate,  httpResultIndexList);

        System.out.println(" oracle service gets off-chain result: " + httpResult);
        //send transaction
        upBlockChain(contractAddress, logId, toJSONString(httpResult));
    }

    /**
     * 将数据上链.
     */
    private void upBlockChain(String contractAddress, byte[] cid, String data) throws Exception {
        String cidStr = Numeric.toHexString(cid);
        log.info("oracle service start to upload result blockChain . contractAddress:{} data:{} cid:{}", contractAddress, data, cidStr);
        System.out.println("oracle service start to upload result blockChain!");

        org.fisco.bcos.channel.client.Service service = ConsoleInitializer.context.getBean(Service.class);
        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        service.run();
        web3j = Web3j.build(channelEthereumService, service.getGroupId());
        // no need
      //  ((AbstractRefreshableApplicationContext) ConsoleInitializer.context).refresh();

        try {
            TemplateOracle templateOracle = TemplateOracle.load(contractAddress, web3j, credentials, contractGasProvider);
            TransactionReceipt receipt = templateOracle.__callback(cid, data).send();
            dealWithReceipt(receipt);
            log.info("upBlockChain success. contractAddress:{} data:{} cid:{}", contractAddress, data, cidStr);
        } catch (Exception e) {
            System.out.println("upload data to BlockChain failed! " + e.getMessage());
        }
    }


//    private RestTemplate setRestTemplate() {
//        CloseableHttpClient httpClient = HttpClients.custom()
//                .setSSLHostnameVerifier(new NoopHostnameVerifier())
//                .build();
//        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//        requestFactory.setHttpClient(httpClient);
//    }
    public static void dealWithReceipt(TransactionReceipt receipt) {
        // log.info("*********"+ transactionReceipt.getOutput());
        if ("0x16".equals(receipt.getStatus()) && receipt.getOutput().startsWith("0x08c379a0")) {
            log.error("transaction error", DecodeOutputUtils.decodeOutputReturnString0x16(receipt.getOutput()));
            System.out.println(DecodeOutputUtils.decodeOutputReturnString0x16(receipt.getOutput()));
        }
        if (!"0x0".equals(receipt.getStatus())) {
            log.error("transaction error, status:{} output:{}", receipt.getStatus(), receipt.getOutput());
            System.out.println(DecodeOutputUtils.decodeOutputReturnString0x16(receipt.getOutput()));
        }
    }

    private EventLogUserParams initSingleEventLogUserParams() {
        EventLogUserParams params = new EventLogUserParams();
        params.setFromBlock("latest");
        params.setToBlock("latest");

        // addresses，设置为Java合约对象的地址
        List<String> addresses = new ArrayList<>();
        addresses.add(OracleService.ORACLE_CORE_ADDRESS);
        params.setAddresses(addresses);
        List<Object> topics = new ArrayList<>();
        topics.add(EventEncoder.encode(LOG1_EVENT));
        params.setTopics(topics);

        return params;
    }
}
