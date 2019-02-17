pragma solidity ^0.5.0;

contract TournamentFactory{
      // index of created contracts

  address[] public tournamentContracts;
  address factoryOwner;
  
  event tournamentCreated(address contractAddress);
  
  constructor() public{
      factoryOwner = msg.sender;
  }

  function getContractCount() public view returns(uint contractCount){
    return tournamentContracts.length;
  }
  

  function createNewTournament(address _tournamentOrganizer, uint _winnersPot) public returns(address newContract){
    Tournament tc = new Tournament(_tournamentOrganizer, _winnersPot);
    tournamentContracts.push(address(tc));
    emit tournamentCreated(address(tc));
    return address(tc);
  }
}


contract Tournament {

    //Member Variables
    address public tournamentOrganizer;
    mapping(address => uint) funders;
    uint public winnersPot;//In wei
    uint public currentFunds;
    bool public isFunded;
    tournamentState public state;
    
    enum tournamentState{
        ACTIVE,COMPLETED,CANCELED
    }
    
    //Modifiers
    modifier only_organizer(){
        assert(msg.sender==tournamentOrganizer);
        _;
    }
    
    modifier isActive(){
        require(state == tournamentState.ACTIVE);
        _;
    }
    
    
    //Functions
    function () external{
        
    }
    
    constructor(address _tournamentOrganizer, uint _winnersPot) public {
        tournamentOrganizer = _tournamentOrganizer;
        winnersPot = _winnersPot;
        state = tournamentState.ACTIVE;
    }
    
    function fundTournamant()public isActive payable{
        require(!isFunded, "Tournament Is Already Fully Funded");
        currentFunds += msg.value;
        if(currentFunds >= winnersPot){
            isFunded = true;
        }
        funders[msg.sender] = funders[msg.sender] + msg.value;
    }
    
    function cancelTournament()public isActive only_organizer{
        state = tournamentState.CANCELED;
    }
    
    function withdrawFunds()public{
        require(state == tournamentState.CANCELED, "Tournament Is Not Canceled");
        require(funders[msg.sender] > 0, "You Did Not Fund This Tournament");
        
        uint _amount = funders[msg.sender];
        funders[msg.sender] = 0;
        msg.sender.transfer(_amount);
        currentFunds = currentFunds - _amount;
    }
    
    function payOutWinners(address payable _fistPlace, address payable _secondPlace)public isActive only_organizer{
        require(isFunded, "Tournament Was Never Funded");
        //Calculate Payouts 70/30
        uint _amount2 = currentFunds/10;
        _amount2 = _amount2*3;
        uint _amount1 = currentFunds - _amount2;
        
        _fistPlace.transfer(_amount1);
        _secondPlace.transfer(_amount2);
        
        //Cleanup
        state = tournamentState.COMPLETED;
        currentFunds = 0;
    }

}
