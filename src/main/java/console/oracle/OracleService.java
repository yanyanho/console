package console.oracle;

import com.alibaba.fastjson.JSON;
import console.ConsoleInitializer;
import console.common.DecodeOutputUtils;
import console.oracle.contract.OracleCore;
import console.oracle.contract.TemplateOracle;
import console.oracle.event.callback.ContractEventCallback;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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

        System.out.println("oracle core start successfully, address is: " + ORACLE_CORE_ADDRESS);
        System.out.println("please deploy your own oracle contract!");
    }

    public void registerContractEvent(ContractEventCallback callBack) {
        // 传入abi作decoder:

        // init EventLogUserParams for register
        EventLogUserParams params = initSingleEventLogUserParams();

        org.fisco.bcos.channel.client.Service service = ConsoleInitializer.context.getBean(Service.class);
        service.registerEventLogFilter(params, callBack);
        ((AbstractRefreshableApplicationContext) ConsoleInitializer.context).refresh();
        System.out.println("oracle event register successfully!");
    }


    public void getDataFromUrlAndUpChain( String contractAddress, byte[] logId, String url, List<Object> httpResultIndexList) throws Exception {
        System.out.println("begin get off-chain result: wating....... " );
       restTemplate.getMessageConverters().add(new PlainMappingJackson2HttpMessageConverter());
       HttpService  httpService = new HttpService(restTemplate);
        Object httpResult = httpService.getObjectByUrlAndKeys(url, httpResultIndexList);
        System.out.println("get off-chain result: " + httpResult);
        //send transaction
        upBlockChain(contractAddress, logId, JSON.toJSONString(httpResult));
    }

    /**
     * 将数据上链.
     */
    private void upBlockChain(String contractAddress, byte[] cid, String data) throws Exception {
        String cidStr = Numeric.toHexString(cid);
        log.info("upBlockChain start. contractAddress:{} data:{} cid:{}", contractAddress, data, cidStr);
        System.out.println("upBlockChain start");

        org.fisco.bcos.channel.client.Service service = ConsoleInitializer.context.getBean(Service.class);
        ChannelEthereumService channelEthereumService = new ChannelEthereumService();
        channelEthereumService.setChannelService(service);
        service.run();
        web3j = Web3j.build(channelEthereumService, service.getGroupId());
        // no need
      //  ((AbstractRefreshableApplicationContext) ConsoleInitializer.context).refresh();

        try {
            System.out.println("query version begin!");
            log.info("version: " + web3j.getNodeVersion().send());
            System.out.println("version: " + web3j.getNodeVersion().send());
            TemplateOracle templateOracle = TemplateOracle.load(contractAddress, web3j, credentials, contractGasProvider);
            TransactionReceipt receipt = templateOracle.__callback(cid, data).send();
            log.info("&&&&&" + receipt.getStatus());
            dealWithReceipt(receipt);
            log.info("upBlockChain success. contractAddress:{} data:{} cid:{}", contractAddress, data, cidStr);
        } catch (Exception e) {
            System.out.println("upload data to BlockChain failed! " + e.getMessage());
        }
    }

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
