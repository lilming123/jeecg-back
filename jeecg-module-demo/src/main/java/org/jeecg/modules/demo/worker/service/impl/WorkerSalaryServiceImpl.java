package org.jeecg.modules.demo.worker.service.impl;

import org.jeecg.modules.demo.worker.entity.WorkerSalary;
import org.jeecg.modules.demo.worker.mapper.WorkerSalaryMapper;
import org.jeecg.modules.demo.worker.service.IWorkerSalaryService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 员工薪资管理
 * @Author: jeecg-boot
 * @Date:   2023-01-22
 * @Version: V1.0
 */
@Service
public class WorkerSalaryServiceImpl extends ServiceImpl<WorkerSalaryMapper, WorkerSalary> implements IWorkerSalaryService {

}
