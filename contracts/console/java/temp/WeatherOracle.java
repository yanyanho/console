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
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610b26806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806327dc297e1461007d5780636b6c2d00146100f45780636d4ce63c1461014f578063a035b1fe146101df578063a2e620451461026f578063af640d0f14610286575b600080fd5b34801561008957600080fd5b506100f26004803603810190808035600019169060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506102b9565b005b34801561010057600080fd5b50610135600480360381019080803573ffffffffffffffffffffffffffffffffffffffff169060200190929190505050610497565b604051808215151515815260200191505060405180910390f35b34801561015b57600080fd5b506101646104fc565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156101a4578082015181840152602081019050610189565b50505050905090810190601f1680156101d15780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156101eb57600080fd5b506101f461059e565b6040518080602001828103825283818151815260200191508051906020019080838360005b83811015610234578082015181840152602081019050610219565b50505050905090810190601f1680156102615780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561027b57600080fd5b5061028461063c565b005b34801561029257600080fd5b5061029b6107cd565b60405180826000191660001916815260200191505060405180910390f35b6102c16107d3565b73ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156102fa57600080fd5b60036000836000191660001916815260200190815260200160002060009054906101000a900460ff161515610397576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b80600190805190602001906103ad929190610a55565b5060036000836000191660001916815260200190815260200160002060006101000a81549060ff02191690557f8c318ccbf1b5a5bfac9bec3f6458271f1269b3c27f7a459256494009f0e66d0c600160405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156104855780601f1061045a57610100808354040283529160200191610485565b820191906000526020600020905b81548152906001019060200180831161046857829003601f168201915b50509250505060405180910390a15050565b6000806104a38361089a565b11156104f257816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600190506104f7565b600090505b919050565b606060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105945780601f1061056957610100808354040283529160200191610594565b820191906000526020600020905b81548152906001019060200180831161057757829003601f168201915b5050505050905090565b60018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106345780601f1061060957610100808354040283529160200191610634565b820191906000526020600020905b81548152906001019060200180831161061757829003601f168201915b505050505081565b7f746f97619dd4e795da5586af48fcbd8a87f2860948dec93277b5b67b2f105cab6040518080602001828103825260348152602001807f4f7261636c65207175657279207761732073656e742c207374616e64696e672081526020017f627920666f722074686520616e737765722e2e2e00000000000000000000000081525060400191505060405180910390a161078b6040805190810160405280600381526020017f75726c0000000000000000000000000000000000000000000000000000000000815250608060405190810160405280604781526020017f6a736f6e28687474703a2f2f742e776561746865722e736f6a736f6e2e636f6d81526020017f2f6170692f776561746865722f636974792f313031303330313030292e64617481526020017f612e77656e6475000000000000000000000000000000000000000000000000008152506108a5565b600281600019169055506001600360006002546000191660001916815260200190815260200160002060006101000a81548160ff021916908315150217905550565b60025481565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c281d19e6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15801561085a57600080fd5b505af115801561086e573d6000803e3d6000fd5b505050506040513d602081101561088457600080fd5b8101908080519060200190929190505050905090565b6000813b9050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f99600085856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b8381101561095d578082015181840152602081019050610942565b50505050905090810190601f16801561098a5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b838110156109c35780820151818401526020810190506109a8565b50505050905090810190601f1680156109f05780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b158015610a1257600080fd5b505af1158015610a26573d6000803e3d6000fd5b505050506040513d6020811015610a3c57600080fd5b8101908080519060200190929190505050905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a9657805160ff1916838001178555610ac4565b82800160010185558215610ac4579182015b82811115610ac3578251825591602001919060010190610aa8565b5b509050610ad19190610ad5565b5090565b610af791905b80821115610af3576000816000905550600101610adb565b5090565b905600a165627a7a723058202715443609e758ef71615b3f175e69eac0962536f1caa9e93c9164ef25aca2410029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_myid\",\"type\":\"bytes32\"},{\"name\":\"_result\",\"type\":\"string\"}],\"name\":\"__callback\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"oracleAddress\",\"type\":\"address\"}],\"name\":\"oracle_setNetwork\",\"outputs\":[{\"name\":\"_networkSet\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"price\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"update\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"id\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"description\",\"type\":\"string\"}],\"name\":\"LogNewQuery\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"price\",\"type\":\"string\"}],\"name\":\"LogNewPriceMeasure\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC___CALLBACK = "__callback";

    public static final String FUNC_ORACLE_SETNETWORK = "oracle_setNetwork";

    public static final String FUNC_GET = "get";

    public static final String FUNC_PRICE = "price";

    public static final String FUNC_UPDATE = "update";

    public static final String FUNC_ID = "id";

    public static final Event LOGNEWQUERY_EVENT = new Event("LogNewQuery", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
    ;

    public static final Event LOGNEWPRICEMEASURE_EVENT = new Event("LogNewPriceMeasure", 
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

    public RemoteCall<String> price() {
        final Function function = new Function(FUNC_PRICE, 
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

    public List<LogNewPriceMeasureEventResponse> getLogNewPriceMeasureEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(LOGNEWPRICEMEASURE_EVENT, transactionReceipt);
        ArrayList<LogNewPriceMeasureEventResponse> responses = new ArrayList<LogNewPriceMeasureEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogNewPriceMeasureEventResponse typedResponse = new LogNewPriceMeasureEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.price = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public void registerLogNewPriceMeasureEventLogFilter(String fromBlock, String toBlock, List<String> otherTopcs, EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGNEWPRICEMEASURE_EVENT);
        registerEventLogPushFilter(ABI,BINARY,topic0,fromBlock,toBlock,otherTopcs,callback);
    }

    public void registerLogNewPriceMeasureEventLogFilter(EventLogPushWithDecodeCallback callback) {
        String topic0 = EventEncoder.encode(LOGNEWPRICEMEASURE_EVENT);
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

    public static class LogNewPriceMeasureEventResponse {
        public Log log;

        public String price;
    }
}
