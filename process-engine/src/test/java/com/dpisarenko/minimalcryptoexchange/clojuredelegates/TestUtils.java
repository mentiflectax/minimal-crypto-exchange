package com.dpisarenko.minimalcryptoexchange.clojuredelegates;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import com.dpisarenko.minimalcryptoexchange.clj.ClojureService;

import static com.dpisarenko.minimalcryptoexchange.clj.ClojureService.MAIN_CLOJURE_NAMESPACE;

public class TestUtils {

    private TestUtils() {
    }

    public static ClojureService createClojureBackend() {
        final ClojureService backend = new ClojureService();
        final IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read(MAIN_CLOJURE_NAMESPACE));
        return backend;
    }

}
