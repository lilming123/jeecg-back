package org.jeecg.modules.demo.base.service.impl;

import org.jeecg.modules.demo.base.entity.Customer;
import org.jeecg.modules.demo.base.mapper.CustomerMapper;
import org.jeecg.modules.demo.base.service.ICustomerService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 客户管理
 * @Author: jeecg-boot
 * @Date:   2023-01-22
 * @Version: V1.0
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

}
