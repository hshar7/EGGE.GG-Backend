pragma solidity ^0.5.0;

contract TournamentContract {
    //Member Variables
    Tournament[] tournaments;
    address private contractOwner;
    
    
    //modifier
    modifier only_owner(uint _tournamentId){
        require(msg.sender==tournaments[_tournamentId].tournamentOrganizer);
        _;
    }
    
    modifier valueIsNotZero(uint _costToEnter){
        require(_costToEnter > 0);
        _;
    }
    modifier only_contract_owner(){
        require(msg.sender == contractOwner);
        _;
    }
    
    //Structs
    struct Tournament{
        address  tournamentOrganizer;
        mapping(string => address) players;
        address[] playerAddresses;
        uint maxNumberOfPlayers;
        uint currentNumberOfPlayers;
        uint costToEnter;//In wei
    }
    
    //functions
    function () external{
        
    }
    
    constructor() public{
        contractOwner = msg.sender;
    }
    
    function viewTournament(uint _tournamentId)public view returns(address, uint, uint, uint){
        return (tournaments[_tournamentId].tournamentOrganizer, tournaments[_tournamentId].maxNumberOfPlayers, tournaments[_tournamentId].currentNumberOfPlayers, tournaments[_tournamentId].costToEnter);
        
    }
    
    function viewPlayers(uint _tournamentId)public view returns(address[] memory){
        return tournaments[_tournamentId].playerAddresses;
    }
    
    function createTournament(uint _tournamentId, address _tournamentOragizer, uint _maxNumberOfPlayers, uint256 _costToEnter)
    public valueIsNotZero(_maxNumberOfPlayers) valueIsNotZero(_costToEnter) returns(bool success){
        require(tournaments[_tournamentId].maxNumberOfPlayers == 0);
        address[] memory _playerAddresses = new address[](8);
        tournaments[_tournamentId] = Tournament({tournamentOrganizer: _tournamentOragizer, playerAddresses: _playerAddresses, maxNumberOfPlayers: _maxNumberOfPlayers, currentNumberOfPlayers: 0, costToEnter: _costToEnter});
        return true;
    }
    
    function addPlayer(string memory uuid, uint _tournamentId)public payable{
        require(tournaments[_tournamentId].currentNumberOfPlayers + 1 <= tournaments[_tournamentId].maxNumberOfPlayers);//Make sure tournament isnt at capacity
        require(tournaments[_tournamentId].players[uuid]==address(0));                                                  //Make sure player id is unique
        require(msg.value == tournaments[_tournamentId].costToEnter);                                                   //Make sure enterence fee is getting payed
        
        tournaments[_tournamentId].players[uuid] = msg.sender;
        tournaments[_tournamentId].playerAddresses.push(msg.sender);
        tournaments[_tournamentId].currentNumberOfPlayers++;
    }
    
    function removePlayer(string memory uuid, uint _tournamentId) public {
        require(tournaments[_tournamentId].players[uuid] == msg.sender || tournaments[_tournamentId].tournamentOrganizer == msg.sender);
        tournaments[_tournamentId].currentNumberOfPlayers--;
        tournaments[_tournamentId].players[uuid] = address(0);
    }
        
    function payOutWinners(string memory first, string memory second, uint _tournamentId)public only_owner(_tournamentId) {
        
    }
    
}
