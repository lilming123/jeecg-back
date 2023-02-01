package org.jeecg.modules.demo.worker.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 员工薪资管理
 * @Author: jeecg-boot
 * @Date:   2023-01-22
 * @Version: V1.0
 */
@Data
@TableName("worker_salary")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="worker_salary对象", description="员工薪资管理")
public class WorkerSalary implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新日期")
    private Date updateTime;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private String sysOrgCode;
	/**员工名*/
	@Excel(name = "员工名", width = 15, dictTable = "worker_info", dicText = "name", dicCode = "id")
	@Dict(dictTable = "worker_info", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "员工名")
    private String workerName;
	/**员工工号*/
	@Excel(name = "员工工号", width = 15)
    @ApiModelProperty(value = "员工工号")
    private String workerCode;
	/**应发工资*/
	@Excel(name = "应发工资", width = 15)
    @ApiModelProperty(value = "应发工资")
    private Integer yfSalary;
	/**月绩效奖金*/
	@Excel(name = "月绩效奖金", width = 15)
    @ApiModelProperty(value = "月绩效奖金")
    private Integer yjxSalary;
	/**其他收入*/
	@Excel(name = "其他收入", width = 15)
    @ApiModelProperty(value = "其他收入")
    private Integer ortherSalary;
	/**发放工资时间*/
	@Excel(name = "发放工资时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "发放工资时间")
    private Date incomeTime;
	/**联系方式*/
	@Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private String phone;
	/**导入时间（表示几月份工资）*/
	@Excel(name = "导入时间（表示几月份工资）", width = 15, format = "yyyy-MM")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM")
    @DateTimeFormat(pattern="yyyy-MM")
    @ApiModelProperty(value = "导入时间（表示几月份工资）")
    private Date month;
}
