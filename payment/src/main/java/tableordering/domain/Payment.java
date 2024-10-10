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
@Table(name = "payment", schema = "payment")
@Data
// <<< DDD / Aggregate Root
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
                this);
        paymentCompleteEvent.publishAfterCommit();

    }

    @PostUpdate
    public void onPostUpdate() {
        switch (this.paymentStatus) {
            case "Cancelled":
                PaymentCancelledEvent paymentCancelledEvent = new PaymentCancelledEvent(
                        this);
                paymentCancelledEvent.publishAfterCommit();
                break;

            default:
                break;
        }

    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
                PaymentRepository.class);
        return paymentRepository;
    }

    // <<< Clean Arch / Port Method
    public static void paymentRequestPolicy(MenuDecreasedEvent menuDecreasedEvent) {
        // implement business logic here:
        Payment payment = new Payment();
        payment.setOrderId(menuDecreasedEvent.getOrderId());
        payment.setUserId(menuDecreasedEvent.getUserId());
        payment.setPaymentAmount(menuDecreasedEvent.getPaymentAmount());
        payment.setPaymentStatus("Completed");
        repository().save(payment);
    }

    // >>> Clean Arch / Port Method
    // <<< Clean Arch / Port Method
    public static void paymentCancelPolicy(MenuIncresedEvent menuIncresedEvent) {
        // implement business logic here:
        repository().findByOrderId(menuIncresedEvent.getOrderId()).ifPresent(payment -> {
            payment.setPaymentStatus("Cancelled");
            repository().save(payment);
        });

    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
