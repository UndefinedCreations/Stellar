package com.undefined.stellar;

import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        new StellarCommand("test")
                .addListArgument("test", new ArrayList<>(), (sender, s) -> s);
    }
}
