package com.tutorial.contracts;

import com.tutorial.states.AppleStamp;
import com.tutorial.states.BasketOfApple;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import static net.corda.core.contracts.ContractsDSL.requireThat;

public class AppleStampContract implements Contract {

    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.tutorial.contracts.AppleStampContract";

    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {

        //Extract the command from the transaction.
        final CommandData commandData = tx.getCommands().get(0).getValue();
        if (commandData instanceof AppleStampContract.Commands.Issue){
            AppleStamp output = tx.outputsOfType(AppleStamp.class).get(0);
            requireThat(require -> {
                require.using("This transaction should only output one AppleStamp state", tx.getOutputs().size() == 1);
                require.using("The output AppleStamp state should have clear description of the type of redeemable goods", !output.getStampDesc().equals(""));
                return null;
            });
        }else if (commandData instanceof AppleStampContract.Commands.Redeem) {
            //Retrieve the output state of the transaction
            AppleStamp input = tx.inputsOfType(AppleStamp.class).get(0);
            BasketOfApple output = tx.outputsOfType(BasketOfApple.class).get(0);

            //Using Corda DSL function requireThat to replicate conditions-checks
            requireThat(require -> {
                require.using("This transaction should consume one AppleStamp state and one BasketOfApple state", tx.getInputStates().size() == 2);
                //require.using("The issuer of the Apple stamp should be the producing farm of this basket of apple", input.getIssuer().equals(output.getFarm()));
                require.using("The basket of apple has to weight more than 0", output.getweight() > 0);
                return null;
            });
        }
        else{

        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        //In our hello-world app, We will only have one command.
        class Issue implements AppleStampContract.Commands {}
        class Redeem implements AppleStampContract.Commands {}
    }
}
