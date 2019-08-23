package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.entity.PageResult;
import com.pinyougou.entity.Result;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 品牌controller
 */

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查找所有
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<TbBrand> findAll(){
        return brandService.findAll();
    }


    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(int page,int size){
        return brandService.findPage(page,size);

    }


    @RequestMapping("/add.do")
    public Result add(@RequestBody TbBrand brand){
        try {
            brandService.add(brand);
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }

    }


}
