package org.jeecg.modules.demo.produce.service.impl;

import org.jeecg.modules.demo.produce.entity.ProduceProcessMaterial;
import org.jeecg.modules.demo.produce.mapper.ProduceProcessMaterialMapper;
import org.jeecg.modules.demo.produce.service.IProduceProcessMaterialService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 生产流程材料表
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@Service
public class ProduceProcessMaterialServiceImpl extends ServiceImpl<ProduceProcessMaterialMapper, ProduceProcessMaterial> implements IProduceProcessMaterialService {
	
	@Autowired
	private ProduceProcessMaterialMapper produceProcessMaterialMapper;
	
	@Override
	public List<ProduceProcessMaterial> selectByMainId(String mainId) {
		return produceProcessMaterialMapper.selectByMainId(mainId);
	}
}
