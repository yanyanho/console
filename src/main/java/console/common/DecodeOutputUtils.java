package console.common;

import org.fisco.bcos.web3j.abi.FunctionReturnDecoder;
import org.fisco.bcos.web3j.abi.TypeReference;
import org.fisco.bcos.web3j.abi.datatypes.Function;
import org.fisco.bcos.web3j.abi.datatypes.Type;
import org.fisco.bcos.web3j.abi.datatypes.Utf8String;

import java.util.Collections;
import java.util.List;

/**
 * 解析合约执行的返回output
 * @author darwindu
 * @date 2019/8/14
 **/
public class DecodeOutputUtils {

    /**
     * 0x16回执TransactionReceipt解析
     * @param input
     * @return
     */
    public static String decodeOutputReturnString0x16(String input) {

        Function function =
            new Function(
                "Error",
                Collections.<Type>emptyList(),
                Collections.singletonList(new TypeReference<Utf8String>() {}));

        List<Type> r = FunctionReturnDecoder.decode(
            input.substring(10),
            function.getOutputParameters());
        return r.get(0).toString();
    }

    /**
     * 非0x16回执TransactionReceipt解析
     * @param abi 合约abi
     * @param input TransactionReceipt.input
     * @param output TransactionReceipt.output
     * @return
     */
//    public static InputAndOutputResult decodeOutputReturn(String abi, String input, String output) {
//
//        TransactionDecoder txDecodeSampleDecoder = TransactionDecoderFactory
//            .buildTransactionDecoder(abi, CommonConstant.EMPTY);
//        try {
//            return txDecodeSampleDecoder.decodeOutputReturnObject(input, output);
//        } catch (TransactionException | BaseException e) {
//            throw new SdkException(ErrorCode.DECODE_OUTPUT_ERROR, e);
//        }
//    }
}
