pragma solidity 0.5.7;

import "./inherited/ERC20Token.sol";
import "./inherited/ERC721Basic.sol";

// Contract: 0xf09362eb76f310170a3874b6e16b416ddf28a7ed

contract Tournaments {

    struct Tournament {
        address payable organizer;
        uint deadline;
        bool active;
        address token;
        uint tokenVersion; // The version of the token being used (0 for ETH, 20 for ERC20, 721 for ERC721)
        uint balance;
        uint maxPlayers;
    }

    uint public numTournaments;
    mapping(uint => Tournament) public tournaments;
    mapping(uint => uint[]) public prizeDistributions;

    bool public callStarted; // Ensures mutex for the entire contract

    modifier callNotStarted(){
        require(!callStarted);
        _;
    }

    modifier validateTournamentArrayIndex(uint _index) {
        require(_index < numTournaments);
        _;
    }

    modifier onlyOrganizer(address _sender, uint _tournamentId) {
        require(_sender == tournaments[_tournamentId].organizer);
        _;
    }

    modifier tournamentActive(uint _tournamentId) {
        require(tournaments[_tournamentId].active);
        _;
    }

    function newTournament(
        address payable _organizer,
        string memory _data,
        uint _deadline,
        address _token,
        uint _tokenVersion,
        uint _maxPlayers,
        uint[] memory _prizeDistribution) public payable returns (uint) {

        require(_tokenVersion == 0 || _tokenVersion == 20 || _tokenVersion == 721);
        require (_prizeDistribution.length == _maxPlayers);

        uint total = 0;
        for (uint i=0; i < _prizeDistribution.length; i++) {
            total += _prizeDistribution[i];
        }
        require (total == 100);

        uint tournamentId = numTournaments;

        Tournament storage tour = tournaments[tournamentId];
        tour.organizer = _organizer;
        tour.deadline = _deadline;
        tour.tokenVersion = _tokenVersion;
        prizeDistributions[tournamentId] = _prizeDistribution;
        tour.active = true;
        tour.maxPlayers = _maxPlayers;

        if (_tokenVersion != 0) {
            tour.token = _token;
        }

        numTournaments++;

        emit TournamentIssued(
            tournamentId,
            _organizer,
            _data,
            _deadline,
            _token,
            _tokenVersion,
            _maxPlayers,
            _prizeDistribution
        );

        return (tournamentId);
    }

    function contribute(uint _tournamentId, uint _amount) public payable
    validateTournamentArrayIndex(_tournamentId)
    tournamentActive(_tournamentId)
    callNotStarted {

        require(_amount > 0); // Contributions of 0 tokens or token ID 0 should fail

        callStarted = true;

        if (tournaments[_tournamentId].tokenVersion == 0) {
            require(msg.value == _amount);
            tournaments[_tournamentId].balance += _amount;

        } else if (tournaments[_tournamentId].tokenVersion == 20) {
            require(msg.value == 0);
            require(ERC20Token(tournaments[_tournamentId].token).transferFrom(msg.sender, address(this), _amount));
            tournaments[_tournamentId].balance += _amount;

        } else if (tournaments[_tournamentId].tokenVersion == 721) {
            // require(msg.value == 0);
            // ERC721BasicToken(tournaments[_tournamentId].token).transferFrom(
            //     msg.sender,
            //     address(this),
            //     _amount);
            // tokenBalances[_tournamentId][_amount] = true;
        } else {
            revert();
        }

        emit ContributionAdded(
            _tournamentId,
            msg.sender,
            _amount
        );

        callStarted = false;
    }

    function changeDeadline(uint _tournamentId, uint _deadline) external
    validateTournamentArrayIndex(_tournamentId)
    onlyOrganizer(msg.sender, _tournamentId) {

        tournaments[_tournamentId].deadline = _deadline;
        emit TournamentDeadlineChanged(_tournamentId, msg.sender, _deadline);
    }

    function payoutWinners(uint _tournamentId, address payable[] memory _winners) public
    callNotStarted
    tournamentActive(_tournamentId)
    onlyOrganizer(msg.sender, _tournamentId)
    validateTournamentArrayIndex(_tournamentId) {

        callStarted = true;

        require(_winners.length == prizeDistributions[_tournamentId].length);
        uint[] memory payouts = new uint[](_winners.length);

        for (uint i = 0; i < _winners.length; i++) {
            uint amount = uint(prizeDistributions[_tournamentId][i] * tournaments[_tournamentId].balance) / 100;
            payouts[i] = amount;
        }

        for (uint i = 0; i < payouts.length; i++) {
            transferTokens(_tournamentId, _winners[i], payouts[i]);
        }

        tournaments[_tournamentId].active = false;

        emit TournamentFinalized( _tournamentId, _winners, payouts);

        callStarted = false;
    }

    function transferTokens(uint _tournamentId, address payable _to, uint _amount) internal {
        if (tournaments[_tournamentId].tokenVersion == 0) {
            if (_amount <= 0) {
                return;
            }

            tournaments[_tournamentId].balance -= _amount;

            _to.transfer(_amount);
        } else if (tournaments[_tournamentId].tokenVersion == 20) {
            if (_amount <= 0) {
                return;
            }

            tournaments[_tournamentId].balance -= _amount;

            require(ERC20Token(tournaments[_tournamentId].token).transfer(_to, _amount));
        } else if (tournaments[_tournamentId].tokenVersion == 721) {
            revert(); // Not supported yet
            //   tournaments[_tournamentId][_amount] = false; // Removes the 721 token from the balance of the bounty

            //   ERC721BasicToken(tournaments[_tournamentId].token).transferFrom(address(this),
            //                                                           _to,
            //                                                           _amount);
        } else {
            revert();
        }
    }

    event TournamentIssued(uint _tournamentId, address payable _organizer, string _data, uint _deadline, address _token, uint _tokenVersion, uint _maxPlayers, uint[] _prizeDistribution);
    event ContributionAdded(uint _tournamentId, address payable _contributor, uint _amount);
    event TournamentDeadlineChanged(uint _tournamentId, address _changer, uint _deadline);
    event TournamentFinalized(uint _tournamentId, address payable[] _winners, uint[] payouts);
}
