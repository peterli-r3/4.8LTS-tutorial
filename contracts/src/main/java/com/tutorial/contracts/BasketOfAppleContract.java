package com.tutorial.contracts;

import com.tutorial.states.AppleStamp;
import com.tutorial.states.BasketOfApple;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;
import org.jetbrains.annotations.NotNull;

import static net.corda.core.contracts.ContractsDSL.requireThat;

public class BasketOfAppleContract implements Contract {

    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.tutorial.contracts.BasketOfAppleContract";


    @Override
    public void verify(@NotNull LedgerTransaction tx) throws IllegalArgumentException {
        //Extract the command from the transaction.
        final CommandData commandData = tx.getCommands().get(0).getValue();
        if (commandData instanceof BasketOfAppleContract.Commands.packToBasket){
            BasketOfApple output = tx.outputsOfType(BasketOfApple.class).get(0);
            requireThat(require -> {
                require.using("This transaction should only output one BasketOfApple state", tx.getOutputs().size() == 1);
                require.using("The output BasketOfApple state should have clear description of Apple product", !output.getDescription().equals(""));
                require.using("The output BasketOfApple state should have non zero weight", output.getweight() > 0);
                return null;
            });
        }else{

        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class packToBasket implements BasketOfAppleContract.Commands {}
    }
}
