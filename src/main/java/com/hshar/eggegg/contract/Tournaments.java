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
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
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
 * <p>Generated with web3j version 4.5.4.
 */
@SuppressWarnings("rawtypes")
public class Tournaments extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b50610dba806100206000396000f3fe60806040526004361061007b5760003560e01c8063855628161161004e57806385562816146102bc5780638c590917146102d1578063b62ea7ff146102f4578063c5e95b6f146103ab5761007b565b80631786e695146100805780636856998f146100b2578063720dc00c1461021f5780637503e1b714610248575b600080fd5b34801561008c57600080fd5b506100b0600480360360408110156100a357600080fd5b50803590602001356103db565b005b61020d600480360360e08110156100c857600080fd5b6001600160a01b0382351691908101906040810160208201356401000000008111156100f357600080fd5b82018360208201111561010557600080fd5b8035906020019184600183028401116401000000008311171561012757600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092958435956001600160a01b036020870135169560408101359550606081013594509192509060a08101906080013564010000000081111561019b57600080fd5b8201836020820111156101ad57600080fd5b803590602001918460208302840111640100000000831117156101cf57600080fd5b91908080602002602001604051908101604052809392919081815260200183836020028082843760009201919091525092955061046f945050505050565b60408051918252519081900360200190f35b34801561022b57600080fd5b506102346106ed565b604080519115158252519081900360200190f35b34801561025457600080fd5b506102726004803603602081101561026b57600080fd5b50356106f6565b604080516001600160a01b039889168152602081019790975294151586860152929095166060850152608084015260a083019390935260c082019290925290519081900360e00190f35b3480156102c857600080fd5b5061020d610743565b6100b0600480360360408110156102e757600080fd5b5080359060200135610749565b34801561030057600080fd5b506100b06004803603604081101561031757600080fd5b8135919081019060408101602082013564010000000081111561033957600080fd5b82018360208201111561034b57600080fd5b8035906020019184602083028401116401000000008311171561036d57600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550610912945050505050565b3480156103b757600080fd5b5061020d600480360360408110156103ce57600080fd5b5080359060200135610b7b565b8160005481106103ea57600080fd5b600083815260016020526040902054339084906001600160a01b0316821461041157600080fd5b6000858152600160208181526040928390209091018690558151878152339181019190915280820186905290517feec5568894b8c8fbef5502e62a40b9588382ffab43a642e5fadc8deba22d87619181900360600190a15050505050565b600083158061047e5750836014145b8061048a5750836102d1145b61049357600080fd5b828251146104a057600080fd5b6000805b83518110156104d2578381815181106104b957fe5b60200260200101518201915080806001019150506104a4565b50806064146104e057600080fd5b60008054905060006001600083815260200190815260200160002090508a8160000160006101000a8154816001600160a01b0302191690836001600160a01b031602179055508881600101819055508681600301819055508460026000848152602001908152602001600020908051906020019061055f929190610d1d565b5060028101805460ff1916600117905560058101869055861561059f57600281018054610100600160a81b0319166101006001600160a01b038b16021790555b60008081548092919060010191905055507faecb8e7072b74e2d3974126acf1227204832e949c111820a1965ef5f43e90aa3828c8c8c8c8c8c8c60405180898152602001886001600160a01b03166001600160a01b0316815260200180602001878152602001866001600160a01b03166001600160a01b0316815260200185815260200184815260200180602001838103835289818151815260200191508051906020019080838360005b8381101561066257818101518382015260200161064a565b50505050905090810190601f16801561068f5780820380516001836020036101000a031916815260200191505b508381038252845181528451602091820191808701910280838360005b838110156106c45781810151838201526020016106ac565b505050509050019a505050505050505050505060405180910390a1509998505050505050505050565b60035460ff1681565b60016020819052600091825260409091208054918101546002820154600383015460048401546005909401546001600160a01b0395861695939460ff841694610100909404909316929087565b60005481565b81600054811061075857600080fd5b600083815260016020526040902060020154839060ff1661077857600080fd5b60035460ff161561078857600080fd5b6000831161079557600080fd5b6003805460ff19166001908117825560008681526020919091526040902001546107e1578234146107c557600080fd5b60008481526001602052604090206004018054840190556108c3565b600084815260016020526040902060030154601414156108a757341561080657600080fd5b60008481526001602090815260408083206002015481516323b872dd60e01b81523360048201523060248201526044810188905291516101009091046001600160a01b0316936323b872dd93606480850194919392918390030190829087803b15801561087257600080fd5b505af1158015610886573d6000803e3d6000fd5b505050506040513d602081101561089c57600080fd5b50516107c557600080fd5b6000848152600160205260409020600301546102d1141561007b575b6040805185815233602082015280820185905290517f75aecd8d57cb4b1b263271bddb4961b993924dd466e6003c254832572d8a57e19181900360600190a150506003805460ff191690555050565b60035460ff161561092257600080fd5b600082815260016020526040902060020154829060ff1661094257600080fd5b600083815260016020526040902054339084906001600160a01b0316821461096957600080fd5b84600054811061097857600080fd5b6003805460ff191660011790556000868152600260205260409020548551146109a057600080fd5b606085516040519080825280602002602001820160405280156109cd578160200160208202803883390190505b50905060005b8651811015610a4057600088815260016020908152604080832060040154600290925282208054606492919085908110610a0957fe5b90600052602060002001540281610a1c57fe5b04905080838381518110610a2c57fe5b6020908102919091010152506001016109d3565b5060005b8151811015610a8657610a7e88888381518110610a5d57fe5b6020026020010151848481518110610a7157fe5b6020026020010151610ba9565b600101610a44565b506000878152600160209081526040808320600201805460ff1916905580518a815260608184018181528b51918301919091528a517fc1b60f03de0142802eef2afaff02546ce5ced8da57476a195b503fab5f71472a958d958d95899594939085019260808601928881019202908190849084905b83811015610b13578181015183820152602001610afb565b50505050905001838103825284818151815260200191508051906020019060200280838360005b83811015610b52578181015183820152602001610b3a565b505050509050019550505050505060405180910390a150506003805460ff191690555050505050565b60026020528160005260406000208181548110610b9457fe5b90600052602060002001600091509150505481565b600083815260016020526040902060030154610c1d5760008111610bcc57610d18565b60008381526001602052604080822060040180548490039055516001600160a01b0384169183156108fc02918491818181858888f19350505050158015610c17573d6000803e3d6000fd5b50610d18565b60008381526001602052604090206003015460141415610cf85760008111610c4457610d18565b6000838152600160209081526040808320600480820180548790039055600290910154825163a9059cbb60e01b81526001600160a01b03888116938201939093526024810187905292516101009091049091169363a9059cbb93604480850194919392918390030190829087803b158015610cbe57600080fd5b505af1158015610cd2573d6000803e3d6000fd5b505050506040513d6020811015610ce857600080fd5b5051610cf357600080fd5b610d18565b6000838152600160205260409020600301546102d1141561007b57600080fd5b505050565b828054828255906000526020600020908101928215610d58579160200282015b82811115610d58578251825591602001919060010190610d3d565b50610d64929150610d68565b5090565b610d8291905b80821115610d645760008155600101610d6e565b9056fea265627a7a723158204545e5e347be433a88778877247740af3f4a2a0fed5a2647db0d32f72454a54e64736f6c634300050b0032";

    public static final String FUNC_CHANGEDEADLINE = "changeDeadline";

    public static final String FUNC_NEWTOURNAMENT = "newTournament";

    public static final String FUNC_CALLSTARTED = "callStarted";

    public static final String FUNC_TOURNAMENTS = "tournaments";

    public static final String FUNC_NUMTOURNAMENTS = "numTournaments";

    public static final String FUNC_CONTRIBUTE = "contribute";

    public static final String FUNC_PAYOUTWINNERS = "payoutWinners";

    public static final String FUNC_PRIZEDISTRIBUTIONS = "prizeDistributions";

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
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Address>>() {}, new TypeReference<DynamicArray<Uint256>>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> changeDeadline(BigInteger _tournamentId, BigInteger _deadline) {
        final Function function = new Function(
                FUNC_CHANGEDEADLINE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> newTournament(String _organizer, String _data, BigInteger _deadline, String _token, BigInteger _tokenVersion, BigInteger _maxPlayers, List<BigInteger> _prizeDistribution, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_NEWTOURNAMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _organizer), 
                new org.web3j.abi.datatypes.Utf8String(_data), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(160, _token), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenVersion), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxPlayers), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_prizeDistribution, org.web3j.abi.datatypes.generated.Uint256.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<Boolean> callStarted() {
        final Function function = new Function(FUNC_CALLSTARTED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger>> tournaments(BigInteger param0) {
        final Function function = new Function(FUNC_TOURNAMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> numTournaments() {
        final Function function = new Function(FUNC_NUMTOURNAMENTS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> contribute(BigInteger _tournamentId, BigInteger _amount, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_CONTRIBUTE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_amount)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> payoutWinners(BigInteger _tournamentId, List<String> _winners) {
        final Function function = new Function(
                FUNC_PAYOUTWINNERS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.Address>(
                        org.web3j.abi.datatypes.Address.class,
                        org.web3j.abi.Utils.typeMap(_winners, org.web3j.abi.datatypes.Address.class))), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> prizeDistributions(BigInteger param0, BigInteger param1) {
        final Function function = new Function(FUNC_PRIZEDISTRIBUTIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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
            typedResponse.payouts = (List<BigInteger>) eventValues.getNonIndexedValues().get(2).getValue();
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
                typedResponse.payouts = (List<BigInteger>) eventValues.getNonIndexedValues().get(2).getValue();
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

    public static class TournamentIssuedEventResponse extends BaseEventResponse {
        public BigInteger _tournamentId;

        public String _organizer;

        public String _data;

        public BigInteger _deadline;

        public String _token;

        public BigInteger _tokenVersion;

        public BigInteger _maxPlayers;

        public List<BigInteger> _prizeDistribution;
    }

    public static class ContributionAddedEventResponse extends BaseEventResponse {
        public BigInteger _tournamentId;

        public String _contributor;

        public BigInteger _amount;
    }

    public static class TournamentDeadlineChangedEventResponse extends BaseEventResponse {
        public BigInteger _tournamentId;

        public String _changer;

        public BigInteger _deadline;
    }

    public static class TournamentFinalizedEventResponse extends BaseEventResponse {
        public BigInteger _tournamentId;

        public List<String> _winners;

        public List<BigInteger> payouts;
    }
}
