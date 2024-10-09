package net.benfro.library.bookhub.test;

import net.benfro.library.bookhub.event.LoginEventHandledApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
//import se.agila.assignment.event.LoginEventHandledApplicationEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class is used to synchronize with the service as it consumes events off Kafka.
 * Since Kafka records are delivered asynchronously, it's hard to know when the service
 * has processed the message. The <code>LoginEventSubscriber</code> emits a Spring
 * application event when a message has been handled - this class subscribes to the
 * corresponding event and puts the event in a queue that the test classes can read from.
 */
@Component
public class LoginEventHandledSync {

    private final BlockingQueue<LoginEventHandledApplicationEvent> eventQueue = new LinkedBlockingQueue<>();

    @EventListener(LoginEventHandledApplicationEvent.class)
    public void onLoginEventHandled(LoginEventHandledApplicationEvent event) {
        eventQueue.offer(event);
    }

    public LoginEventHandledApplicationEvent read() throws InterruptedException {
        return eventQueue.take();
    }

    public void reset() {
        eventQueue.clear();
    }
}
