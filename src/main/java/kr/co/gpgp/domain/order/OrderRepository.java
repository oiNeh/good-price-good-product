package kr.co.gpgp.domain.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Page<Order> orderSearch(OrderSearchCondition condition, Pageable pageable);

}