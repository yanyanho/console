/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package console.common;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.crypto.Sign.SignatureData;
import org.fisco.bcos.web3j.tx.txdecode.EventResultEntity;
import org.fisco.bcos.web3j.utils.Numeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static console.common.JsonUtils.stringToObj;
import static console.common.JsonUtils.toJSONString;

/**
 * CommonUtils.
 */
public class CommonUtils {

    private static final Logger log = LoggerFactory.getLogger(CommonUtils.class);
    /**
     * signatureDataToString.
     *
     * @param signatureData signatureData
     */
    public static String signatureDataToString(SignatureData signatureData) {
        byte[] byteArr = new byte[1 + signatureData.getR().length + signatureData.getS().length];
        byteArr[0] = signatureData.getV();
        System.arraycopy(signatureData.getR(), 0, byteArr, 1, signatureData.getR().length);
        System.arraycopy(signatureData.getS(), 0, byteArr, signatureData.getR().length + 1,
                signatureData.getS().length);
        return Numeric.toHexString(byteArr, 0, byteArr.length, false);
    }


    /**
     * @param params    Fields of the eventLog
     * @param fieldName Field names to be returned
     * @return Field value
     */
    public static String getStringFromEventLog(List<EventResultEntity> params, String fieldName) {
        return getDataFromEventLog(params, fieldName, String.class);
    }


    /**
     * @param params    Fields of the eventLog
     * @param fieldName Field names to be returned
     * @return Field value
     */
    public static Bytes32 getBytes32FromEventLog(List<EventResultEntity> params, String fieldName) {
        return getDataFromEventLog(params, fieldName, Bytes32.class);
    }

    /**
     * @param params      Fields of the eventLog
     * @param fieldName   Field names to be returned
     * @param resultClazz Return value type
     * @param <T>         Return value
     * @return Field value
     */
    private static <T> T getDataFromEventLog(List<EventResultEntity> params, String fieldName, Class<T> resultClazz) {
        for (EventResultEntity param : params) {
            if (!fieldName.equals(param.getName())) continue;
            if (Bytes32.class == resultClazz) {
                return (T) param.getTypeObject();
            }
            return stringToObj(toJSONString(param.getData()), resultClazz);
        }
        return null;
    }
}
