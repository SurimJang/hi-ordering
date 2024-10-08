package tableordering.domain;

import tableordering.domain.OrderPlacedEvent;
import tableordering.domain.OrderCancelledEvent;
import tableordering.domain.OrderConfirmedEvent;
import tableordering.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;

@Entity
@Table(name = "Order_table")
@Data

// <<< DDD / Aggregate Root
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long id;

    private Long userId;

    @ElementCollection
    private List<Long> menuId;

    private Integer qty;

    private Date createdAt;

    private Date updatedAt;

    private String orderStatus;

    @PostPersist
    public void onPostPersist() {
        if (this.orderStatus.equals("OrderPlaced")) {
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent(this);
            orderPlacedEvent.publishAfterCommit();
        }
    }

    @PostUpdate
    public void onPostUpdate() {
        // 주문 상태에 따라 각각 이벤트 발행
        switch (this.orderStatus) {
            case "OrderCancelled":
                OrderCancelledEvent orderCancelledEvent = new OrderCancelledEvent(this);
                orderCancelledEvent.publishAfterCommit();
                break;

            case "OrderConfirmed":
                OrderConfirmedEvent orderConfirmedEvent = new OrderConfirmedEvent(this);
                orderConfirmedEvent.publishAfterCommit();
                break;

            // 필요한 다른 상태 이벤트 추가 가능
        }
    }

    public static OrderRepository repository() {
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }

    // <<< Clean Arch / Port Method
    public static void updateStatusPolicy(OutOfStockEvent outOfStockEvent) {

        // 재고 부족으로 인한 주문 취소
        repository().findById(outOfStockEvent.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("OrderCancelled");
            repository().save(order);
        });
    }

    // >>> Clean Arch / Port Method
    // <<< Clean Arch / Port Method
    public static void updateStatusPolicy(PaymentCompleteEvent paymentCompleteEvent) {

        // 결제 완료시 주문 생성 및 완료
        repository().findById(paymentCompleteEvent.getOrderId()).ifPresent(order -> {
            order.setOrderStatus("OrderConfirmed");
            repository().save(order);
        });
    }
    // >>> Clean Arch / Port Method

}
// >>> DDD / Aggregate Root
