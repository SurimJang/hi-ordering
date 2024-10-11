package tableordering.domain;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tableordering.domain.*;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "menus", path = "menus")
public interface MenuRepository extends PagingAndSortingRepository<Menu, Long> {

    // storeId와 categoryId로 필터링하는 메서드
    List<Menu> findByStoreIdAndCategoryId(Long storeId, Long categoryId, Pageable pageable);

    // storeId 필터링하는 메서드
    List<Menu> findByStoreId(Long storeId, Pageable pageable);
}