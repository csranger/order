package com.csranger.order.dao;

import com.csranger.order.model.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterDao extends JpaRepository<OrderMaster, String> {


}
