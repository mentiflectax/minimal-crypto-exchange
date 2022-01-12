package com.dpisarenko.minimalcryptoexchange.clj;

import clojure.java.api.Clojure;
import clojure.lang.IFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.dpisarenko.minimalcryptoexchange.clj.ClojureService.MAIN_CLOJURE_NAMESPACE;

@Service
public class ClojureRepl {
    private final Logger logger = LoggerFactory.getLogger("CLOJURE");

    @PostConstruct
    public void init() {
        final IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read(MAIN_CLOJURE_NAMESPACE));

        Clojure.var(MAIN_CLOJURE_NAMESPACE, "init")
                .invoke(logger);

        Clojure.var("clojure.core.server", "start-server").invoke(
                Clojure.read("{:port 5555 :name spring-repl :accept clojure.core.server/repl}")
        );

    }

    @PreDestroy
    public void destroy() {
        Clojure.var("clojure.core.server", "stop-server").invoke(
                Clojure.read("{:name spring-repl}")
        );
    }
}
