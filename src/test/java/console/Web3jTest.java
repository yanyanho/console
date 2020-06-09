package console;

import static console.oracle.OracleService.dealWithReceipt;
import static console.oracle.contract.OracleCore.LOG1_EVENT;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import console.oracle.OracleService;
import console.oracle.contract.OracleCore;
import console.oracle.contract.TemplateOracle;
import console.oracle.event.callback.ContractEventCallback;
import org.fisco.bcos.channel.client.Service;
import org.fisco.bcos.channel.event.filter.EventLogUserParams;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.gas.StaticGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.springframework.context.support.AbstractRefreshableApplicationContext;

public class Web3jTest extends TestBase {

    @Rule public final SystemOutRule log = new SystemOutRule().enableLog();
    private static BigInteger gasPrice = new BigInteger("1");
    private static BigInteger gasLimit = new BigInteger("2100000000");
    private static ContractGasProvider contractGasProvider = new StaticGasProvider(gasPrice, gasLimit);
    private Credentials credentials = ConsoleInitializer.credentials;
    private Web3j web3j = web3jFace.getWeb3j();

    private String  orcleAddress;
    @Test
    public void getBlockNumberTest() throws IOException {
        String[] params1 = {};
        web3jFace.getBlockNumber(params1);
        assertTrue(!"".equals(log.getLog()));
        log.clearLog();

        String[] params2 = {"-h"};
        web3jFace.getBlockNumber(params2);
        assertTrue(!"".equals(log.getLog()));
        log.clearLog();

        String[] params3 = {"--help"};
        web3jFace.getBlockNumber(params3);
        assertTrue(!"".equals(log.getLog()));
        log.clearLog();

        String[] params4 = {"k"};
        web3jFace.getBlockNumber(params4);

        assertTrue(!"".equals(log.getLog()));
    }

    @Test
    public void oracleTemplateTest() throws Exception {

        //fist  secretRegistty
        OracleCore oraliceCore = OracleCore.deploy(web3j, credentials, contractGasProvider).send();
         orcleAddress = oraliceCore.getContractAddress();
        // asset
        PriceOracle temperatureOracle = PriceOracle.deploy(web3j, credentials, contractGasProvider).send();

        TransactionReceipt t=  temperatureOracle.oracle_setNetwork(orcleAddress).send();

        System.out.println(t.getStatus());
      //  TransactionReceipt t1 = temperatureOracle.update().send();
        // t1.getLogs().

        // temperatureOracle.__callback()
        TemplateOracle templateOracle = TemplateOracle.load(temperatureOracle.getContractAddress(),web3j, credentials,contractGasProvider);

       // byte[] bytes1=  temperatureOracle.id().send();

       // TransactionReceipt  transactionReceipt = templateOracle.__callback("0x119a31bf842976a1ab23a8484f7c91".getBytes(), "21").send();

       //+ System.out.println(bytesToHex(bytes1));
        System.out.println(temperatureOracle.price().send());
      //  System.out.println(transactionReceipt.getStatus());

       // System.out.println(transactionReceipt.getOutput());
      //  dealWithReceipt(transactionReceipt);



        org.fisco.bcos.channel.client.Service service = ConsoleInitializer.context.getBean(Service.class);


        TransactionDecoder decoder = new TransactionDecoder(OracleCore.ABI);
        OracleService oracleService = new OracleService(web3j,credentials);
        EventLogUserParams params = initSingleEventLogUserParams();
        ContractEventCallback callBack = new ContractEventCallback(oracleService, decoder);
        service.registerEventLogFilter(params, callBack);
        //必须要刷新service
      ((AbstractRefreshableApplicationContext) ConsoleInitializer.context).refresh();

        TransactionReceipt t11 = temperatureOracle.update().send();
        System.out.println(t11.getStatus());
        Thread.sleep(1000);
       String s=  temperatureOracle.get().send();
        System.out.println("price get: " + s);
        System.out.println("oracle event register successfully!");

    }


    private EventLogUserParams initSingleEventLogUserParams() {
        EventLogUserParams params = new EventLogUserParams();
        params.setFromBlock("latest");
        params.setToBlock("latest");

        // addresses，设置为Java合约对象的地址
        List<String> addresses = new ArrayList<>();
        addresses.add(orcleAddress);
        params.setAddresses(addresses);
        List<Object> topics = new ArrayList<>();
        topics.add(EventEncoder.encode(LOG1_EVENT));
        params.setTopics(topics);

        return params;
    }

    private static String bytesToHex(byte[] bytes)
    {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ )
        {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        String finalHex = new String(hexChars);
        return finalHex;
    }
}
