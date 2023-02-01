package org.jeecg.modules.demo.procurement.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.spire.pdf.PdfPageBase;
import org.jeecg.modules.system.model.DepartIdModel;
import org.jeecg.modules.system.service.ISysDepartService;
import org.jeecg.modules.system.service.ISysUserDepartService;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderItem;
import org.jeecg.modules.demo.procurement.entity.ProcurementOrderSupplier;
import org.jeecg.modules.demo.procurement.vo.ProcurementOrderSupplierPage;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderSupplierService;
import org.jeecg.modules.demo.procurement.service.IProcurementOrderItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;


 /**
 * @Description: 采购审批表
 * @Author: jeecg-boot
 * @Date:   2023-01-25
 * @Version: V1.0
 */
@Api(tags="采购审批表")
@RestController
@RequestMapping("/procurement/procurementOrderSupplier")
@Slf4j
public class ProcurementOrderSupplierController {
	@Autowired
	private ISysUserDepartService sysUserDepartService;
	 @Autowired
	 private ISysDepartService sysDepartService;
	@Autowired
	private IProcurementOrderSupplierService procurementOrderSupplierService;
	@Autowired
	private IProcurementOrderItemService procurementOrderItemService;
	
	/**
	 * 分页列表查询
	 *
	 * @param procurementOrderSupplier
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "采购审批表-分页列表查询")
	@ApiOperation(value="采购审批表-分页列表查询", notes="采购审批表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProcurementOrderSupplier>> queryPageList(ProcurementOrderSupplier procurementOrderSupplier,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProcurementOrderSupplier> queryWrapper = QueryGenerator.initQueryWrapper(procurementOrderSupplier, req.getParameterMap());
		Page<ProcurementOrderSupplier> page = new Page<ProcurementOrderSupplier>(pageNo, pageSize);
		IPage<ProcurementOrderSupplier> pageList = procurementOrderSupplierService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	 /**
	  * 通过单个
	  */
	 @AutoLog(value = "采购审批表-通过单个")
	 @ApiOperation(value="采购审批表-通过单个", notes="采购审批表-通过单个")
	 @GetMapping(value = "/pass")
	 public Result<String> pass(@RequestParam (name="id",required=true) String id){
		 UpdateWrapper<ProcurementOrderSupplier> updateWrapper = new UpdateWrapper<ProcurementOrderSupplier>();
		 updateWrapper.eq("id", id);
		 updateWrapper.set("state","2");
		 procurementOrderSupplierService.update(null,updateWrapper);
		 return Result.OK("通过成功！");
	 }
	 /**
	  * 查询当前用户和部门
	  *
	  */
	 @AutoLog(value = "查询当前用户和部门")
	 @ApiOperation(value="查询当前用户和部门", notes="查询当前用户和部门")
	 @GetMapping(value = "/user")
	 public Result<Map<String,String>> user(){
		 Result<Map<String,String>> result = new Result<Map<String,String>>();
		 Map<String,String> map = new HashMap<String,String>();
		 LoginUser sysUser = (LoginUser)SecurityUtils.getSubject().getPrincipal();
		 List<DepartIdModel> departIdModels = sysUserDepartService.queryDepartIdsOfUser(sysUser.getId());
		 StringBuilder departIds = new StringBuilder(new String());
		 for (DepartIdModel eachDepartIdModel :departIdModels){
			 departIds.append(eachDepartIdModel.getTitle());
		 }
		 System.out.println(departIds);
		 System.out.println(sysUser.getRealname());
		 map.put(sysUser.getRealname(), departIds.toString());
		 result.setResult(map);
		 return result;
	 }
	/**
	 *   添加
	 *
	 * @param procurementOrderSupplierPage
	 * @return
	 */
	@AutoLog(value = "采购审批表-添加")
	@ApiOperation(value="采购审批表-添加", notes="采购审批表-添加")
    //@RequiresPermissions("procurement:procurement_order_supplier:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProcurementOrderSupplierPage procurementOrderSupplierPage) {
		ProcurementOrderSupplier procurementOrderSupplier = new ProcurementOrderSupplier();
		BeanUtils.copyProperties(procurementOrderSupplierPage, procurementOrderSupplier);
		procurementOrderSupplierService.saveMain(procurementOrderSupplier, procurementOrderSupplierPage.getProcurementOrderItemList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param procurementOrderSupplierPage
	 * @return
	 */
	@AutoLog(value = "采购审批表-编辑")
	@ApiOperation(value="采购审批表-编辑", notes="采购审批表-编辑")
    //@RequiresPermissions("procurement:procurement_order_supplier:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProcurementOrderSupplierPage procurementOrderSupplierPage) {
		ProcurementOrderSupplier procurementOrderSupplier = new ProcurementOrderSupplier();
		BeanUtils.copyProperties(procurementOrderSupplierPage, procurementOrderSupplier);
		ProcurementOrderSupplier procurementOrderSupplierEntity = procurementOrderSupplierService.getById(procurementOrderSupplier.getId());
		if(procurementOrderSupplierEntity==null) {
			return Result.error("未找到对应数据");
		}
		procurementOrderSupplierService.updateMain(procurementOrderSupplier, procurementOrderSupplierPage.getProcurementOrderItemList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "采购审批表-通过id删除")
	@ApiOperation(value="采购审批表-通过id删除", notes="采购审批表-通过id删除")
    //@RequiresPermissions("procurement:procurement_order_supplier:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		procurementOrderSupplierService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "采购审批表-批量删除")
	@ApiOperation(value="采购审批表-批量删除", notes="采购审批表-批量删除")
    //@RequiresPermissions("procurement:procurement_order_supplier:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.procurementOrderSupplierService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购审批表-通过id查询")
	@ApiOperation(value="采购审批表-通过id查询", notes="采购审批表-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProcurementOrderSupplier> queryById(@RequestParam(name="id",required=true) String id) {
		ProcurementOrderSupplier procurementOrderSupplier = procurementOrderSupplierService.getById(id);
		if(procurementOrderSupplier==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(procurementOrderSupplier);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "采购材料明细表通过主表ID查询")
	@ApiOperation(value="采购材料明细表主表ID查询", notes="采购材料明细表-通主表ID查询")
	@GetMapping(value = "/queryProcurementOrderItemByMainId")
	public Result<List<ProcurementOrderItem>> queryProcurementOrderItemListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ProcurementOrderItem> procurementOrderItemList = procurementOrderItemService.selectByMainId(id);
		return Result.OK(procurementOrderItemList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param procurementOrderSupplier
    */
    //@RequiresPermissions("procurement:procurement_order_supplier:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProcurementOrderSupplier procurementOrderSupplier) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ProcurementOrderSupplier> queryWrapper = QueryGenerator.initQueryWrapper(procurementOrderSupplier, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<ProcurementOrderSupplier> procurementOrderSupplierList = procurementOrderSupplierService.list(queryWrapper);

      // Step.3 组装pageList
      List<ProcurementOrderSupplierPage> pageList = new ArrayList<ProcurementOrderSupplierPage>();
      for (ProcurementOrderSupplier main : procurementOrderSupplierList) {
          ProcurementOrderSupplierPage vo = new ProcurementOrderSupplierPage();
          BeanUtils.copyProperties(main, vo);
          List<ProcurementOrderItem> procurementOrderItemList = procurementOrderItemService.selectByMainId(main.getId());
          vo.setProcurementOrderItemList(procurementOrderItemList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "采购审批表列表");
      mv.addObject(NormalExcelConstants.CLASS, ProcurementOrderSupplierPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("采购审批表数据", "导出人:"+sysUser.getRealname(), "采购审批表"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("procurement:procurement_order_supplier:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<ProcurementOrderSupplierPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ProcurementOrderSupplierPage.class, params);
              for (ProcurementOrderSupplierPage page : list) {
                  ProcurementOrderSupplier po = new ProcurementOrderSupplier();
                  BeanUtils.copyProperties(page, po);
                  procurementOrderSupplierService.saveMain(po, page.getProcurementOrderItemList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
