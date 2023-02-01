package org.jeecg.modules.demo.procurement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderItemService;

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
 * @Description: 采购材料明细表
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@Api(tags="采购材料明细表")
@RestController
@RequestMapping("/procurement/procurementOrderItem")
@Slf4j
public class ProcurementOrderItemController extends JeecgController<ProcurementOrderItem, IProcurementOrderItemService> {
	@Autowired
	private IProcurementOrderItemService procurementOrderItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param procurementOrderItem
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购材料明细表-分页列表查询")
	@ApiOperation(value="采购材料明细表-分页列表查询", notes="采购材料明细表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProcurementOrderItem>> queryPageList(ProcurementOrderItem procurementOrderItem,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProcurementOrderItem> queryWrapper = QueryGenerator.initQueryWrapper(procurementOrderItem, req.getParameterMap());
		Page<ProcurementOrderItem> page = new Page<ProcurementOrderItem>(pageNo, pageSize);
		IPage<ProcurementOrderItem> pageList = procurementOrderItemService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param procurementOrderItem
	 * @return
	 */
	@AutoLog(value = "采购材料明细表-添加")
	@ApiOperation(value="采购材料明细表-添加", notes="采购材料明细表-添加")
	//@RequiresPermissions("procurement:procurement_order_item:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProcurementOrderItem procurementOrderItem) {
		procurementOrderItemService.save(procurementOrderItem);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param procurementOrderItem
	 * @return
	 */
	@AutoLog(value = "采购材料明细表-编辑")
	@ApiOperation(value="采购材料明细表-编辑", notes="采购材料明细表-编辑")
	//@RequiresPermissions("procurement:procurement_order_item:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProcurementOrderItem procurementOrderItem) {
		procurementOrderItemService.updateById(procurementOrderItem);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购材料明细表-通过id删除")
	@ApiOperation(value="采购材料明细表-通过id删除", notes="采购材料明细表-通过id删除")
	//@RequiresPermissions("procurement:procurement_order_item:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		procurementOrderItemService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购材料明细表-批量删除")
	@ApiOperation(value="采购材料明细表-批量删除", notes="采购材料明细表-批量删除")
	//@RequiresPermissions("procurement:procurement_order_item:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.procurementOrderItemService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购材料明细表-通过id查询")
	@ApiOperation(value="采购材料明细表-通过id查询", notes="采购材料明细表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProcurementOrderItem> queryById(@RequestParam(name="id",required=true) String id) {
		ProcurementOrderItem procurementOrderItem = procurementOrderItemService.getById(id);
		if(procurementOrderItem==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(procurementOrderItem);
	}

	 /**
	  * 通过id入库
	  * @param id
	  * @return
	  */
	 @ApiOperation(value="采购材料明细表-通过id入库", notes="采购材料明细表-通过id入库")
	 @GetMapping(value = "/inStorage")
	 public Result<String> inStorage(@RequestParam(name="id",required=true) String id) {
		 ProcurementOrderItem procurementOrderItem = procurementOrderItemService.getById(id);
		 procurementOrderItem.setState("2");
		 procurementOrderItemService.updateById(procurementOrderItem);

		 return Result.OK("入库成功!");
	 }
	 /**
	  * 通过id反入库
	  * @param id
	  * @return
	  */
	 @ApiOperation(value="采购材料明细表-通过id反入库", notes="采购材料明细表-通过id反入库")
	 @GetMapping(value = "/notInStorage")
	 public Result<String> notInStorage(@RequestParam(name="id",required=true) String id) {
		 ProcurementOrderItem procurementOrderItem = procurementOrderItemService.getById(id);
		 if (Objects.equals(procurementOrderItem.getState(), "3")) {
			 return Result.error("该材料已领料，不能反入库");
		 }else {
			 procurementOrderItem.setState("1");
			 procurementOrderItemService.updateById(procurementOrderItem);
			 return Result.OK("反入库成功!");
			 //对库存表要进行   修改
		 }
	 }


    /**
    * 导出excel
    *
    * @param request
    * @param procurementOrderItem
    */
    //@RequiresPermissions("procurement:procurement_order_item:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProcurementOrderItem procurementOrderItem) {
        return super.exportXls(request, procurementOrderItem, ProcurementOrderItem.class, "采购材料明细表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("procurement:procurement_order_item:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ProcurementOrderItem.class);
    }

}
