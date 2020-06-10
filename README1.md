
# Oracle控制台

控制台是[FISCO BCOS 2.0+](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/)的重要交互式客户端工具。控制台拥有丰富的命令，包括查询区块链状态、管理区块链节点、部署并调用合约等。

## 关键特性

 - 提供大量的区块链状态查询命令。
 - 提供简单易用的部署和调用合约命令。
 - 提供一些可以管理区块链节点的命令。
 - 提供一个合约编译工具，用户可以方便快捷的将Solidity合约文件编译为Java合约文件。


## 源码安装
```
$ git clone https://github.com/FISCO-BCOS/console.git
$ cd console
$ ./gradlew build
```
如果安装成功，将在当前目录生成一个`dist`目录。

## 使用
- 具体参考 [控制台手册](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html)。


## 配置
控制台具体配置参考[这里](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/console.html#id11)。

## oracle 使用 
  进入控制台界面后：
 1. 编写自己的Oracle合约并拷贝到./contracts/console/solidity目录下（（可参考SampleOracle合约）），合约必须继承usingOracleCore，并且实现 
  function __callback(bytes32 _myid,string memory _result) 方法，方便oracleService回调这个方法。
 2. oracle-start  
  此命令会用特定私钥执行OracleCore合约的部署，OracleCore合约对应由oracleService管理，oracleService并注册event事件监听。
 3. deploy SampleOracle  
   部署合约
 4. call SampleOracle 0x0b9e03b92566581ec9b735911ff7fe7e751dc7e7  oracle_setNetwork "OracleCore地址"   
  设置OracleCore地址，这样SampleOracle可以调用OracleCore合约。
 5. call SampleOracle 0x0b9e03b92566581ec9b735911ff7fe7e751dc7e7  setUrl "json(http://t.weather.sojson.com/api/weather/city/101030100).data.wendu"    
   设置你要访问的url。
 6. call SampleOracle 0x0b9e03b92566581ec9b735911ff7fe7e751dc7e7  update
   发起查询链下数据请求。oracleService监听到请求会发起http调用并回写到SampleOracle合约。
 7. call SampleOracle 0x0b9e03b92566581ec9b735911ff7fe7e751dc7e7  get  
    查询最新链下结果。
    


