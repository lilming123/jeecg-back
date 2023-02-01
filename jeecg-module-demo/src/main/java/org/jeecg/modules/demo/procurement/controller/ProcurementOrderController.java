package org.jeecg.modules.demo.procurement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrder;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 生成采购订单
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@Api(tags="生成采购订单")
@RestController
@RequestMapping("/procurement/procurementOrder")
@Slf4j
public class ProcurementOrderController extends JeecgController<ProcurementOrder, IProcurementOrderService> {
	@Autowired
	private IProcurementOrderService procurementOrderService;
	
	/**
	 * 分页列表查询
	 *
	 * @param procurementOrder
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "生成采购订单-分页列表查询")
	@ApiOperation(value="生成采购订单-分页列表查询", notes="生成采购订单-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProcurementOrder>> queryPageList(ProcurementOrder procurementOrder,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProcurementOrder> queryWrapper = QueryGenerator.initQueryWrapper(procurementOrder, req.getParameterMap());
		Page<ProcurementOrder> page = new Page<ProcurementOrder>(pageNo, pageSize);
		IPage<ProcurementOrder> pageList = procurementOrderService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param procurementOrder
	 * @return
	 */
	@AutoLog(value = "生成采购订单-添加")
	@ApiOperation(value="生成采购订单-添加", notes="生成采购订单-添加")
	//@RequiresPermissions("procurement:procurement_order:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody List<ProcurementOrder> procurementOrderList) {
		procurementOrderService.orderItem(procurementOrderList);
		for (ProcurementOrder eachProcurementOrder : procurementOrderList){
			procurementOrderService.save(eachProcurementOrder);
		}
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param procurementOrder
	 * @return
	 */
	@AutoLog(value = "生成采购订单-编辑")
	@ApiOperation(value="生成采购订单-编辑", notes="生成采购订单-编辑")
	//@RequiresPermissions("procurement:procurement_order:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProcurementOrder procurementOrder) {
		procurementOrderService.updateById(procurementOrder);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "生成采购订单-通过id删除")
	@ApiOperation(value="生成采购订单-通过id删除", notes="生成采购订单-通过id删除")
	//@RequiresPermissions("procurement:procurement_order:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		procurementOrderService.removeById(id);
		return Result.OK("撤销成功!");
	}
	 /**
	  *   通过id通过
	  *
	  * @param id
	  * @return
	  */
	 @AutoLog(value = "生成采购订单-通过id通过")
	 @ApiOperation(value="生成采购订单-通过id通过", notes="生成采购订单-通过id通过")
	 //@RequiresPermissions("procurement:procurement_order:delete")
	 @GetMapping(value = "/pass")
	 public Result<String> pass(@RequestParam(name="id",required=true) String id) {
		 System.out.println(id);
		 ProcurementOrder procurementOrder = procurementOrderService.getById(id);
		 procurementOrder.setState("2");
		 procurementOrderService.updateById(procurementOrder);
		 return Result.OK("订单已通过,并生成采购明细!");
	 }
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "生成采购订单-批量删除")
	@ApiOperation(value="生成采购订单-批量删除", notes="生成采购订单-批量删除")
	//@RequiresPermissions("procurement:procurement_order:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.procurementOrderService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生成采购订单-通过id查询")
	@ApiOperation(value="生成采购订单-通过id查询", notes="生成采购订单-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProcurementOrder> queryById(@RequestParam(name="id",required=true) String id) {
		ProcurementOrder procurementOrder = procurementOrderService.getById(id);
		if(procurementOrder==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(procurementOrder);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param procurementOrder
    */
    //@RequiresPermissions("procurement:procurement_order:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProcurementOrder procurementOrder) {
        return super.exportXls(request, procurementOrder, ProcurementOrder.class, "生成采购订单");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("procurement:procurement_order:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ProcurementOrder.class);
    }

}
