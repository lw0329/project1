package com.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import com.pinyougou.dao.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.dao.TbSpecificationMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.sellergoods.service.SpecificationService;

import com.pinyougou.entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;

	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		specificationMapper.insert(specification.getSpecification());//插入规格

		//循环插入选项
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();


		for (TbSpecificationOption option : specificationOptionList) {
			option.setSpecId(specification.getSpecification().getId());//设置规格id
			specificationOptionMapper.insert(option);//新增规格

			
		}
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){

		//保存修改的规格
		specificationMapper.updateByPrimaryKey(specification.getSpecification());

		//删除原有的规格选项
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(specification.getSpecification().getId());//指定规格id为条件

		specificationOptionMapper.deleteByExample(example);//根据规格的id 对字表进行删除


		//循环插入新的规格选项
		List<TbSpecificationOption> optionList = specification.getSpecificationOptionList();
		for (TbSpecificationOption specificationOption : optionList) {
			//将规格id设置到规格选项的外键中
			specificationOption.setSpecId(specification.getSpecification().getId());
			specificationOptionMapper.insert(specificationOption);
		}


	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){

		Specification specification=new Specification();
		//获取规格实体
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		specification.setSpecification(tbSpecification);

		//获取规格选项列表
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		List<TbSpecificationOption> specificationOptionList = specificationOptionMapper.selectByExample(example);
		specification.setSpecificationOptionList(specificationOptionList);

		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {

		//批量删除

		for(Long id:ids){
			//先删除规格选项然后在删除规格
			TbSpecificationOptionExample example = new TbSpecificationOptionExample();
			TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(example);


			//删除规格
			specificationMapper.deleteByPrimaryKey(id);

		}		
	}
	
	
	@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 模板中,规格下拉列表的显示
	 * @return
	 */
	@Override
	public List<Map> selectOptionList() {
		return specificationMapper.selectOptionList();
	}

}
