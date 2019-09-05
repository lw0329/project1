package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.dao.TbBrandMapper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

/**
 * 品牌业务的实现类
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;


    /**
     * 查询所有品牌
     * @return
     */
    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {


        PageHelper.startPage(pageNum,pageSize);

        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);//强转


        return new PageResult(page.getTotal(),page.getResult());
    }


    /**
     * 添加品牌
     * @param brand
     */
    @Override
    public void add(TbBrand brand) {

        brandMapper.insert(brand);
    }



    /**
     * 根据id查询对象
     * @param id
     * @return
     */
    @Override
    public TbBrand findOne(Long id) {

        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改品牌对象
     * @param brand
     */
    @Override
    public void update(TbBrand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 根据id删除
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {

            brandMapper.deleteByPrimaryKey(id);
        }
    }

    /**
     * 分页条件查询
     * @param brand 查询条件封装的对象
     * @param pageNum 当前页
     * @param pageSize 每页显示条数
     * @return
     */
    @Override
    public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {

        //分页助手
        PageHelper.startPage(pageNum,pageSize);

        //创建分页条件
        TbBrandExample example = new TbBrandExample();

        TbBrandExample.Criteria criteria = example.createCriteria();

        if (brand!=null){
            if (brand.getName()!=null&&brand.getName().length()>0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }
            if (brand.getFirstChar()!=null&&brand.getFirstChar().length()>0){
                criteria.andFirstCharEqualTo(brand.getFirstChar());
            }
        }

        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Map> selectOptionList() {
        return brandMapper.selectOptionList();
    }


}
