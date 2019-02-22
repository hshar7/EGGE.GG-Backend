pragma solidity ^0.5.0;

contract TournamentContract {
    mapping(uint => Tournament) public tournaments;

    //Member Variables
    struct Tournament{
        address tournamentOrganizer;
        mapping(address => uint) funders;
        uint winnersPot;//In wei
        uint currentFunds;
        bool isFunded;
        tournamentState state;
    }

    enum tournamentState{
        ACTIVE,COMPLETED,CANCELED
    }

    //Modifiers
    modifier only_organizer(uint _tournamentId){
        assert(msg.sender == tournaments[_tournamentId].tournamentOrganizer);
        _;
    }

    modifier isActive(uint _tournamentId){
        require(tournaments[_tournamentId].state == tournamentState.ACTIVE);
        _;
    }


    //Functions
    function () external{

    }


    function createTournament(address _tournamentOrganizer, uint _winnersPot, uint _tournamentId) public {
        require(tournaments[_tournamentId].tournamentOrganizer == address(0));
        tournaments[_tournamentId].tournamentOrganizer = _tournamentOrganizer;
        tournaments[_tournamentId].winnersPot = _winnersPot;
    }

    function fundTournamant(uint _tournamentId)public isActive(_tournamentId) payable{
        require(!tournaments[_tournamentId].isFunded, "Tournament Is Already Fully Funded");
        tournaments[_tournamentId].currentFunds += msg.value;
        if(tournaments[_tournamentId].currentFunds >= tournaments[_tournamentId].winnersPot){
            tournaments[_tournamentId].isFunded = true;
        }
        tournaments[_tournamentId].funders[msg.sender] = tournaments[_tournamentId].funders[msg.sender] + msg.value;
    }

    function cancelTournament(uint _tournamentId)public isActive(_tournamentId) only_organizer(_tournamentId){
        tournaments[_tournamentId].state = tournamentState.CANCELED;
    }

    function withdrawFunds(uint _tournamentId)public{
        require(tournaments[_tournamentId].state == tournamentState.CANCELED, "Tournament Is Not Canceled");
        require(tournaments[_tournamentId].funders[msg.sender] > 0, "You Did Not Fund This Tournament");

        uint _amount = tournaments[_tournamentId].funders[msg.sender];
        tournaments[_tournamentId].funders[msg.sender] = 0;
        msg.sender.transfer(_amount);
        tournaments[_tournamentId].currentFunds = tournaments[_tournamentId].currentFunds - _amount;
    }

    function payOutWinners(address payable _fistPlace, address payable _secondPlace, uint _tournamentId)public isActive(_tournamentId) only_organizer(_tournamentId){
        require(tournaments[_tournamentId].isFunded, "Tournament Was Never Funded");
        //Calculate Payouts 70/30
        uint _amount2 = tournaments[_tournamentId].currentFunds/10;
        _amount2 = _amount2*3;
        uint _amount1 = tournaments[_tournamentId].currentFunds - _amount2;

        _fistPlace.transfer(_amount1);
        _secondPlace.transfer(_amount2);

        //Cleanup
        tournaments[_tournamentId].state = tournamentState.COMPLETED;
        tournaments[_tournamentId].currentFunds = 0;
    }

}