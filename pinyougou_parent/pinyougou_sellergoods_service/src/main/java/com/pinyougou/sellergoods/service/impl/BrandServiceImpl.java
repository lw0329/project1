package com.pinyougou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.dao.TbBrandMapper;
import com.pinyougou.entity.PageResult;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.List;

/**
 * 品牌业务的实现类
 */
@Service
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
}
