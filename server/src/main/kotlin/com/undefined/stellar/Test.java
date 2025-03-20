package com.undefined.stellar;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        new StellarCommand("test")
                .addPhraseArgument("args")
                .addWordArgument(1, argument -> {
                    argument.addSuggestion(context -> new ArrayList<>());
                    argument.addRunnable(context -> false);
                    argument.addExecution(context -> context.getSender().sendMessage("Hello world!"));
                });
    }
}
