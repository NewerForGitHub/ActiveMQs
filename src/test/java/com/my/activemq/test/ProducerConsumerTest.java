package com.my.activemq.test;

import javax.jms.Destination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.my.activemq.producer.ProducerService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext.xml")
public class ProducerConsumerTest {

    @Autowired
    private ProducerService producerService;
    @Autowired
    @Qualifier("queueDestination")
    private Destination destination;

    @Test
    public void testSend() {
        for (int i = 0; i < 2; i++) {
//            Thread.sleep(3000);
            this.producerService.sendMessage(this.destination, "你好，生产者！这是消息："
                + (i + 1));
        }
    }

}