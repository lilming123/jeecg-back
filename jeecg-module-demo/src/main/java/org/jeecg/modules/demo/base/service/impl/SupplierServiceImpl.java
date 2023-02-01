package org.jeecg.modules.demo.base.service.impl;

import org.jeecg.modules.demo.base.entity.Supplier;
import org.jeecg.modules.demo.base.mapper.SupplierMapper;
import org.jeecg.modules.demo.base.service.ISupplierService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 供应商管理
 * @Author: jeecg-boot
 * @Date:   2023-01-22
 * @Version: V1.0
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

}
