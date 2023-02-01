package org.jeecg.modules.demo.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.demo.base.entity.BaseGoodsType;
import org.jeecg.modules.demo.base.mapper.BaseGoodsTypeMapper;
import org.jeecg.modules.demo.base.service.IBaseGoodsTypeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 商品分类
 * @Author: jeecg-boot
 * @Date:   2023-01-23
 * @Version: V1.0
 */
@Service
public class BaseGoodsTypeServiceImpl extends ServiceImpl<BaseGoodsTypeMapper, BaseGoodsType> implements IBaseGoodsTypeService {

	@Override
	public void addBaseGoodsType(BaseGoodsType baseGoodsType) {
	   //新增时设置hasChild为0
	    baseGoodsType.setHasChild(IBaseGoodsTypeService.NOCHILD);
		if(oConvertUtils.isEmpty(baseGoodsType.getPid())){
			baseGoodsType.setPid(IBaseGoodsTypeService.ROOT_PID_VALUE);
		}else{
			//如果当前节点父ID不为空 则设置父节点的hasChildren 为1
			BaseGoodsType parent = baseMapper.selectById(baseGoodsType.getPid());
			if(parent!=null && !"1".equals(parent.getHasChild())){
				parent.setHasChild("1");
				baseMapper.updateById(parent);
			}
		}
		baseMapper.insert(baseGoodsType);
	}
	
	@Override
	public void updateBaseGoodsType(BaseGoodsType baseGoodsType) {
		BaseGoodsType entity = this.getById(baseGoodsType.getId());
		if(entity==null) {
			throw new JeecgBootException("未找到对应实体");
		}
		String old_pid = entity.getPid();
		String new_pid = baseGoodsType.getPid();
		if(!old_pid.equals(new_pid)) {
			updateOldParentNode(old_pid);
			if(oConvertUtils.isEmpty(new_pid)){
				baseGoodsType.setPid(IBaseGoodsTypeService.ROOT_PID_VALUE);
			}
			if(!IBaseGoodsTypeService.ROOT_PID_VALUE.equals(baseGoodsType.getPid())) {
				baseMapper.updateTreeNodeStatus(baseGoodsType.getPid(), IBaseGoodsTypeService.HASCHILD);
			}
		}
		baseMapper.updateById(baseGoodsType);
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteBaseGoodsType(String id) throws JeecgBootException {
		//查询选中节点下所有子节点一并删除
        id = this.queryTreeChildIds(id);
        if(id.indexOf(",")>0) {
            StringBuffer sb = new StringBuffer();
            String[] idArr = id.split(",");
            for (String idVal : idArr) {
                if(idVal != null){
                    BaseGoodsType baseGoodsType = this.getById(idVal);
                    String pidVal = baseGoodsType.getPid();
                    //查询此节点上一级是否还有其他子节点
                    List<BaseGoodsType> dataList = baseMapper.selectList(new QueryWrapper<BaseGoodsType>().eq("pid", pidVal).notIn("id",Arrays.asList(idArr)));
                    boolean flag = (dataList == null || dataList.size() == 0) && !Arrays.asList(idArr).contains(pidVal) && !sb.toString().contains(pidVal);
                    if(flag){
                        //如果当前节点原本有子节点 现在木有了，更新状态
                        sb.append(pidVal).append(",");
                    }
                }
            }
            //批量删除节点
            baseMapper.deleteBatchIds(Arrays.asList(idArr));
            //修改已无子节点的标识
            String[] pidArr = sb.toString().split(",");
            for(String pid : pidArr){
                this.updateOldParentNode(pid);
            }
        }else{
            BaseGoodsType baseGoodsType = this.getById(id);
            if(baseGoodsType==null) {
                throw new JeecgBootException("未找到对应实体");
            }
            updateOldParentNode(baseGoodsType.getPid());
            baseMapper.deleteById(id);
        }
	}
	
	@Override
    public List<BaseGoodsType> queryTreeListNoPage(QueryWrapper<BaseGoodsType> queryWrapper) {
        List<BaseGoodsType> dataList = baseMapper.selectList(queryWrapper);
        List<BaseGoodsType> mapList = new ArrayList<>();
        for(BaseGoodsType data : dataList){
            String pidVal = data.getPid();
            //递归查询子节点的根节点
            if(pidVal != null && !IBaseGoodsTypeService.NOCHILD.equals(pidVal)){
                BaseGoodsType rootVal = this.getTreeRoot(pidVal);
                if(rootVal != null && !mapList.contains(rootVal)){
                    mapList.add(rootVal);
                }
            }else{
                if(!mapList.contains(data)){
                    mapList.add(data);
                }
            }
        }
        return mapList;
    }

    @Override
    public List<SelectTreeModel> queryListByCode(String parentCode) {
        String pid = ROOT_PID_VALUE;
        if (oConvertUtils.isNotEmpty(parentCode)) {
            LambdaQueryWrapper<BaseGoodsType> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BaseGoodsType::getPid, parentCode);
            List<BaseGoodsType> list = baseMapper.selectList(queryWrapper);
            if (list == null || list.size() == 0) {
                throw new JeecgBootException("该编码【" + parentCode + "】不存在，请核实!");
            }
            if (list.size() > 1) {
                throw new JeecgBootException("该编码【" + parentCode + "】存在多个，请核实!");
            }
            pid = list.get(0).getId();
        }
        return baseMapper.queryListByPid(pid, null);
    }

    @Override
    public List<SelectTreeModel> queryListByPid(String pid) {
        if (oConvertUtils.isEmpty(pid)) {
            pid = ROOT_PID_VALUE;
        }
        return baseMapper.queryListByPid(pid, null);
    }

	/**
	 * 根据所传pid查询旧的父级节点的子节点并修改相应状态值
	 * @param pid
	 */
	private void updateOldParentNode(String pid) {
		if(!IBaseGoodsTypeService.ROOT_PID_VALUE.equals(pid)) {
			Long count = baseMapper.selectCount(new QueryWrapper<BaseGoodsType>().eq("pid", pid));
			if(count==null || count<=1) {
				baseMapper.updateTreeNodeStatus(pid, IBaseGoodsTypeService.NOCHILD);
			}
		}
	}

	/**
     * 递归查询节点的根节点
     * @param pidVal
     * @return
     */
    private BaseGoodsType getTreeRoot(String pidVal){
        BaseGoodsType data =  baseMapper.selectById(pidVal);
        if(data != null && !IBaseGoodsTypeService.ROOT_PID_VALUE.equals(data.getPid())){
            return this.getTreeRoot(data.getPid());
        }else{
            return data;
        }
    }

    /**
     * 根据id查询所有子节点id
     * @param ids
     * @return
     */
    private String queryTreeChildIds(String ids) {
        //获取id数组
        String[] idArr = ids.split(",");
        StringBuffer sb = new StringBuffer();
        for (String pidVal : idArr) {
            if(pidVal != null){
                if(!sb.toString().contains(pidVal)){
                    if(sb.toString().length() > 0){
                        sb.append(",");
                    }
                    sb.append(pidVal);
                    this.getTreeChildIds(pidVal,sb);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 递归查询所有子节点
     * @param pidVal
     * @param sb
     * @return
     */
    private StringBuffer getTreeChildIds(String pidVal,StringBuffer sb){
        List<BaseGoodsType> dataList = baseMapper.selectList(new QueryWrapper<BaseGoodsType>().eq("pid", pidVal));
        if(dataList != null && dataList.size()>0){
            for(BaseGoodsType tree : dataList) {
                if(!sb.toString().contains(tree.getId())){
                    sb.append(",").append(tree.getId());
                }
                this.getTreeChildIds(tree.getId(),sb);
            }
        }
        return sb;
    }

}
