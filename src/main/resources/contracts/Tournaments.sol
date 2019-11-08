pragma solidity 0.5.7;

import "./inherited/ERC20Token.sol";
import "./inherited/ERC721Basic.sol";

contract Tournaments {

    struct Tournament {
        address payable organizer;
        uint deadline;
        bool active;
        address token;
        uint tokenVersion; // The version of the token being used (0 for ETH, 20 for ERC20, 721 for ERC721)
        uint balance;
        uint maxPlayers;
        string platform;
        bool restrictContributors;
    }

    address superAdmin = 0xB6E58769550608DEF3043DCcbBE1Fa653af23151;
    mapping(address => string[]) public organizerPlatforms;

    uint public numTournaments;
    mapping(uint => Tournament) public tournaments;
    mapping(uint => address[]) public tournamentContributors;
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

    modifier onlySuperAdmin(address _sender) {
        require(_sender == superAdmin);
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

    function whitelistOrganizer(address _organizer, string memory _platform) public onlySuperAdmin(msg.sender) {
        organizerPlatforms[_organizer][organizerPlatforms[_organizer].length - 1] = _platform;
        emit OrganizerWhitelisted(_platform, _organizer);
    }

    function whitelistContributor(uint _tournamentId, address _newContributor) public onlyOrganizer(msg.sender, _tournamentId) {
        tournamentContributors[_tournamentId][tournamentContributors[_tournamentId].length - 1] = _newContributor;
        emit ContributorWhitelisted(_tournamentId, _newContributor);
    }

    function newTournament(
        string memory _data,
        uint _deadline,
        address _token,
        uint _tokenVersion,
        uint _maxPlayers,
        uint[] memory _prizeDistribution,
        string memory _platform,
        bool _restrictContributors) public payable returns (uint) {


        require(_tokenVersion == 0 || _tokenVersion == 20);
        require (_prizeDistribution.length == _maxPlayers);

        if (!equals(_platform, "default")) {
            bool allowed = false;
            for (uint i = 0; i <= organizerPlatforms[msg.sender].length; i++) {
                if (equals(organizerPlatforms[msg.sender][i], _platform)) {
                    allowed = true;
                }
            }
            if (!allowed) { revert(); }
        }

        uint total = 0;
        for (uint i=0; i < _prizeDistribution.length; i++) {
            total += _prizeDistribution[i];
        }
        require (total == 100);

        uint tournamentId = numTournaments;

        tournaments[tournamentId] = Tournament({
            organizer: msg.sender,
            deadline: _deadline,
            tokenVersion: _tokenVersion,
            active: true,
            maxPlayers: _maxPlayers,
            platform: _platform,
            restrictContributors: _restrictContributors,
            token: _token,
            balance: 0
        });
        tournamentContributors[tournamentId] = [msg.sender];
        prizeDistributions[tournamentId] = _prizeDistribution;

        numTournaments++;

        emit TournamentIssued(
            tournamentId,
            msg.sender,
            _data,
            _deadline,
            _token,
            _tokenVersion,
            _maxPlayers,
            _prizeDistribution,
            _platform,
            _restrictContributors
        );

        return (tournamentId);
    }

    function contribute(uint _tournamentId, uint _amount) public payable
    validateTournamentArrayIndex(_tournamentId)
    tournamentActive(_tournamentId)
    callNotStarted {

        require(_amount > 0); // Contributions of 0 tokens or token ID 0 should fail

        if (tournaments[_tournamentId].restrictContributors == true) {
            bool contributorAllowed = false;
            for (uint i = 0; i < tournamentContributors[_tournamentId].length; i++) {
                if (tournamentContributors[_tournamentId][i] == msg.sender) {
                    contributorAllowed = true;
                }
            }
            if (!contributorAllowed) { revert(); }
        }

        callStarted = true;

        if (tournaments[_tournamentId].tokenVersion == 0) {
            require(msg.value == _amount);
            tournaments[_tournamentId].balance += _amount;
        } else if (tournaments[_tournamentId].tokenVersion == 20) {
            require(msg.value == 0);
            require(ERC20Token(tournaments[_tournamentId].token).transferFrom(msg.sender, address(this), _amount));
            tournaments[_tournamentId].balance += _amount;
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

    function equals(string memory a, string memory b) internal pure returns (bool) {
        if (bytes(a).length != bytes(b).length) {
            return false;
        } else {
            return keccak256(abi.encodePacked(a)) == keccak256(abi.encodePacked(b));
        }
    }

    event TournamentIssued(uint _tournamentId, address payable _organizer, string _data, uint _deadline, address _token, uint _tokenVersion, uint _maxPlayers, uint[] _prizeDistribution, string _platform, bool _restrictContributors);
    event ContributionAdded(uint _tournamentId, address payable _contributor, uint _amount);
    event TournamentDeadlineChanged(uint _tournamentId, address _changer, uint _deadline);
    event TournamentFinalized(uint _tournamentId, address payable[] _winners, uint[] payouts);
    event ContributorWhitelisted(uint _tournamentId, address _contributor);
    event OrganizerWhitelisted(string _platform, address _organizer);
}
