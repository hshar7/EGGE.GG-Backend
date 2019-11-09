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
import org.web3j.tuples.generated.Tuple9;
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
    private static final String BINARY = "6080604052600080546001600160a01b03191673b6e58769550608def3043dccbbe1fa653af2315117905534801561003657600080fd5b506118e8806100466000396000f3fe6080604052600436106100a75760003560e01c8063855628161161006457806385562816146104e85780638c590917146104fd578063b62ea7ff14610520578063c5e95b6f146105d5578063ccc61ab814610605578063e0aad7f8146106b3576100a7565b8063083da4b5146100ac5780631786e695146100e757806339abdf09146101175780635e9db8fe146102f8578063720dc00c146103b95780637503e1b7146103e2575b600080fd5b3480156100b857600080fd5b506100e5600480360360408110156100cf57600080fd5b50803590602001356001600160a01b03166106ff565b005b3480156100f357600080fd5b506100e56004803603604081101561010a57600080fd5b50803590602001356107b0565b6102e6600480360361010081101561012e57600080fd5b810190602081018135600160201b81111561014857600080fd5b82018360208201111561015a57600080fd5b803590602001918460018302840111600160201b8311171561017b57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092958435956001600160a01b036020870135169560408101359550606081013594509192509060a081019060800135600160201b8111156101ee57600080fd5b82018360208201111561020057600080fd5b803590602001918460208302840111600160201b8311171561022157600080fd5b9190808060200260200160405190810160405280939291908181526020018383602002808284376000920191909152509295949360208101935035915050600160201b81111561027057600080fd5b82018360208201111561028257600080fd5b803590602001918460018302840111600160201b831117156102a357600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295505050503515159050610844565b60408051918252519081900360200190f35b34801561030457600080fd5b506100e56004803603604081101561031b57600080fd5b6001600160a01b038235169190810190604081016020820135600160201b81111561034557600080fd5b82018360208201111561035757600080fd5b803590602001918460018302840111600160201b8311171561037857600080fd5b91908080601f016020809104026020016040519081016040528093929190818152602001838380828437600092019190915250929550610d22945050505050565b3480156103c557600080fd5b506103ce610e3c565b604080519115158252519081900360200190f35b3480156103ee57600080fd5b5061040c6004803603602081101561040557600080fd5b5035610e45565b604051808a6001600160a01b03166001600160a01b0316815260200189815260200188151515158152602001876001600160a01b03166001600160a01b031681526020018681526020018581526020018481526020018060200183151515158152602001828103825284818151815260200191508051906020019080838360005b838110156104a557818101518382015260200161048d565b50505050905090810190601f1680156104d25780820380516001836020036101000a031916815260200191505b509a505050505050505050505060405180910390f35b3480156104f457600080fd5b506102e6610f2a565b6100e56004803603604081101561051357600080fd5b5080359060200135610f30565b34801561052c57600080fd5b506100e56004803603604081101561054357600080fd5b81359190810190604081016020820135600160201b81111561056457600080fd5b82018360208201111561057657600080fd5b803590602001918460208302840111600160201b8311171561059757600080fd5b919080806020026020016040519081016040528093929190818152602001838360200280828437600092019190915250929550611169945050505050565b3480156105e157600080fd5b506102e6600480360360408110156105f857600080fd5b50803590602001356113d2565b34801561061157600080fd5b5061063e6004803603604081101561062857600080fd5b506001600160a01b038135169060200135611400565b6040805160208082528351818301528351919283929083019185019080838360005b83811015610678578181015183820152602001610660565b50505050905090810190601f1680156106a55780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b3480156106bf57600080fd5b506106e3600480360360408110156106d657600080fd5b50803590602001356114b4565b604080516001600160a01b039092168252519081900360200190f35b600082815260036020526040902054339083906001600160a01b0316821461072657600080fd5b60008481526004602052604090208054849190600019810190811061074757fe5b60009182526020918290200180546001600160a01b0319166001600160a01b03938416179055604080518781529286169183019190915280517f37e6eb6772cbcbab66b167a5d504664d1f8599e1d2c1213e9ff77cef3de0c2489281900390910190a150505050565b8160025481106107bf57600080fd5b600083815260036020526040902054339084906001600160a01b031682146107e657600080fd5b6000858152600360209081526040918290206001018690558151878152339181019190915280820186905290517feec5568894b8c8fbef5502e62a40b9588382ffab43a642e5fadc8deba22d87619181900360600190a15050505050565b60008515806108535750856014145b61085c57600080fd5b8484511461086957600080fd5b6108928360405180604001604052806007815260200166191959985d5b1d60ca1b8152506114e9565b610988576000805b33600090815260016020526040902054811161097b5733600090815260016020526040902080546109699190839081106108d057fe5b600091825260209182902001805460408051601f600260001961010060018716150201909416939093049283018590048502810185019091528181529283018282801561095e5780601f106109335761010080835404028352916020019161095e565b820191906000526020600020905b81548152906001019060200180831161094157829003601f168201915b5050505050866114e9565b1561097357600191505b60010161089a565b508061098657600080fd5b505b6000805b85518110156109ba578581815181106109a157fe5b602002602001015182019150808060010191505061098c565b50806064146109c857600080fd5b60006002549050604051806101200160405280336001600160a01b031681526020018b81526020016001151581526020018a6001600160a01b03168152602001898152602001600081526020018881526020018681526020018515158152506003600083815260200190815260200160002060008201518160000160006101000a8154816001600160a01b0302191690836001600160a01b031602179055506020820151816001015560408201518160020160006101000a81548160ff02191690831515021790555060608201518160020160016101000a8154816001600160a01b0302191690836001600160a01b031602179055506080820151816003015560a0820151816004015560c0820151816005015560e0820151816006019080519060200190610af8929190611759565b5061010091909101516007909101805460ff191691151591909117905560408051602080820183523382526000848152600490915291909120610b3c9160016117d7565b5060008181526005602090815260409091208751610b5c92890190611838565b506002600081548092919060010191905055507f4450af4cb1202f5837b340eb29fb835c25419ceee1275d540351315306a6278a81338d8d8d8d8d8d8d8d604051808b81526020018a6001600160a01b03166001600160a01b0316815260200180602001898152602001886001600160a01b03166001600160a01b0316815260200187815260200186815260200180602001806020018515151515815260200184810384528c818151815260200191508051906020019080838360005b83811015610c31578181015183820152602001610c19565b50505050905090810190601f168015610c5e5780820380516001836020036101000a031916815260200191505b508481038352875181528751602091820191808a01910280838360005b83811015610c93578181015183820152602001610c7b565b50505050905001848103825286818151815260200191508051906020019080838360005b83811015610ccf578181015183820152602001610cb7565b50505050905090810190601f168015610cfc5780820380516001836020036101000a031916815260200191505b509d505050505050505050505050505060405180910390a19a9950505050505050505050565b60005433906001600160a01b03168114610d3b57600080fd5b6001600160a01b038316600090815260016020526040902080548391906000198101908110610d6657fe5b906000526020600020019080519060200190610d83929190611759565b507f949f75ae32248184b82a6ffcd53eff828ca8a6f2e7c1b3cad35668f1b1e5a27882846040518080602001836001600160a01b03166001600160a01b03168152602001828103825284818151815260200191508051906020019080838360005b83811015610dfc578181015183820152602001610de4565b50505050905090810190601f168015610e295780820380516001836020036101000a031916815260200191505b50935050505060405180910390a1505050565b60065460ff1681565b600360208181526000928352604092839020805460018083015460028085015496850154600486015460058701546006880180548c516101009882161589026000190190911695909504601f81018b90048b0286018b01909c528b85526001600160a01b039788169b959a60ff81169a9790049097169792969195909492939290830182828015610f175780601f10610eec57610100808354040283529160200191610f17565b820191906000526020600020905b815481529060010190602001808311610efa57829003601f168201915b5050506007909301549192505060ff1689565b60025481565b816002548110610f3f57600080fd5b600083815260036020526040902060020154839060ff16610f5f57600080fd5b60065460ff1615610f6f57600080fd5b60008311610f7c57600080fd5b60008481526003602052604090206007015460ff16151560011415611007576000805b600086815260046020526040902054811015610ffa576000868152600460205260409020805433919083908110610fd257fe5b6000918252602090912001546001600160a01b03161415610ff257600191505b600101610f9f565b508061100557600080fd5b505b6006805460ff19166001179055600084815260036020819052604090912001546110535782341461103757600080fd5b600084815260036020526040902060040180548401905561111a565b60008481526003602081905260409091200154601414156100a757341561107957600080fd5b60008481526003602090815260408083206002015481516323b872dd60e01b81523360048201523060248201526044810188905291516101009091046001600160a01b0316936323b872dd93606480850194919392918390030190829087803b1580156110e557600080fd5b505af11580156110f9573d6000803e3d6000fd5b505050506040513d602081101561110f57600080fd5b505161103757600080fd5b6040805185815233602082015280820185905290517f75aecd8d57cb4b1b263271bddb4961b993924dd466e6003c254832572d8a57e19181900360600190a150506006805460ff191690555050565b60065460ff161561117957600080fd5b600082815260036020526040902060020154829060ff1661119957600080fd5b600083815260036020526040902054339084906001600160a01b031682146111c057600080fd5b8460025481106111cf57600080fd5b6006805460ff191660011790556000868152600560205260409020548551146111f757600080fd5b60608551604051908082528060200260200182016040528015611224578160200160208202803883390190505b50905060005b86518110156112975760008881526003602090815260408083206004015460059092528220805460649291908590811061126057fe5b9060005260206000200154028161127357fe5b0490508083838151811061128357fe5b60209081029190910101525060010161122a565b5060005b81518110156112dd576112d5888883815181106112b457fe5b60200260200101518484815181106112c857fe5b60200260200101516115e2565b60010161129b565b506000878152600360209081526040808320600201805460ff1916905580518a815260608184018181528b51918301919091528a517fc1b60f03de0142802eef2afaff02546ce5ced8da57476a195b503fab5f71472a958d958d95899594939085019260808601928881019202908190849084905b8381101561136a578181015183820152602001611352565b50505050905001838103825284818151815260200191508051906020019060200280838360005b838110156113a9578181015183820152602001611391565b505050509050019550505050505060405180910390a150506006805460ff191690555050505050565b600560205281600052604060002081815481106113eb57fe5b90600052602060002001600091509150505481565b6001602052816000526040600020818154811061141957fe5b600091825260209182902001805460408051601f600260001961010060018716150201909416939093049283018590048502810185019091528181529450909250908301828280156114ac5780601f10611481576101008083540402835291602001916114ac565b820191906000526020600020905b81548152906001019060200180831161148f57829003601f168201915b505050505081565b600460205281600052604060002081815481106114cd57fe5b6000918252602090912001546001600160a01b03169150829050565b600081518351146114fc575060006115dc565b816040516020018082805190602001908083835b6020831061152f5780518252601f199092019160209182019101611510565b6001836020036101000a03801982511681845116808217855250505050505090500191505060405160208183030381529060405280519060200120836040516020018082805190602001908083835b6020831061159d5780518252601f19909201916020918201910161157e565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051602081830303815290604052805190602001201490505b92915050565b60008381526003602081905260409091200154611657576000811161160657611754565b60008381526003602052604080822060040180548490039055516001600160a01b0384169183156108fc02918491818181858888f19350505050158015611651573d6000803e3d6000fd5b50611754565b6000838152600360208190526040909120015460141415611733576000811161167f57611754565b6000838152600360209081526040808320600480820180548790039055600290910154825163a9059cbb60e01b81526001600160a01b03888116938201939093526024810187905292516101009091049091169363a9059cbb93604480850194919392918390030190829087803b1580156116f957600080fd5b505af115801561170d573d6000803e3d6000fd5b505050506040513d602081101561172357600080fd5b505161172e57600080fd5b611754565b600083815260036020819052604090912001546102d114156100a757600080fd5b505050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061179a57805160ff19168380011785556117c7565b828001600101855582156117c7579182015b828111156117c75782518255916020019190600101906117ac565b506117d3929150611872565b5090565b82805482825590600052602060002090810192821561182c579160200282015b8281111561182c57825182546001600160a01b0319166001600160a01b039091161782556020909201916001909101906117f7565b506117d392915061188f565b8280548282559060005260206000209081019282156117c757916020028201828111156117c75782518255916020019190600101906117ac565b61188c91905b808211156117d35760008155600101611878565b90565b61188c91905b808211156117d35780546001600160a01b031916815560010161189556fea265627a7a7231582071c73f24329afc63906d4dadba54c8d22b6d3e6681bfe336031a55ffa265c07f64736f6c634300050b0032";

    public static final String FUNC_WHITELISTCONTRIBUTOR = "whitelistContributor";

    public static final String FUNC_CHANGEDEADLINE = "changeDeadline";

    public static final String FUNC_NEWTOURNAMENT = "newTournament";

    public static final String FUNC_WHITELISTORGANIZER = "whitelistOrganizer";

    public static final String FUNC_CALLSTARTED = "callStarted";

    public static final String FUNC_TOURNAMENTS = "tournaments";

    public static final String FUNC_NUMTOURNAMENTS = "numTournaments";

    public static final String FUNC_CONTRIBUTE = "contribute";

    public static final String FUNC_PAYOUTWINNERS = "payoutWinners";

    public static final String FUNC_PRIZEDISTRIBUTIONS = "prizeDistributions";

    public static final String FUNC_ORGANIZERPLATFORMS = "organizerPlatforms";

    public static final String FUNC_TOURNAMENTCONTRIBUTORS = "tournamentContributors";

    public static final Event TOURNAMENTISSUED_EVENT = new Event("TournamentIssued", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<DynamicArray<Uint256>>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
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

    public static final Event CONTRIBUTORWHITELISTED_EVENT = new Event("ContributorWhitelisted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event ORGANIZERWHITELISTED_EVENT = new Event("OrganizerWhitelisted", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
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

    public RemoteFunctionCall<TransactionReceipt> whitelistContributor(BigInteger _tournamentId, String _newContributor) {
        final Function function = new Function(
                FUNC_WHITELISTCONTRIBUTOR, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.Address(160, _newContributor)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> changeDeadline(BigInteger _tournamentId, BigInteger _deadline) {
        final Function function = new Function(
                FUNC_CHANGEDEADLINE, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_tournamentId), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> newTournament(String _data, BigInteger _deadline, String _token, BigInteger _tokenVersion, BigInteger _maxPlayers, List<BigInteger> _prizeDistribution, String _platform, Boolean _restrictContributors, BigInteger weiValue) {
        final Function function = new Function(
                FUNC_NEWTOURNAMENT, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_data), 
                new org.web3j.abi.datatypes.generated.Uint256(_deadline), 
                new org.web3j.abi.datatypes.Address(160, _token), 
                new org.web3j.abi.datatypes.generated.Uint256(_tokenVersion), 
                new org.web3j.abi.datatypes.generated.Uint256(_maxPlayers), 
                new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                        org.web3j.abi.datatypes.generated.Uint256.class,
                        org.web3j.abi.Utils.typeMap(_prizeDistribution, org.web3j.abi.datatypes.generated.Uint256.class)), 
                new org.web3j.abi.datatypes.Utf8String(_platform), 
                new org.web3j.abi.datatypes.Bool(_restrictContributors)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> whitelistOrganizer(String _organizer, String _platform) {
        final Function function = new Function(
                FUNC_WHITELISTORGANIZER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _organizer), 
                new org.web3j.abi.datatypes.Utf8String(_platform)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Boolean> callStarted() {
        final Function function = new Function(FUNC_CALLSTARTED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Tuple9<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger, String, Boolean>> tournaments(BigInteger param0) {
        final Function function = new Function(FUNC_TOURNAMENTS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Bool>() {}));
        return new RemoteFunctionCall<Tuple9<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger, String, Boolean>>(function,
                new Callable<Tuple9<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger, String, Boolean>>() {
                    @Override
                    public Tuple9<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger, String, Boolean> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple9<String, BigInteger, Boolean, String, BigInteger, BigInteger, BigInteger, String, Boolean>(
                                (String) results.get(0).getValue(), 
                                (BigInteger) results.get(1).getValue(), 
                                (Boolean) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue(), 
                                (BigInteger) results.get(6).getValue(), 
                                (String) results.get(7).getValue(), 
                                (Boolean) results.get(8).getValue());
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

    public RemoteFunctionCall<String> organizerPlatforms(String param0, BigInteger param1) {
        final Function function = new Function(FUNC_ORGANIZERPLATFORMS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> tournamentContributors(BigInteger param0, BigInteger param1) {
        final Function function = new Function(FUNC_TOURNAMENTCONTRIBUTORS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0), 
                new org.web3j.abi.datatypes.generated.Uint256(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
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
            typedResponse._platform = (String) eventValues.getNonIndexedValues().get(8).getValue();
            typedResponse._restrictContributors = (Boolean) eventValues.getNonIndexedValues().get(9).getValue();
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
                typedResponse._platform = (String) eventValues.getNonIndexedValues().get(8).getValue();
                typedResponse._restrictContributors = (Boolean) eventValues.getNonIndexedValues().get(9).getValue();
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

    public List<ContributorWhitelistedEventResponse> getContributorWhitelistedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CONTRIBUTORWHITELISTED_EVENT, transactionReceipt);
        ArrayList<ContributorWhitelistedEventResponse> responses = new ArrayList<ContributorWhitelistedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ContributorWhitelistedEventResponse typedResponse = new ContributorWhitelistedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ContributorWhitelistedEventResponse> contributorWhitelistedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ContributorWhitelistedEventResponse>() {
            @Override
            public ContributorWhitelistedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CONTRIBUTORWHITELISTED_EVENT, log);
                ContributorWhitelistedEventResponse typedResponse = new ContributorWhitelistedEventResponse();
                typedResponse.log = log;
                typedResponse._tournamentId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._contributor = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<ContributorWhitelistedEventResponse> contributorWhitelistedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CONTRIBUTORWHITELISTED_EVENT));
        return contributorWhitelistedEventFlowable(filter);
    }

    public List<OrganizerWhitelistedEventResponse> getOrganizerWhitelistedEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ORGANIZERWHITELISTED_EVENT, transactionReceipt);
        ArrayList<OrganizerWhitelistedEventResponse> responses = new ArrayList<OrganizerWhitelistedEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OrganizerWhitelistedEventResponse typedResponse = new OrganizerWhitelistedEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._platform = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._organizer = (String) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OrganizerWhitelistedEventResponse> organizerWhitelistedEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OrganizerWhitelistedEventResponse>() {
            @Override
            public OrganizerWhitelistedEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ORGANIZERWHITELISTED_EVENT, log);
                OrganizerWhitelistedEventResponse typedResponse = new OrganizerWhitelistedEventResponse();
                typedResponse.log = log;
                typedResponse._platform = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._organizer = (String) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OrganizerWhitelistedEventResponse> organizerWhitelistedEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ORGANIZERWHITELISTED_EVENT));
        return organizerWhitelistedEventFlowable(filter);
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

        public String _platform;

        public Boolean _restrictContributors;
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

    public static class ContributorWhitelistedEventResponse extends BaseEventResponse {
        public BigInteger _tournamentId;

        public String _contributor;
    }

    public static class OrganizerWhitelistedEventResponse extends BaseEventResponse {
        public String _platform;

        public String _organizer;
    }
}
