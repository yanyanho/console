pragma solidity ^0.4.24;

import "./Oracle.sol";

contract PriceOracle is usingOracleCore {

    string public price;
    bytes32 public id;
    mapping(bytes32=>bool) validIds;

    event LogNewQuery(string description);
    event LogNewPriceMeasure(string price);

    constructor()
        public
    {
       // update(); // Update on contract creation...
    }

    function __callback(
        bytes32 _myid,
        string memory _result
    )
        public
    {
        require(msg.sender == oracle_cbAddress());
         require(validIds[_myid], "id must be not used!") ;
        price = _result;
        delete validIds[_myid];
        emit LogNewPriceMeasure(price);

    }

    function update()
        public
    {
        emit LogNewQuery("Oracle query was sent, standing by for the answer...");
        id = oracle_query("url", "plain(https://www.random.org/integers/?num=100&min=1&max=100&col=1&base=10&format=plain&rnd=new)");
        validIds[id] = true;
    }

      function get()  public view  returns(string){
              return price;
            }
}