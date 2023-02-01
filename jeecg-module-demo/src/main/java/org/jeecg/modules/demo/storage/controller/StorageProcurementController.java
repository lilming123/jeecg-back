package org.jeecg.modules.demo.storage.controller;

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
import org.jeecg.modules.demo.storage.entity.StorageProcurement;
import org.jeecg.modules.demo.storage.service.IStorageProcurementService;

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
 * @Description: 采购入库
 * @Author: jeecg-boot
 * @Date:   2023-01-27
 * @Version: V1.0
 */
@Api(tags="采购入库")
@RestController
@RequestMapping("/storage/storageProcurement")
@Slf4j
public class StorageProcurementController extends JeecgController<StorageProcurement, IStorageProcurementService> {
	@Autowired
	private IStorageProcurementService storageProcurementService;
	
	/**
	 * 分页列表查询
	 *
	 * @param storageProcurement
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购入库-分页列表查询")
	@ApiOperation(value="采购入库-分页列表查询", notes="采购入库-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<StorageProcurement>> queryPageList(StorageProcurement storageProcurement,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<StorageProcurement> queryWrapper = QueryGenerator.initQueryWrapper(storageProcurement, req.getParameterMap());
		Page<StorageProcurement> page = new Page<StorageProcurement>(pageNo, pageSize);
		IPage<StorageProcurement> pageList = storageProcurementService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param storageProcurement
	 * @return
	 */
	@AutoLog(value = "采购入库-添加")
	@ApiOperation(value="采购入库-添加", notes="采购入库-添加")
	//@RequiresPermissions("storage:storage_procurement:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody StorageProcurement storageProcurement) {
		storageProcurementService.save(storageProcurement);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param storageProcurement
	 * @return
	 */
	@AutoLog(value = "采购入库-编辑")
	@ApiOperation(value="采购入库-编辑", notes="采购入库-编辑")
	//@RequiresPermissions("storage:storage_procurement:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody StorageProcurement storageProcurement) {
		storageProcurementService.updateById(storageProcurement);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购入库-通过id删除")
	@ApiOperation(value="采购入库-通过id删除", notes="采购入库-通过id删除")
	//@RequiresPermissions("storage:storage_procurement:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		storageProcurementService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购入库-批量删除")
	@ApiOperation(value="采购入库-批量删除", notes="采购入库-批量删除")
	//@RequiresPermissions("storage:storage_procurement:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.storageProcurementService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购入库-通过id查询")
	@ApiOperation(value="采购入库-通过id查询", notes="采购入库-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<StorageProcurement> queryById(@RequestParam(name="id",required=true) String id) {
		StorageProcurement storageProcurement = storageProcurementService.getById(id);
		if(storageProcurement==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(storageProcurement);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param storageProcurement
    */
    //@RequiresPermissions("storage:storage_procurement:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, StorageProcurement storageProcurement) {
        return super.exportXls(request, storageProcurement, StorageProcurement.class, "采购入库");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("storage:storage_procurement:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, StorageProcurement.class);
    }

}
