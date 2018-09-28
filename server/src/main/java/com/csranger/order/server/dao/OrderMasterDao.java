package com.csranger.order.server.dao;

import com.csranger.order.server.model.OrderMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderMasterDao extends JpaRepository<OrderMaster, String> {


}
