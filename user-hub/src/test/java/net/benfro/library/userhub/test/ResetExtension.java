package net.benfro.library.userhub.test;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import reactor.core.publisher.Flux;

/**
 * This is a JUnit extension that clears database tables between test runs to ensure that
 * data isn't kept between tests.
 * TODO: If you add new tables, that table should probably be cleared here too!
 */
public class ResetExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext extensionContext) throws Exception {
        var applicationContext = SpringExtension.getApplicationContext(extensionContext);
        var db = applicationContext
                .getBean(DatabaseClient.class);
        var transactionManager = applicationContext
                .getBean(ReactiveTransactionManager.class);
        var tx = TransactionalOperator.create(transactionManager);

        Flux.just( "person").flatMap(table -> db.sql("delete from %s".formatted(table)).then())
                .as(tx::transactional)
                .collectList()
                .block();

        // Reset the sync too so a test doesn't get released off an old event
//        applicationContext.getBean(LoginEventHandledSync.class).reset();
    }
}
