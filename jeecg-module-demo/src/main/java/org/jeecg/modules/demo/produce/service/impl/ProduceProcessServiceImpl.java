package org.jeecg.modules.demo.produce.service.impl;

import org.jeecg.modules.demo.produce.entity.ProduceProcess;
import org.jeecg.modules.demo.produce.entity.ProduceProcessMaterial;
import org.jeecg.modules.demo.produce.mapper.ProduceProcessMaterialMapper;
import org.jeecg.modules.demo.produce.mapper.ProduceProcessMapper;
import org.jeecg.modules.demo.produce.service.IProduceProcessService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 生产流程管理
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@Service
public class ProduceProcessServiceImpl extends ServiceImpl<ProduceProcessMapper, ProduceProcess> implements IProduceProcessService {

	@Autowired
	private ProduceProcessMapper produceProcessMapper;
	@Autowired
	private ProduceProcessMaterialMapper produceProcessMaterialMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(ProduceProcess produceProcess, List<ProduceProcessMaterial> produceProcessMaterialList) {
		produceProcessMapper.insert(produceProcess);
		if(produceProcessMaterialList!=null && produceProcessMaterialList.size()>0) {
			for(ProduceProcessMaterial entity:produceProcessMaterialList) {
				//外键设置
				entity.setProcessId(produceProcess.getId());
				produceProcessMaterialMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(ProduceProcess produceProcess,List<ProduceProcessMaterial> produceProcessMaterialList) {
		produceProcessMapper.updateById(produceProcess);
		
		//1.先删除子表数据
		produceProcessMaterialMapper.deleteByMainId(produceProcess.getId());
		
		//2.子表数据重新插入
		if(produceProcessMaterialList!=null && produceProcessMaterialList.size()>0) {
			for(ProduceProcessMaterial entity:produceProcessMaterialList) {
				//外键设置
				entity.setProcessId(produceProcess.getId());
				produceProcessMaterialMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		produceProcessMaterialMapper.deleteByMainId(id);
		produceProcessMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			produceProcessMaterialMapper.deleteByMainId(id.toString());
			produceProcessMapper.deleteById(id);
		}
	}
	
}
