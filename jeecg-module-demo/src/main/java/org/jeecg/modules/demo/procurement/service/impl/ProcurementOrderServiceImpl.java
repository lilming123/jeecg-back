package org.jeecg.modules.demo.procurement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.jeecg.modules.demo.base.entity.Supplier;
import org.jeecg.modules.demo.base.mapper.SupplierMapper;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrder;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderSupplier;
import org.jeecg.modules.demo.procurement.entity.ProcurementPrice;
import org.jeecg.modules.demo.procurement.mapper.ProcurementOrderMapper;
import org.jeecg.modules.demo.procurement.mapper.ProcurementOrderSupplierMapper;
import org.jeecg.modules.demo.procurement.mapper.ProcurementPriceMapper;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderItemService;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderService;
import org.jeecg.modules.demo.produce.entity.ProduceProcess;
import org.jeecg.modules.demo.produce.entity.ProduceProcessMaterial;
import org.jeecg.modules.demo.produce.mapper.ProduceProcessMapper;
import org.jeecg.modules.demo.produce.mapper.ProduceProcessMaterialMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 生成采购订单
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@Service
public class ProcurementOrderServiceImpl extends ServiceImpl<ProcurementOrderMapper, ProcurementOrder> implements IProcurementOrderService {
    @Autowired
    private IProcurementOrderItemService procurementOrderItemService;
    @Autowired
    private  ProcurementPriceMapper procurementPriceMapper;
    @Autowired
    private  SupplierMapper supplierMapper;
    @Autowired
    private ProduceProcessMapper produceProcessMapper;
    @Autowired
    private ProcurementOrderSupplierMapper procurementOrderSupplierMapper;
    @Autowired
    private ProduceProcessMaterialMapper produceProcessMaterialMapper;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderItem(List<ProcurementOrder> procurementOrderList) {
        //materialMap是材料名和数量的键值对
        Map<String, BigDecimal> materialMap = new HashMap<>();
        //循环每个产品
        for (ProcurementOrder eachProcurementOrder : procurementOrderList){
            String goodId = eachProcurementOrder.getName();
            Integer num = eachProcurementOrder.getNum();
            QueryWrapper<ProduceProcess> queryWrapper = new QueryWrapper<ProduceProcess>();
            queryWrapper.eq("goods_name", goodId);
            List<ProduceProcess> processes =  produceProcessMapper.selectList(queryWrapper);
            //循环每个产品的工序
            for (ProduceProcess each : processes){
                String processId = each.getId();
                List<ProduceProcessMaterial> materials = produceProcessMaterialMapper.selectByMainId(processId);
                //循环每个工序所耗的材料
                for (ProduceProcessMaterial eachMaterial : materials){
                    //如果材料重复要加上，同时要乘产品数量
                    if (materialMap.containsKey(eachMaterial.getMaterial())){
                        BigDecimal addNumber = eachMaterial.getNum().multiply(new BigDecimal(num));
                        materialMap.put(eachMaterial.getMaterial(),materialMap.get(eachMaterial.getMaterial()).add(addNumber));
                    }else {
                        materialMap.put(eachMaterial.getMaterial(), eachMaterial.getNum().multiply(new BigDecimal(num)));
                    }
                }
            }
        }

        //supplierMap是供应商id和供应商采购订单id的键值对
        Map<String, String> supplierMap = new HashMap<>();
        for (Map.Entry<String, BigDecimal> eachMaterialEntry: materialMap.entrySet()){
            QueryWrapper<ProcurementPrice> Wrapper = new QueryWrapper<ProcurementPrice>();
            Wrapper.eq("name", eachMaterialEntry.getKey());
            ProcurementPrice procurementPrice = procurementPriceMapper.selectOne(Wrapper);
            String supplierId = procurementPrice.getSupplier();
            if (supplierMap.containsKey(supplierId)){
                ProcurementOrderItem procurementOrderItem = new ProcurementOrderItem();
                procurementOrderItem.setSupplier(supplierMap.get(supplierId));
                procurementOrderItem.setUnit(procurementPrice.getUnit());
                procurementOrderItem.setName(eachMaterialEntry.getKey());
                procurementOrderItem.setSpecs(procurementPrice.getSpecs());
                procurementOrderItem.setPrice(procurementPrice.getPrice());
                procurementOrderItem.setNum((int) Math.ceil(eachMaterialEntry.getValue().doubleValue()));
                procurementOrderItem.setTotalMoney(new BigDecimal(procurementOrderItem.getNum()).multiply(procurementOrderItem.getPrice()));
                procurementOrderItemService.save(procurementOrderItem);
            }else {
                ProcurementOrderSupplier procurementOrderSupplier = new ProcurementOrderSupplier();
                Supplier supplier = supplierMapper.selectById(supplierId);
                procurementOrderSupplier.setPhone(supplier.getPhone());
                procurementOrderSupplier.setPerson(supplier.getPerson());
                procurementOrderSupplier.setLocation(supplier.getLocation());
                procurementOrderSupplier.setSupplier(supplier.getName());
                procurementOrderSupplier.setState("1");
                procurementOrderSupplierMapper.insert(procurementOrderSupplier);
                supplierMap.put(supplierId,procurementOrderSupplier.getId());
                ProcurementOrderItem procurementOrderItem = new ProcurementOrderItem();
                procurementOrderItem.setSupplier(procurementOrderSupplier.getId());
                procurementOrderItem.setUnit(procurementPrice.getUnit());
                procurementOrderItem.setName(eachMaterialEntry.getKey());
                procurementOrderItem.setSpecs(procurementPrice.getSpecs());
                procurementOrderItem.setPrice(procurementPrice.getPrice());
                procurementOrderItem.setNum((int) Math.ceil(eachMaterialEntry.getValue().doubleValue()));
                procurementOrderItem.setTotalMoney(new BigDecimal(procurementOrderItem.getNum()).multiply(procurementOrderItem.getPrice()));
                procurementOrderItemService.save(procurementOrderItem);
            }
        }
        //更新所有订单的总金额
        for (Map.Entry<String, String> eachSupplierOrderEntry: supplierMap.entrySet()){
            BigDecimal total_money = new BigDecimal(0);
            List<ProcurementOrderItem> listItem = procurementOrderItemService.selectByMainId(eachSupplierOrderEntry.getValue());
            //循环加总获得单个订单的总金额
            for (ProcurementOrderItem eachItem : listItem){
                total_money = total_money.add(eachItem.getTotalMoney());
            }
            UpdateWrapper<ProcurementOrderSupplier> updateWrapper = new UpdateWrapper<ProcurementOrderSupplier>();
            updateWrapper.eq("id", eachSupplierOrderEntry.getValue());
            updateWrapper.set("total_money",total_money);
            procurementOrderSupplierMapper.update(null,updateWrapper);
        }
    }

}
