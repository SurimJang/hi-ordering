package tableordering.domain;

import tableordering.domain.MenuDecreasedEvent;
import tableordering.domain.MenuIncresedEvent;
import tableordering.domain.MenuCreatedEvent;
import tableordering.domain.MenuDeletedEvent;
import tableordering.domain.OutOfStockEvent;
import tableordering.MenuApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;


@Entity
@Table(name="Menu_table")
@Data

//<<< DDD / Aggregate Root
public class Menu  {


    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
    
    private Long id;
    
    
    
    
    private String menuName;
    
    
    
    
    private Integer menuPrice;
    
    
    
    
    private Integer qty;
    
    
    
    
    private Long categoryId;

    @PostPersist
    public void onPostPersist(){


        MenuDecreasedEvent menuDecreasedEvent = new MenuDecreasedEvent(this);
        menuDecreasedEvent.publishAfterCommit();



        MenuIncresedEvent menuIncresedEvent = new MenuIncresedEvent(this);
        menuIncresedEvent.publishAfterCommit();



        MenuCreatedEvent menuCreatedEvent = new MenuCreatedEvent(this);
        menuCreatedEvent.publishAfterCommit();



        MenuDeletedEvent menuDeletedEvent = new MenuDeletedEvent(this);
        menuDeletedEvent.publishAfterCommit();

    
    }
    @PrePersist
    public void onPrePersist(){


        OutOfStockEvent outOfStockEvent = new OutOfStockEvent(this);
        outOfStockEvent.publishAfterCommit();

    
    }

    public static MenuRepository repository(){
        MenuRepository menuRepository = MenuApplication.applicationContext.getBean(MenuRepository.class);
        return menuRepository;
    }



    
    public void menu(){
        tableordering.external.MenuQuery menuQuery = new tableordering.external.MenuQuery();
        MenuApplication.applicationContext
            .getBean(tableordering.external.Service.class)
            .( menuQuery);
    }
    
    

//<<< Clean Arch / Port Method
    public static void decreaseMenuPolicy(OrderPlacedEvent orderPlacedEvent){
        
        //implement business logic here:

        /** Example 1:  new item 
        Menu menu = new Menu();
        repository().save(menu);

        MenuDecreasedEvent menuDecreasedEvent = new MenuDecreasedEvent(menu);
        menuDecreasedEvent.publishAfterCommit();
        OutOfStockEvent outOfStockEvent = new OutOfStockEvent(menu);
        outOfStockEvent.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderPlacedEvent.get???()).ifPresent(menu->{
            
            menu // do something
            repository().save(menu);

            MenuDecreasedEvent menuDecreasedEvent = new MenuDecreasedEvent(menu);
            menuDecreasedEvent.publishAfterCommit();
            OutOfStockEvent outOfStockEvent = new OutOfStockEvent(menu);
            outOfStockEvent.publishAfterCommit();

         });
        */

        
    }
//>>> Clean Arch / Port Method
//<<< Clean Arch / Port Method
    public static void increaseMenuPolicy(OrderCancelledEvent orderCancelledEvent){
        
        //implement business logic here:

        /** Example 1:  new item 
        Menu menu = new Menu();
        repository().save(menu);

        MenuIncresedEvent menuIncresedEvent = new MenuIncresedEvent(menu);
        menuIncresedEvent.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderCancelledEvent.get???()).ifPresent(menu->{
            
            menu // do something
            repository().save(menu);

            MenuIncresedEvent menuIncresedEvent = new MenuIncresedEvent(menu);
            menuIncresedEvent.publishAfterCommit();

         });
        */

        
    }
//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
