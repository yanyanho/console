package temp;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.fisco.bcos.channel.client.TransactionSucCallback;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.EventEncoder;
import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Address;
import org.fisco.bcos.web3j.abi.datatypes.Bool;
import org.fisco.bcos.web3j.abi.datatypes.Event;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.fisco.bcos.web3j.protocol.Web3j;
import org.fisco.bcos.web3j.protocol.core.RemoteCall;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.protocol.core.methods.response.TransactionReceipt;
import org.fisco.bcos.web3j.tuples.generated.Tuple1;
import org.fisco.bcos.web3j.tuples.generated.Tuple2;
import org.fisco.bcos.web3j.tx.Contract;
import org.fisco.bcos.web3j.tx.TransactionManager;
import org.fisco.bcos.web3j.tx.gas.ContractGasProvider;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.fisco.bcos.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version none.
 */
@SuppressWarnings("unchecked")
public class WeatherOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b506109ed806100206000396000f30060806040526004361061006d576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806327dc297e146100725780636b6c2d00146100e95780636d4ce63c14610144578063a2e62045146101d4578063af640d0f146101eb575b600080fd5b34801561007e57600080fd5b506100e76004803603810190808035600019169060200190929190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919291929050505061021e565b005b3480156100f557600080fd5b5061012a600480360381019080803573ffffffffffffffffffffffffffffffffffffffff1690602001909291905050506103fc565b604051808215151515815260200191505060405180910390f35b34801561015057600080fd5b50610159610461565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561019957808201518184015260208101905061017e565b50505050905090810190601f1680156101c65780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101e057600080fd5b506101e9610503565b005b3480156101f757600080fd5b50610200610694565b60405180826000191660001916815260200191505060405180910390f35b61022661069a565b73ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561025f57600080fd5b60036000836000191660001916815260200190815260200160002060009054906101000a900460ff1615156102fc576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b806001908051906020019061031292919061091c565b5060036000836000191660001916815260200190815260200160002060006101000a81549060ff02191690557f3c788c40baed6579048bb96c8cc6309868f8da25a96ef36b0d352d993106fa39600160405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156103ea5780601f106103bf576101008083540402835291602001916103ea565b820191906000526020600020905b8154815290600101906020018083116103cd57829003601f168201915b50509250505060405180910390a15050565b60008061040883610761565b111561045757816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905061045c565b600090505b919050565b606060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104f95780601f106104ce576101008083540402835291602001916104f9565b820191906000526020600020905b8154815290600101906020018083116104dc57829003601f168201915b5050505050905090565b7f746f97619dd4e795da5586af48fcbd8a87f2860948dec93277b5b67b2f105cab6040518080602001828103825260348152602001807f4f7261636c65207175657279207761732073656e742c207374616e64696e672081526020017f627920666f722074686520616e737765722e2e2e00000000000000000000000081525060400191505060405180910390a16106526040805190810160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250608060405190810160405280604781526020017f6a736f6e28687474703a2f2f742e776561746865722e736f6a736f6e2e636f6d81526020017f2f6170692f776561746865722f636974792f313031303330313030292e64617481526020017f612e77656e64750000000000000000000000000000000000000000000000000081525061076c565b600281600019169055506001600360006002546000191660001916815260200190815260200160002060006101000a81548160ff021916908315150217905550565b60025481565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c281d19e6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561072157600080fd5b505af1158015610735573d6000803e3d6000fd5b505050506040513d602081101561074b57600080fd5b8101908080519060200190929190505050905090565b6000813b9050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f99600085856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610824578082015181840152602081019050610809565b50505050905090810190601f1680156108515780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b8381101561088a57808201518184015260208101905061086f565b50505050905090810190601f1680156108b75780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b1580156108d957600080fd5b505af11580156108ed573d6000803e3d6000fd5b505050506040513d602081101561090357600080fd5b8101908080519060200190929190505050905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061095d57805160ff191683800117855561098b565b8280016001018555821561098b579182015b8281111561098a57825182559160200191906001019061096f565b5b509050610998919061099c565b5090565b6109be91905b808211156109ba5760008160009055506001016109a2565b5090565b905600a165627a7a72305820ea5a26b71a2c1389a94ec058a5411ccc10ddded8c5cf6ec634fbaa25fda189cc0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_myid\",\"type\":\"bytes32\"},{\"name\":\"_result\",\"type\":\"string\"}],\"name\":\"__callback\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"oracleAddress\",\"type\":\"address\"}],\"name\":\"oracle_setNetwork\",\"outputs\":[{\"name\":\"_networkSet\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"update\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"id\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"description\",\"type\":\"string\"}],\"name\":\"LogNewQuery\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"temp\",\"type\":\"string\"}],\"name\":\"LogNewTempMeasure\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC___CALLBACK = "__callback";

    public static final String FUNC_ORACLE_SETNETWORK = "oracle_setNetwork";

    public static final String FUNC_GET = "get";

    public static final String FUNC_UPDATE = "update";

    public static final String FUNC_ID = "id";

    public static final Event LOGNEWQUERY_EVENT = new Event("LogNewQuery", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGNEWTEMPMEASURE_EVENT = new Event("LogNewTempMeasure", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    @Deprecated
    protected WeatherOracle(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected WeatherOracle(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected WeatherOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected WeatherOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<TransactionReceipt> __callback(byte[] _myid, String _result) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_myid), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void __callback(byte[] _myid, String _result, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_myid), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String __callbackSeq(byte[] _myid, String _result) {
        final Function function = new Function(
                FUNC___CALLBACK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32(_myid), 
                new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_result)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple2<byte[], String> get__callbackInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC___CALLBACK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple2<byte[], String>(

                (byte[]) results.get(0).getValue(), 
                (String) results.get(1).getValue()
                );
    }

    public RemoteCall<TransactionReceipt> oracle_setNetwork(String oracleAddress) {
        final Function function = new Function(
                FUNC_ORACLE_SETNETWORK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void oracle_setNetwork(String oracleAddress, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_ORACLE_SETNETWORK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String oracle_setNetworkSeq(String oracleAddress) {
        final Function function = new Function(
                FUNC_ORACLE_SETNETWORK, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Address(oracleAddress)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getOracle_setNetworkInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_ORACLE_SETNETWORK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
    }

    public Tuple1<Boolean> getOracle_setNetworkOutput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getOutput();
        final Function function = new Function(FUNC_ORACLE_SETNETWORK, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<Boolean>(

                (Boolean) results.get(0).getValue()
                );
    }

    public RemoteCall<String> get() {
        final Function function = new Function(FUNC_GET, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> update() {
        final Function function = new Function(
                FUNC_UPDATE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void update(TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_UPDATE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String updateSeq() {
        final Function function = new Function(
                FUNC_UPDATE, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public RemoteCall<byte[]> id() {
        final Function function = new Function(FUNC_ID, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bytes32>() {}));
        return executeRemoteCallSingleValueReturn(function, byte[].class);
    }

    public List<LogNewQueryEventResponse> getLogNewQueryEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGNEWQUERY_EVENT, transactionReceipt);
        ArrayList<LogNewQueryEventResponse> responses = new ArrayList<LogNewQueryEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogNewQueryEventResponse typedResponse = new LogNewQueryEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerLogNewQueryEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGNEWQUERY_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerLogNewQueryEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGNEWQUERY_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    public List<LogNewTempMeasureEventResponse> getLogNewTempMeasureEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGNEWTEMPMEASURE_EVENT, transactionReceipt);
        ArrayList<LogNewTempMeasureEventResponse> responses = new ArrayList<LogNewTempMeasureEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogNewTempMeasureEventResponse typedResponse = new LogNewTempMeasureEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.temp = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerLogNewTempMeasureEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGNEWTEMPMEASURE_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerLogNewTempMeasureEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGNEWTEMPMEASURE_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,callback);
    }

    @Deprecated
    public static WeatherOracle load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new WeatherOracle(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static WeatherOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new WeatherOracle(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static WeatherOracle load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new WeatherOracle(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static WeatherOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new WeatherOracle(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<WeatherOracle> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WeatherOracle.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<WeatherOracle> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(WeatherOracle.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<WeatherOracle> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(WeatherOracle.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<WeatherOracle> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(WeatherOracle.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class LogNewQueryEventResponse {
        public Log log;

        public String description;
    }

    public static class LogNewTempMeasureEventResponse {
        public Log log;

        public String temp;
    }
}
