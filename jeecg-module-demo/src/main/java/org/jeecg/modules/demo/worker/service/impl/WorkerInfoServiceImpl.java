package org.jeecg.modules.demo.worker.service.impl;

import org.jeecg.modules.demo.worker.entity.WorkerInfo;
import org.jeecg.modules.demo.worker.mapper.WorkerInfoMapper;
import org.jeecg.modules.demo.worker.service.IWorkerInfoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 员工基本信息
 * @Author: jeecg-boot
 * @Date:   2023-01-22
 * @Version: V1.0
 */
@Service
public class WorkerInfoServiceImpl extends ServiceImpl<WorkerInfoMapper, WorkerInfo> implements IWorkerInfoService {

}
