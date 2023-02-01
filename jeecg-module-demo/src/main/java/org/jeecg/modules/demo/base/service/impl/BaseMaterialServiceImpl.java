package org.jeecg.modules.demo.base.service.impl;

import org.jeecg.modules.demo.base.entity.BaseMaterial;
import org.jeecg.modules.demo.base.mapper.BaseMaterialMapper;
import org.jeecg.modules.demo.base.service.IBaseMaterialService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 原材料管理
 * @Author: jeecg-boot
 * @Date:   2023-01-23
 * @Version: V1.0
 */
@Service
public class BaseMaterialServiceImpl extends ServiceImpl<BaseMaterialMapper, BaseMaterial> implements IBaseMaterialService {

}
