package tableordering.domain;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import tableordering.PaymentApplication;
import tableordering.domain.PaymentCancelledEvent;
import tableordering.domain.PaymentCompleteEvent;

@Entity
@Table(name = "Payment_table")
@Data
//<<< DDD / Aggregate Root
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer paymentAmount;

    private Long userId;

    private Long orderId;

    private String paymentStatus;

    @PostPersist
    public void onPostPersist() {
        PaymentCompleteEvent paymentCompleteEvent = new PaymentCompleteEvent(
            this
        );
        paymentCompleteEvent.publishAfterCommit();

        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent(
            this
        );
        paymentCancelledEvent.publishAfterCommit();
    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
            PaymentRepository.class
        );
        return paymentRepository;
    }

    //<<< Clean Arch / Port Method
    public static void paymentRequestPolicy(
        MenuDecreasedEvent menuDecreasedEvent
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        PaymentCompleteEvent paymentCompleteEvent = new PaymentCompleteEvent(payment);
        paymentCompleteEvent.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(menuDecreasedEvent.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentCompleteEvent paymentCompleteEvent = new PaymentCompleteEvent(payment);
            paymentCompleteEvent.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void paymentCancelPolicy(
        MenuIncresedEvent menuIncresedEvent
    ) {
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent(payment);
        paymentCancelledEvent.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(menuIncresedEvent.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent(payment);
            paymentCancelledEvent.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
