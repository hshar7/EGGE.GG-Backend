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
import org.web3j.tuples.generated.Tuple6;
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
    private static final String BINARY = "608060405234801561001057600080fd5b50610a11806100206000396000f3fe6080604052600436106100705760003560e01c806387121f6e1161004e57806387121f6e146100f45780638c59091714610107578063ebc425c11461011c578063ec41e4cd1461013c57610070565b8063720dc00c146100755780637503e1b7146100a057806385562816146100d2575b600080fd5b34801561008157600080fd5b5061008a61015c565b6040516100979190610864565b60405180910390f35b3480156100ac57600080fd5b506100c06100bb3660046106db565b610165565b6040516100979695949392919061080a565b3480156100de57600080fd5b506100e76101ac565b6040516100979190610878565b6100e7610102366004610628565b6101b2565b61011a6101153660046106f9565b61028a565b005b34801561012857600080fd5b5061011a610137366004610733565b6104f8565b34801561014857600080fd5b5061008a6101573660046106f9565b610589565b60035460ff1681565b60016020819052600091825260409091208054918101546002820154600383015460048401546005909401546001600160a01b039586169593949390921692909160ff1686565b60005481565b60008115806101c15750816014145b806101cd5750816102d1145b6101d657600080fd5b6000805480825260016020819052604090922080546001600160a01b0319166001600160a01b038a1617815591820186905560038201849055908315610234576002810180546001600160a01b0319166001600160a01b0387161790555b6000805460010190556040517fc211a2f664a57c9df96c0657b6c8724c9bffaa8237cd796e4546c83195925752906102779084908b908b908b908b908b90610886565b60405180910390a1509695505050505050565b81600054811061029957600080fd5b60035460ff16156102a957600080fd5b600082116102b657600080fd5b6003805460ff1916600190811782556000858152602091909152604090200154610302578134146102e657600080fd5b60008381526001602052604090206004018054830190556104ae565b600083815260016020526040902060030154601414156103c957341561032757600080fd5b60008381526001602052604090819020600201549051600160e01b6323b872dd0281526001600160a01b03909116906323b872dd9061036e903390309087906004016107e2565b602060405180830381600087803b15801561038857600080fd5b505af115801561039c573d6000803e3d6000fd5b505050506040513d601f19601f820116820180604052506103c091908101906106b5565b6102e657600080fd5b6000838152600160205260409020600301546102d114156100705734156103ef57600080fd5b60008381526001602052604090819020600201549051600160e01b6323b872dd0281526001600160a01b03909116906323b872dd90610436903390309087906004016107e2565b602060405180830381600087803b15801561045057600080fd5b505af1158015610464573d6000803e3d6000fd5b505050506040513d601f19601f8201168201806040525061048891908101906106b5565b5060008381526002602090815260408083208584529091529020805460ff191660011790555b7f75aecd8d57cb4b1b263271bddb4961b993924dd466e6003c254832572d8a57e18333846040516104e1939291906108dc565b60405180910390a150506003805460ff1916905550565b82600054811061050757600080fd5b6000848152600160205260409020543390859085906001600160a01b0316831461053057600080fd5b60008781526001602081905260409182902001869055517feec5568894b8c8fbef5502e62a40b9588382ffab43a642e5fadc8deba22d876190610578908990339089906108ea565b60405180910390a150505050505050565b600260209081526000928352604080842090915290825290205460ff1681565b60006105b58235610961565b9392505050565b60006105b5825161096c565b600082601f8301126105d957600080fd5b81356105ec6105e78261092c565b610905565b9150808252602083016020830185838301111561060857600080fd5b610613838284610991565b50505092915050565b60006105b5823561097d565b600080600080600060a0868803121561064057600080fd5b600061064c88886105a9565b955050602086013567ffffffffffffffff81111561066957600080fd5b610675888289016105c8565b94505060406106868882890161061c565b9350506060610697888289016105a9565b92505060806106a88882890161061c565b9150509295509295909350565b6000602082840312156106c757600080fd5b60006106d384846105bc565b949350505050565b6000602082840312156106ed57600080fd5b60006106d3848461061c565b6000806040838503121561070c57600080fd5b6000610718858561061c565b92505060206107298582860161061c565b9150509250929050565b60008060006060848603121561074857600080fd5b6000610754868661061c565b93505060206107658682870161061c565b92505060406107768682870161061c565b9150509250925092565b61078981610980565b82525050565b61078981610961565b6107898161096c565b60006107ac82610954565b6107b68185610958565b93506107c681856020860161099d565b6107cf816109cd565b9093019392505050565b6107898161097d565b606081016107f08286610780565b6107fd602083018561078f565b6106d360408301846107d9565b60c08101610818828961078f565b61082560208301886107d9565b610832604083018761078f565b61083f60608301866107d9565b61084c60808301856107d9565b61085960a0830184610798565b979650505050505050565b602081016108728284610798565b92915050565b6020810161087282846107d9565b60c0810161089482896107d9565b6108a1602083018861078f565b81810360408301526108b381876107a1565b90506108c260608301866107d9565b6108cf608083018561078f565b61085960a08301846107d9565b606081016107f082866107d9565b606081016108f882866107d9565b6107fd6020830185610780565b60405181810167ffffffffffffffff8111828210171561092457600080fd5b604052919050565b600067ffffffffffffffff82111561094357600080fd5b506020601f91909101601f19160190565b5190565b90815260200190565b600061087282610971565b151590565b6001600160a01b031690565b90565b600061087282600061087282610961565b82818337506000910152565b60005b838110156109b85781810151838201526020016109a0565b838111156109c7576000848401525b50505050565b601f01601f19169056fea265627a7a72305820956f83bb72689f8720ebe9b2f4d19c036676926ee38fbe210a17b2e4229767096c6578706572696d656e74616cf50037";

    public static final String FUNC_CALLSTARTED = "callStarted";

    public static final String FUNC_TOURNAMENTS = "tournaments";

    public static final String FUNC_NUMTOURNAMENTS = "numTournaments";

    public static final String FUNC_NEWTOURNAMENT = "newTournament";

    public static final String FUNC_CONTRIBUTE = "contribute";

    public static final String FUNC_CHANGEDEADLINE = "changeDeadline";

    public static final String FUNC_TOKENBALANCES = "tokenBalances";

    public static final Event TOURNAMENTISSUED_EVENT = new Event("TournamentIssued", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CONTRIBUTIONADDED_EVENT = new Event("ContributionAdded", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event TOURNAMENTDEADLINECHANGED_EVENT = new Event("TournamentDeadlineChanged", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
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

    public RemoteCall<Boolean> callStarted() {
        final Function function = new Function(FUNC_CALLSTARTED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Tuple6<String, BigInteger, String, BigInteger, BigInteger, Boolean>> tournaments(BigInteger param0) {
        final Function function = new Function(FUNC_TOURNAMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}));
        return new RemoteCall<Tuple6<String, BigInteger, String, BigInteger, BigInteger, Boolean>>(
                new Callable<Tuple6<String, BigInteger, String, BigInteger, BigInteger, Boolean>>() {
                    @Override
                    public Tuple6<String, BigInteger, String, BigInteger, BigInteger, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, BigInteger, String, BigInteger, BigInteger, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (BigInteger) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (Boolean) results.get(5).getValue());
                    }
                });
    }

    public RemoteCall<BigInteger> numTournaments() {
        final Function function = new Function(FUNC_NUMTOURNAMENTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> newTournament(String _organizer, String _data, BigInteger _deadline, String _token, BigInteger _tokenVersion, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_NEWTOURNAMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(_organizer), 
                new org.web3j.abi.datatypes.Utf8String(_data), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(_token), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenVersion)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> contribute(BigInteger _tournamentId, BigInteger _amount, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_CONTRIBUTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> changeDeadline(BigInteger _tournamentId, BigInteger _issuerId, BigInteger _deadline) {
        final Function function = new Function(
                FUNC_CHANGEDEADLINE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_issuerId), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline)), 
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
}
