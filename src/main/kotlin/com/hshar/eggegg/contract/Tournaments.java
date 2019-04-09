package com.hshar.eggegg.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.2.0.
 */
public class Tournaments extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b5061108f806100206000396000f3fe60806040526004361061007b5760003560e01c8063855628161161004e57806385562816146101205780638c59091714610135578063b62ea7ff14610148578063ec41e4cd146101685761007b565b80631786e695146100805780636856998f146100a2578063720dc00c146100cb5780637503e1b7146100ed575b600080fd5b34801561008c57600080fd5b506100a061009b366004610c73565b610188565b005b6100b56100b0366004610b11565b610215565b6040516100c29190610e84565b60405180910390f35b3480156100d757600080fd5b506100e061036a565b6040516100c29190610e70565b3480156100f957600080fd5b5061010d610108366004610c03565b610373565b6040516100c29796959493929190610e08565b34801561012c57600080fd5b506100b56103c3565b6100a0610143366004610c73565b6103c9565b34801561015457600080fd5b506100a0610163366004610c21565b61063f565b34801561017457600080fd5b506100e0610183366004610c73565b6107ab565b81600054811061019757600080fd5b600083815260016020526040902054339084906001600160a01b031682146101be57600080fd5b60008581526001602081905260409182902001859055517feec5568894b8c8fbef5502e62a40b9588382ffab43a642e5fadc8deba22d87619061020690879033908890610f24565b60405180910390a15050505050565b60008315806102245750836014145b806102305750836102d1145b61023957600080fd5b8282511461024657600080fd5b6000805b83518110156102785783818151811061025f57fe5b602002602001015182019150808060010191505061024a565b508060641461028657600080fd5b6000805480825260016020818152604090932080546001600160a01b0319166001600160a01b038e161781559081018a9055600381018890558551919290916102d791600684019190880190610940565b5060028101805460ff19169055861561030d57600281018054610100600160a81b0319166101006001600160a01b038b16021790555b6000805460010190556040517faecb8e7072b74e2d3974126acf1227204832e949c111820a1965ef5f43e90aa3906103549084908e908e908e908e908e908e908e90610e92565b60405180910390a1509998505050505050505050565b60035460ff1681565b60016020819052600091825260409091208054918101546002820154600383015460048401546005909401546001600160a01b0395861695939460ff808516956101009095049094169390911687565b60005481565b8160005481106103d857600080fd5b60035460ff16156103e857600080fd5b600082116103f557600080fd5b6003805460ff19166001908117825560008581526020919091526040902001546104415781341461042557600080fd5b60008381526001602052604090206004018054830190556105f5565b6000838152600160205260409020600301546014141561050c57341561046657600080fd5b60008381526001602052604090819020600201549051600160e01b6323b872dd0281526101009091046001600160a01b0316906323b872dd906104b190339030908790600401610dc5565b602060405180830381600087803b1580156104cb57600080fd5b505af11580156104df573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506105039190810190610bdd565b61042557600080fd5b6000838152600160205260409020600301546102d1141561007b57341561053257600080fd5b60008381526001602052604090819020600201549051600160e01b6323b872dd0281526101009091046001600160a01b0316906323b872dd9061057d90339030908790600401610dc5565b602060405180830381600087803b15801561059757600080fd5b505af11580156105ab573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506105cf9190810190610bdd565b5060008381526002602090815260408083208584529091529020805460ff191660011790555b7f75aecd8d57cb4b1b263271bddb4961b993924dd466e6003c254832572d8a57e183338460405161062893929190610f16565b60405180910390a150506003805460ff1916905550565b60035460ff161561064f57600080fd5b600082815260016020526040902060020154829060ff1661066f57600080fd5b600083815260016020526040902054339084906001600160a01b0316821461069657600080fd5b8460005481106106a557600080fd5b6003805460ff19166001908117909155600087815260209190915260409020600601548551146106d457600080fd5b60005b8551811015610745576000878152600160205260408120600481015460069091018054606491908590811061070857fe5b90600052602060002001548161071a57fe5b0402905061073c8888848151811061072e57fe5b6020026020010151836107cb565b506001016106d7565b5060008681526001602052604090819020600201805460ff19169055517f4293cd82d345ce04fd3495d8cc9a0ae413860e4c1904319a8bd1ef431a1ca8c5906107919088908890610f3f565b60405180910390a150506003805460ff1916905550505050565b600260209081526000928352604080842090915290825290205460ff1681565b60008381526001602052604090206003015461083f57600081116107ee57600080fd5b60008381526001602052604080822060040180548490039055516001600160a01b0384169183156108fc02918491818181858888f19350505050158015610839573d6000803e3d6000fd5b5061093b565b6000838152600160205260409020600301546014141561091b576000811161086657600080fd5b600083815260016020526040908190206004808201805485900390556002909101549151600160e01b63a9059cbb0281526101009092046001600160a01b03169163a9059cbb916108bb918691869101610ded565b602060405180830381600087803b1580156108d557600080fd5b505af11580156108e9573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061090d9190810190610bdd565b61091657600080fd5b61093b565b6000838152600160205260409020600301546102d1141561007b57600080fd5b505050565b82805482825590600052602060002090810192821561097b579160200282015b8281111561097b578251825591602001919060010190610960565b5061098792915061098b565b5090565b6109a591905b808211156109875760008155600101610991565b90565b60006109b48235610fe2565b9392505050565b600082601f8301126109cc57600080fd5b81356109df6109da82610f86565b610f5f565b91508181835260208401935060208101905083856020840282011115610a0457600080fd5b60005b83811015610a305781610a1a88826109a8565b8452506020928301929190910190600101610a07565b5050505092915050565b600082601f830112610a4b57600080fd5b8135610a596109da82610f86565b91508181835260208401935060208101905083856020840282011115610a7e57600080fd5b60005b83811015610a305781610a948882610b05565b8452506020928301929190910190600101610a81565b60006109b48251610fed565b600082601f830112610ac757600080fd5b8135610ad56109da82610fa7565b91508082526020830160208301858383011115610af157600080fd5b610afc83828461100f565b50505092915050565b60006109b482356109a5565b600080600080600080600060e0888a031215610b2c57600080fd5b6000610b388a8a6109a8565b975050602088013567ffffffffffffffff811115610b5557600080fd5b610b618a828b01610ab6565b9650506040610b728a828b01610b05565b9550506060610b838a828b016109a8565b9450506080610b948a828b01610b05565b93505060a0610ba58a828b01610b05565b92505060c088013567ffffffffffffffff811115610bc257600080fd5b610bce8a828b01610a3a565b91505092959891949750929550565b600060208284031215610bef57600080fd5b6000610bfb8484610aaa565b949350505050565b600060208284031215610c1557600080fd5b6000610bfb8484610b05565b60008060408385031215610c3457600080fd5b6000610c408585610b05565b925050602083013567ffffffffffffffff811115610c5d57600080fd5b610c69858286016109bb565b9150509250929050565b60008060408385031215610c8657600080fd5b6000610c928585610b05565b9250506020610c6985828601610b05565b6000610caf8383610cd2565b505060200190565b6000610caf8383610dbc565b610ccc81610ffe565b82525050565b610ccc81610fe2565b6000610ce682610fd5565b610cf08185610fd9565b9350610cfb83610fcf565b60005b82811015610d2657610d11868351610ca3565b9550610d1c82610fcf565b9150600101610cfe565b5093949350505050565b6000610d3b82610fd5565b610d458185610fd9565b9350610d5083610fcf565b60005b82811015610d2657610d66868351610cb7565b9550610d7182610fcf565b9150600101610d53565b610ccc81610fed565b6000610d8f82610fd5565b610d998185610fd9565b9350610da981856020860161101b565b610db28161104b565b9093019392505050565b610ccc816109a5565b60608101610dd38286610cc3565b610de06020830185610cd2565b610bfb6040830184610dbc565b60408101610dfb8285610cc3565b6109b46020830184610dbc565b60e08101610e16828a610cd2565b610e236020830189610dbc565b610e306040830188610d7b565b610e3d6060830187610cd2565b610e4a6080830186610dbc565b610e5760a0830185610dbc565b610e6460c0830184610d7b565b98975050505050505050565b60208101610e7e8284610d7b565b92915050565b60208101610e7e8284610dbc565b6101008101610ea1828b610dbc565b610eae602083018a610cd2565b8181036040830152610ec08189610d84565b9050610ecf6060830188610dbc565b610edc6080830187610cd2565b610ee960a0830186610dbc565b610ef660c0830185610dbc565b81810360e0830152610f088184610d30565b9a9950505050505050505050565b60608101610dd38286610dbc565b60608101610f328286610dbc565b610de06020830185610cc3565b60408101610f4d8285610dbc565b8181036020830152610bfb8184610cdb565b60405181810167ffffffffffffffff81118282101715610f7e57600080fd5b604052919050565b600067ffffffffffffffff821115610f9d57600080fd5b5060209081020190565b600067ffffffffffffffff821115610fbe57600080fd5b506020601f91909101601f19160190565b60200190565b5190565b90815260200190565b6000610e7e82610ff2565b151590565b6001600160a01b031690565b6000610e7e826000610e7e82610fe2565b82818337506000910152565b60005b8381101561103657818101518382015260200161101e565b83811115611045576000848401525b50505050565b601f01601f19169056fea265627a7a72305820d5d46095e887ea4de1e8de7728bdd1b135768210f2582ec1d737fa59df0089096c6578706572696d656e74616cf50037";

    public static final String FUNC_CHANGEDEADLINE = "changeDeadline";

    public static final String FUNC_NEWTOURNAMENT = "newTournament";

    public static final String FUNC_CALLSTARTED = "callStarted";

    public static final String FUNC_TOURNAMENTS = "tournaments";

    public static final String FUNC_NUMTOURNAMENTS = "numTournaments";

    public static final String FUNC_CONTRIBUTE = "contribute";

    public static final String FUNC_PAYOUTWINNERS = "payoutWinners";

    public static final String FUNC_TOKENBALANCES = "tokenBalances";

    public static final Event TOURNAMENTISSUED_EVENT = new Event("TournamentIssued", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
    ;

    public static final Event CONTRIBUTIONADDED_EVENT = new Event("ContributionAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOURNAMENTDEADLINECHANGED_EVENT = new Event("TournamentDeadlineChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOURNAMENTFINALIZED_EVENT = new Event("TournamentFinalized", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Address>>() {}));
    ;

    @Deprecated
    protected Tournaments(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Tournaments(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Tournaments(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Tournaments(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> changeDeadline(BigInteger _tournamentId, BigInteger _deadline) {
        final Function function = new Function(
                FUNC_CHANGEDEADLINE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> newTournament(String _organizer, String _data, BigInteger _deadline, String _token, BigInteger _tokenVersion, BigInteger _maxPlayers, List<BigInteger> _prizeDistribution, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_NEWTOURNAMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_organizer), 
                new org.web3j.abi.datatypes.Utf8String(_data), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_token), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenVersion), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxPlayers), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_prizeDistribution, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<Boolean> callStarted() {
        final Function function = new Function(FUNC_CALLSTARTED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, Boolean>> tournaments(BigInteger param0) {
        final Function function = new Function(FUNC_TOURNAMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, Boolean>>(
                new Callable<Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (Boolean) results.get(6).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> numTournaments() {
        final Function function = new Function(FUNC_NUMTOURNAMENTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> contribute(BigInteger _tournamentId, BigInteger _amount, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_CONTRIBUTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> payoutWinners(BigInteger _tournamentId, List<String> _winners) {
        final Function function = new Function(
                FUNC_PAYOUTWINNERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_winners, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> tokenBalances(BigInteger param0, BigInteger param1) {
        final Function function = new Function(FUNC_TOKENBALANCES, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public List<TournamentIssuedEventResponse> getTournamentIssuedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOURNAMENTISSUED_EVENT, transactionReceipt);
        ArrayList<TournamentIssuedEventResponse> responses = new ArrayList<TournamentIssuedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TournamentIssuedEventResponse typedResponse = new TournamentIssuedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._organizer = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._data = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._deadline = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._token = (String) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse._tokenVersion = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse._maxPlayers = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse._prizeDistribution = (List<BigInteger>) eventValues.getNonIndexedValues().get(7).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TournamentIssuedEventResponse> tournamentIssuedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TournamentIssuedEventResponse>() {
            @Override
            public TournamentIssuedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TOURNAMENTISSUED_EVENT, log);
                TournamentIssuedEventResponse typedResponse = new TournamentIssuedEventResponse();
                typedResponse.log = log;
                typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._organizer = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._data = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._deadline = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._token = (String) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse._tokenVersion = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
                typedResponse._maxPlayers = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
                typedResponse._prizeDistribution = (List<BigInteger>) eventValues.getNonIndexedValues().get(7).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TournamentIssuedEventResponse> tournamentIssuedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOURNAMENTISSUED_EVENT));
        return tournamentIssuedEventFlowable(filter);
    }

    public List<ContributionAddedEventResponse> getContributionAddedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONTRIBUTIONADDED_EVENT, transactionReceipt);
        ArrayList<ContributionAddedEventResponse> responses = new ArrayList<ContributionAddedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContributionAddedEventResponse typedResponse = new ContributionAddedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContributionAddedEventResponse> contributionAddedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ContributionAddedEventResponse>() {
            @Override
            public ContributionAddedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRIBUTIONADDED_EVENT, log);
                ContributionAddedEventResponse typedResponse = new ContributionAddedEventResponse();
                typedResponse.log = log;
                typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContributionAddedEventResponse> contributionAddedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRIBUTIONADDED_EVENT));
        return contributionAddedEventFlowable(filter);
    }

    public List<TournamentDeadlineChangedEventResponse> getTournamentDeadlineChangedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOURNAMENTDEADLINECHANGED_EVENT, transactionReceipt);
        ArrayList<TournamentDeadlineChangedEventResponse> responses = new ArrayList<TournamentDeadlineChangedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TournamentDeadlineChangedEventResponse typedResponse = new TournamentDeadlineChangedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._changer = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._deadline = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TournamentDeadlineChangedEventResponse> tournamentDeadlineChangedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TournamentDeadlineChangedEventResponse>() {
            @Override
            public TournamentDeadlineChangedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TOURNAMENTDEADLINECHANGED_EVENT, log);
                TournamentDeadlineChangedEventResponse typedResponse = new TournamentDeadlineChangedEventResponse();
                typedResponse.log = log;
                typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._changer = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._deadline = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TournamentDeadlineChangedEventResponse> tournamentDeadlineChangedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOURNAMENTDEADLINECHANGED_EVENT));
        return tournamentDeadlineChangedEventFlowable(filter);
    }

    public List<TournamentFinalizedEventResponse> getTournamentFinalizedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(TOURNAMENTFINALIZED_EVENT, transactionReceipt);
        ArrayList<TournamentFinalizedEventResponse> responses = new ArrayList<TournamentFinalizedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TournamentFinalizedEventResponse typedResponse = new TournamentFinalizedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._winners = (List<String>) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<TournamentFinalizedEventResponse> tournamentFinalizedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, TournamentFinalizedEventResponse>() {
            @Override
            public TournamentFinalizedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(TOURNAMENTFINALIZED_EVENT, log);
                TournamentFinalizedEventResponse typedResponse = new TournamentFinalizedEventResponse();
                typedResponse.log = log;
                typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._winners = (List<String>) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<TournamentFinalizedEventResponse> tournamentFinalizedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TOURNAMENTFINALIZED_EVENT));
        return tournamentFinalizedEventFlowable(filter);
    }

    @Deprecated
    public static Tournaments load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Tournaments(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Tournaments load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Tournaments(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Tournaments load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Tournaments(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Tournaments load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Tournaments(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Tournaments> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Tournaments.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Tournaments> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Tournaments.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<Tournaments> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Tournaments.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Tournaments> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Tournaments.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class TournamentIssuedEventResponse {
        public Log log;

        public BigInteger _tournamentId;

        public String _organizer;

        public String _data;

        public BigInteger _deadline;

        public String _token;

        public BigInteger _tokenVersion;

        public BigInteger _maxPlayers;

        public List<BigInteger> _prizeDistribution;
    }

    public static class ContributionAddedEventResponse {
        public Log log;

        public BigInteger _tournamentId;

        public String _contributor;

        public BigInteger _amount;
    }

    public static class TournamentDeadlineChangedEventResponse {
        public Log log;

        public BigInteger _tournamentId;

        public String _changer;

        public BigInteger _deadline;
    }

    public static class TournamentFinalizedEventResponse {
        public Log log;

        public BigInteger _tournamentId;

        public List<String> _winners;
    }
}
