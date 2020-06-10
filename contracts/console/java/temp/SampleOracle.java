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
public class SampleOracle extends Contract {
    public static final String[] BINARY_ARRAY = {"608060405234801561001057600080fd5b50610a93806100206000396000f300608060405260043610610078576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff168063252498a21461007d57806327dc297e146100e65780636b6c2d001461015d5780636d4ce63c146101b8578063a2e6204514610248578063af640d0f1461025f575b600080fd5b34801561008957600080fd5b506100e4600480360381019080803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509192919290505050610292565b005b3480156100f257600080fd5b5061015b6004803603810190808035600019169060200190929190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091929192905050506102ac565b005b34801561016957600080fd5b5061019e600480360381019080803573ffffffffffffffffffffffffffffffffffffffff16906020019092919050505061048a565b604051808215151515815260200191505060405180910390f35b3480156101c457600080fd5b506101cd6104ef565b6040518080602001828103825283818151815260200191508051906020019080838360005b8381101561020d5780820151818401526020810190506101f2565b50505050905090810190601f16801561023a5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561025457600080fd5b5061025d610591565b005b34801561026b57600080fd5b5061027461073a565b60405180826000191660001916815260200191505060405180910390f35b80600490805190602001906102a89291906109c2565b5050565b6102b4610740565b73ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156102ed57600080fd5b60036000836000191660001916815260200190815260200160002060009054906101000a900460ff16151561038a576040517f08c379a00000000000000000000000000000000000000000000000000000000081526004018080602001828103825260148152602001807f6964206d757374206265206e6f7420757365642100000000000000000000000081525060200191505060405180910390fd5b80600190805190602001906103a09291906109c2565b5060036000836000191660001916815260200190815260200160002060006101000a81549060ff02191690557f3c788c40baed6579048bb96c8cc6309868f8da25a96ef36b0d352d993106fa39600160405180806020018281038252838181546001816001161561010002031660029004815260200191508054600181600116156101000203166002900480156104785780601f1061044d57610100808354040283529160200191610478565b820191906000526020600020905b81548152906001019060200180831161045b57829003601f168201915b50509250505060405180910390a15050565b60008061049683610807565b11156104e557816000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550600190506104ea565b600090505b919050565b606060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105875780601f1061055c57610100808354040283529160200191610587565b820191906000526020600020905b81548152906001019060200180831161056a57829003601f168201915b5050505050905090565b7f746f97619dd4e795da5586af48fcbd8a87f2860948dec93277b5b67b2f105cab6040518080602001828103825260348152602001807f4f7261636c65207175657279207761732073656e742c207374616e64696e672081526020017f627920666f722074686520616e737765722e2e2e00000000000000000000000081525060400191505060405180910390a16106f86040805190810160405280600381526020017f75726c000000000000000000000000000000000000000000000000000000000081525060048054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156106ee5780601f106106c3576101008083540402835291602001916106ee565b820191906000526020600020905b8154815290600101906020018083116106d157829003601f168201915b5050505050610812565b600281600019169055506001600360006002546000191660001916815260200190815260200160002060006101000a81548160ff021916908315150217905550565b60025481565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c281d19e6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1580156107c757600080fd5b505af11580156107db573d6000803e3d6000fd5b505050506040513d60208110156107f157600080fd5b8101908080519060200190929190505050905090565b6000813b9050919050565b60008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f99600085856040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156108ca5780820151818401526020810190506108af565b50505050905090810190601f1680156108f75780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610930578082015181840152602081019050610915565b50505050905090810190601f16801561095d5780820380516001836020036101000a031916815260200191505b5095505050505050602060405180830381600087803b15801561097f57600080fd5b505af1158015610993573d6000803e3d6000fd5b505050506040513d60208110156109a957600080fd5b8101908080519060200190929190505050905092915050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10610a0357805160ff1916838001178555610a31565b82800160010185558215610a31579182015b82811115610a30578251825591602001919060010190610a15565b5b509050610a3e9190610a42565b5090565b610a6491905b80821115610a60576000816000905550600101610a48565b5090565b905600a165627a7a72305820423e6da94172855bc8b8f6f93d3c91f04b3b5f7acbbd8fb5b40d6d202a12a59d0029"};

    public static final String BINARY = String.join("", BINARY_ARRAY);

    public static final String[] ABI_ARRAY = {"[{\"constant\":false,\"inputs\":[{\"name\":\"_url\",\"type\":\"string\"}],\"name\":\"setUrl\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"_myid\",\"type\":\"bytes32\"},{\"name\":\"_result\",\"type\":\"string\"}],\"name\":\"__callback\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[{\"name\":\"oracleAddress\",\"type\":\"address\"}],\"name\":\"oracle_setNetwork\",\"outputs\":[{\"name\":\"_networkSet\",\"type\":\"bool\"}],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"get\",\"outputs\":[{\"name\":\"\",\"type\":\"string\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"constant\":false,\"inputs\":[],\"name\":\"update\",\"outputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"constant\":true,\"inputs\":[],\"name\":\"id\",\"outputs\":[{\"name\":\"\",\"type\":\"bytes32\"}],\"payable\":false,\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"payable\":false,\"stateMutability\":\"nonpayable\",\"type\":\"constructor\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"description\",\"type\":\"string\"}],\"name\":\"LogNewQuery\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"name\":\"temp\",\"type\":\"string\"}],\"name\":\"LogNewTempMeasure\",\"type\":\"event\"}]"};

    public static final String ABI = String.join("", ABI_ARRAY);

    public static final TransactionDecoder transactionDecoder = new TransactionDecoder(ABI, BINARY);

    public static final String FUNC_SETURL = "setUrl";

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
    protected SampleOracle(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SampleOracle(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected SampleOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected SampleOracle(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static TransactionDecoder getTransactionDecoder() {
        return transactionDecoder;
    }

    public RemoteCall<TransactionReceipt> setUrl(String _url) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public void setUrl(String _url, TransactionSucCallback callback) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList());
        asyncExecuteTransaction(function, callback);
    }

    public String setUrlSeq(String _url) {
        final Function function = new Function(
                FUNC_SETURL, 
                Arrays.<Type>asList(new org.fisco.bcos.web3j.abi.datatypes.Utf8String(_url)), 
                Collections.<TypeReference<?>>emptyList());
        return createTransactionSeq(function);
    }

    public Tuple1<String> getSetUrlInput(TransactionReceipt transactionReceipt) {
        String data = transactionReceipt.getInput().substring(10);
        final Function function = new Function(FUNC_SETURL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Type> results = FunctionReturnDecoder.decode(data, function.getOutputParameters());;
        return new Tuple1<String>(

                (String) results.get(0).getValue()
                );
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
    public static SampleOracle load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SampleOracle(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static SampleOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SampleOracle(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static SampleOracle load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new SampleOracle(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static SampleOracle load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new SampleOracle(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<SampleOracle> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SampleOracle.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<SampleOracle> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(SampleOracle.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SampleOracle> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SampleOracle.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<SampleOracle> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SampleOracle.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
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
