/**
 * Copyright 2014-2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package console.oracle.event.callback;

import console.common.CommonUtils;
import console.oracle.OracleService;
import org.apache.commons.lang3.StringUtils;
import org.fisco.bcos.channel.event.filter.EventLogPushWithDecodeCallback;
import org.fisco.bcos.web3j.abi.datatypes.generated.Bytes32;
import org.fisco.bcos.web3j.protocol.core.methods.response.Log;
import org.fisco.bcos.web3j.tx.txdecode.BaseException;
import org.fisco.bcos.web3j.tx.txdecode.LogResult;
import org.fisco.bcos.web3j.tx.txdecode.TransactionDecoder;
import org.fisco.bcos.web3j.utils.Numeric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 从callback中获取事件推送过来的请求地址，再请求该地址获取数据上链。
 */
public class ContractEventCallback extends EventLogPushWithDecodeCallback {
    private static final String LOG_ID = "cid";
    private static final String LOG_ARG = "arg";
    private static final String LOG_SENDER = "sender";
    private static final String SUB_URL_EVENT_FILED_PREFIX = "json(";

    private static final Logger logger =
            LoggerFactory.getLogger(ContractEventCallback.class);

    private OracleService oracleService;


    public ContractEventCallback(OracleService oracleService, TransactionDecoder decoder) {
        // onPush will call father class's decoder, init EventLogPushWithDecodeCallback's decoder
        this.setDecoder(decoder);
        this.oracleService = oracleService;
    }


    /**
     * 根据Log对象中的blockNumber，transactionIndex，logIndex进行去重
     *
     * @param status
     * @param logs
     */
    @Override
    public void onPushEventLog(int status, List<LogResult> logs) {
        logger.info(
                "ContractEventCallback onPushEventLog  params: {}, status: {}, logs: {}",
                getFilter().getParams(), status, logs);

        for (LogResult log : logs) {
            String cidStr = "";
            try {

                Bytes32 cid = CommonUtils.getBytes32FromEventLog(log.getLogParams(), LOG_ID);
                cidStr = Numeric.toHexString(cid.getValue());
                String argValue = CommonUtils.getStringFromEventLog(log.getLogParams(), LOG_ARG);
                String contractAddress = CommonUtils.getStringFromEventLog(log.getLogParams(), LOG_SENDER);
                logger.debug("==========cidStr:{} arg:{} contractAddress:{} ", cidStr, argValue, contractAddress);

               // String url = subFiledValueForUrl(argValue);
                if (StringUtils.isBlank(argValue)) {
                    logger.warn("argValue is empty");
                }
                //  argValue = argValue.replace(SUB_URL_EVENT_FILED_PREFIX, "");
                int left = argValue.indexOf("(");
                int right = argValue.indexOf(")");
                String format = argValue.substring(0,left);
                String url = argValue.substring(left+1,right);
                List<String> httpResultIndexList = subFiledValueForHttpResultIndex(argValue);
                //get data from url and update blockChain
                oracleService.getDataFromUrlAndUpChain(contractAddress, cid.getValue(), url,format, httpResultIndexList);
                logger.info("cid:{}   callback success", cidStr);
                System.out.println( "oracle service upload result to blockchain successfully, please query your contract for result!");
            } catch (Exception ex) {
                logger.error("onPushEventLog exception, cidStr:{}", cidStr, ex);
            }
        }
    }


    @Override
    public LogResult transferLogToLogResult(Log log) {
        try {
            LogResult logResult = getDecoder().decodeEventLogReturnObject(log);
            return logResult;
        } catch (BaseException e) {
            logger.warn(" event log decode failed, log: {}", log);
            return null;
        }
    }


    /**
     * @param argValue
     * @return
     */
    private String subFiledValueForUrl(String argValue) {
        if (StringUtils.isBlank(argValue)) {
            logger.warn("argValue is empty");
            return argValue;
        }
      //  argValue = argValue.replace(SUB_URL_EVENT_FILED_PREFIX, "");
        int left = argValue.indexOf("(");
        int right = argValue.indexOf(")");
        String header = argValue.substring(0,left);
        String url = argValue.substring(left,right);

        return url;
    }


    /**
     * @param argValue
     * @return
     */
    private List<String> subFiledValueForHttpResultIndex(String argValue) {
        if (StringUtils.isBlank(argValue) || argValue.endsWith(")")) {
            logger.warn("argValue is:{} ,return empty list", argValue);
            return Collections.EMPTY_LIST;
        }

        String resultIndex = argValue.substring(argValue.indexOf(").") + 2);

        String[] resultIndexArr = resultIndex.split("\\.");
        List resultList = new ArrayList<>(resultIndexArr.length);
        Collections.addAll(resultList, resultIndexArr);
        return resultList;
    }


}
