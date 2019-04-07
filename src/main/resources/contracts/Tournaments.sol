//pragma solidity 0.5.7;
//pragma experimental ABIEncoderV2;
//
//import "./inherited/ERC20Token.sol";
//import "./inherited/ERC721Basic.sol";
//
//// Contract: 0x4c94f4ac7f93bd6f145134eae9e127de09176c1d
//
//contract Tournaments {
//
//    struct Tournament {
//        address payable organizer;
//        uint deadline;
//        address token;
//        uint tokenVersion; // The version of the token being used (0 for ETH, 20 for ERC20, 721 for ERC721)
//        uint balance;
//        bool hasPaidOut;
//    }
//
//    uint public numTournaments;
//    mapping(uint => Tournament) public tournaments;
//    mapping (uint => mapping (uint => bool)) public tokenBalances;
//
//    bool public callStarted; // Ensures mutex for the entire contract
//
//    modifier callNotStarted(){
//        require(!callStarted);
//        _;
//    }
//
//    modifier validateTournamentArrayIndex(uint _index) {
//        require(_index < numTournaments);
//        _;
//    }
//
//    modifier onlyOrganizer(address _sender, uint _tournamentId, uint _issuerId) {
//        require(_sender == tournaments[_tournamentId].organizer);
//        _;
//    }
//
//    modifier hasNotPaid(uint _tournamentId) {
//        require(!tournaments[_tournamentId].hasPaidOut);
//        _;
//    }
//
//    function newTournament(
//        address payable _organizer,
//        string memory _data,
//        uint _deadline,
//        address _token,
//        uint _tokenVersion) public payable returns (uint) {
//
//        require(_tokenVersion == 0 || _tokenVersion == 20 || _tokenVersion == 721);
//
//        uint tournamentId = numTournaments;
//
//        Tournament storage tour = tournaments[tournamentId];
//        tour.organizer = _organizer;
//        tour.deadline = _deadline;
//        tour.tokenVersion = _tokenVersion;
//
//        if (_tokenVersion != 0) {
//            tour.token = _token;
//        }
//
//        numTournaments++;
//
//        emit TournamentIssued(
//            tournamentId,
//            _organizer,
//            _data,
//            _deadline,
//            _token,
//            _tokenVersion
//        );
//
//        return (tournamentId);
//    }
//
//    function contribute(uint _tournamentId, uint _amount) public payable
//    validateTournamentArrayIndex(_tournamentId)
//    callNotStarted {
//
//        require(_amount > 0); // Contributions of 0 tokens or token ID 0 should fail
//
//        callStarted = true;
//
//        if (tournaments[_tournamentId].tokenVersion == 0) {
//            require(msg.value == _amount);
//            tournaments[_tournamentId].balance += _amount;
//
//        } else if (tournaments[_tournamentId].tokenVersion == 20) {
//            require(msg.value == 0);
//            require(ERC20Token(tournaments[_tournamentId].token).transferFrom(msg.sender, address(this), _amount));
//            tournaments[_tournamentId].balance += _amount;
//
//        } else if (tournaments[_tournamentId].tokenVersion == 721) {
//            require(msg.value == 0);
//            ERC721BasicToken(tournaments[_tournamentId].token).transferFrom(
//                msg.sender,
//                address(this),
//                _amount);
//            tokenBalances[_tournamentId][_amount] = true;
//        } else {
//            revert();
//        }
//
//        emit ContributionAdded(
//            _tournamentId,
//            msg.sender,
//            _amount
//        );
//
//        callStarted = false;
//    }
//
//    function changeDeadline(uint _tournamentId, uint _issuerId, uint _deadline) external
//    validateTournamentArrayIndex(_tournamentId)
//    onlyOrganizer(msg.sender, _tournamentId, _issuerId) {
//
//        tournaments[_tournamentId].deadline = _deadline;
//        emit TournamentDeadlineChanged(_tournamentId, msg.sender, _deadline);
//    }
//
//    event TournamentIssued(uint _tournamentId, address payable _organizer, string _data, uint _deadline, address _token, uint _tokenVersion);
//    event ContributionAdded(uint _tournamentId, address payable _contributor, uint _amount);
//    event TournamentDeadlineChanged(uint _tournamentId, address _changer, uint _deadline);
//}
