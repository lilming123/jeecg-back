package org.jeecg.modules.demo.produce.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
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
import org.jeecg.modules.demo.produce.entity.ProduceProcessMaterial;
import org.jeecg.modules.demo.produce.entity.ProduceProcess;
import org.jeecg.modules.demo.produce.vo.ProduceProcessPage;
import org.jeecg.modules.demo.produce.service.IProduceProcessService;
import org.jeecg.modules.demo.produce.service.IProduceProcessMaterialService;
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
 * @Description: 生产流程管理
 * @Author: jeecg-boot
 * @Date:   2023-01-24
 * @Version: V1.0
 */
@Api(tags="生产流程管理")
@RestController
@RequestMapping("/produce/produceProcess")
@Slf4j
public class ProduceProcessController {
	@Autowired
	private IProduceProcessService produceProcessService;
	@Autowired
	private IProduceProcessMaterialService produceProcessMaterialService;
	
	/**
	 * 分页列表查询
	 *
	 * @param produceProcess
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "生产流程管理-分页列表查询")
	@ApiOperation(value="生产流程管理-分页列表查询", notes="生产流程管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ProduceProcess>> queryPageList(ProduceProcess produceProcess,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ProduceProcess> queryWrapper = QueryGenerator.initQueryWrapper(produceProcess, req.getParameterMap());
		Page<ProduceProcess> page = new Page<ProduceProcess>(pageNo, pageSize);
		page.addOrder(OrderItem.asc("goods_name"));
		page.addOrder(OrderItem.asc("precedence"));
		IPage<ProduceProcess> pageList = produceProcessService.page(page, queryWrapper);

		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param produceProcessPage
	 * @return
	 */
	@AutoLog(value = "生产流程管理-添加")
	@ApiOperation(value="生产流程管理-添加", notes="生产流程管理-添加")
    //@RequiresPermissions("produce:produce_process:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ProduceProcessPage produceProcessPage) {
		ProduceProcess produceProcess = new ProduceProcess();
		BeanUtils.copyProperties(produceProcessPage, produceProcess);
		produceProcessService.saveMain(produceProcess, produceProcessPage.getProduceProcessMaterialList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param produceProcessPage
	 * @return
	 */
	@AutoLog(value = "生产流程管理-编辑")
	@ApiOperation(value="生产流程管理-编辑", notes="生产流程管理-编辑")
    //@RequiresPermissions("produce:produce_process:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ProduceProcessPage produceProcessPage) {
		ProduceProcess produceProcess = new ProduceProcess();
		BeanUtils.copyProperties(produceProcessPage, produceProcess);
		ProduceProcess produceProcessEntity = produceProcessService.getById(produceProcess.getId());
		if(produceProcessEntity==null) {
			return Result.error("未找到对应数据");
		}
		produceProcessService.updateMain(produceProcess, produceProcessPage.getProduceProcessMaterialList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "生产流程管理-通过id删除")
	@ApiOperation(value="生产流程管理-通过id删除", notes="生产流程管理-通过id删除")
    //@RequiresPermissions("produce:produce_process:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		produceProcessService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "生产流程管理-批量删除")
	@ApiOperation(value="生产流程管理-批量删除", notes="生产流程管理-批量删除")
    //@RequiresPermissions("produce:produce_process:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.produceProcessService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产流程管理-通过id查询")
	@ApiOperation(value="生产流程管理-通过id查询", notes="生产流程管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ProduceProcess> queryById(@RequestParam(name="id",required=true) String id) {
		ProduceProcess produceProcess = produceProcessService.getById(id);
		if(produceProcess==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(produceProcess);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "生产流程材料表通过主表ID查询")
	@ApiOperation(value="生产流程材料表主表ID查询", notes="生产流程材料表-通主表ID查询")
	@GetMapping(value = "/queryProduceProcessMaterialByMainId")
	public Result<List<ProduceProcessMaterial>> queryProduceProcessMaterialListByMainId(@RequestParam(name="id",required=true) String id) {
		List<ProduceProcessMaterial> produceProcessMaterialList = produceProcessMaterialService.selectByMainId(id);
		return Result.OK(produceProcessMaterialList);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param produceProcess
    */
    //@RequiresPermissions("produce:produce_process:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ProduceProcess produceProcess) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<ProduceProcess> queryWrapper = QueryGenerator.initQueryWrapper(produceProcess, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

      //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
         List<String> selectionList = Arrays.asList(selections.split(","));
         queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<ProduceProcess> produceProcessList = produceProcessService.list(queryWrapper);

      // Step.3 组装pageList
      List<ProduceProcessPage> pageList = new ArrayList<ProduceProcessPage>();
      for (ProduceProcess main : produceProcessList) {
          ProduceProcessPage vo = new ProduceProcessPage();
          BeanUtils.copyProperties(main, vo);
          List<ProduceProcessMaterial> produceProcessMaterialList = produceProcessMaterialService.selectByMainId(main.getId());
          vo.setProduceProcessMaterialList(produceProcessMaterialList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "生产流程管理列表");
      mv.addObject(NormalExcelConstants.CLASS, ProduceProcessPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("生产流程管理数据", "导出人:"+sysUser.getRealname(), "生产流程管理"));
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
    //@RequiresPermissions("produce:produce_process:importExcel")
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
              List<ProduceProcessPage> list = ExcelImportUtil.importExcel(file.getInputStream(), ProduceProcessPage.class, params);
              for (ProduceProcessPage page : list) {
                  ProduceProcess po = new ProduceProcess();
                  BeanUtils.copyProperties(page, po);
                  produceProcessService.saveMain(po, page.getProduceProcessMaterialList());
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
