package org.jeecg.modules.demo.produce.service;

import org.jeecg.modules.demo.produce.entity.ProduceProcessMaterial;
import org.jeecg.modules.demo.produce.entity.ProduceProcess;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 生产流程管理
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
public interface IProduceProcessService extends IService<ProduceProcess> {

	/**
	 * 添加一对多
	 *
	 * @param produceProcess
	 * @param produceProcessMaterialList
	 */
	public void saveMain(ProduceProcess produceProcess,List<ProduceProcessMaterial> produceProcessMaterialList) ;
	
	/**
	 * 修改一对多
	 *
   * @param produceProcess
   * @param produceProcessMaterialList
	 */
	public void updateMain(ProduceProcess produceProcess,List<ProduceProcessMaterial> produceProcessMaterialList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
