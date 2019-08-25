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
import java.util.Map;

/**
 * 品牌controller
 */
//@RestController=@Controller+每个方法加上@ResponseBody

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


    /**
     * 添加品牌
     * @param brand
     * @return
     */
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


    /**
     * 根据id查询实体
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public TbBrand findOne(Long id){

        return brandService.findOne(id);
    }


    /**
     * 修改对象
     * @param brand
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody TbBrand brand){

        try {
            brandService.update(brand);
            return new Result(true,"修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"修改失败");
        }
    }


    /**
     * 根据id批量删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long [] ids){

        try {
            brandService.delete(ids);
            return new Result(true,"删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"删除失败");
        }

    }


    /**
     * 条件分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(@RequestBody TbBrand brand,int page,int size){


        return brandService.findPage(brand,page,size);

    }


    /**
     * 模板中品牌下拉列表
     * @return
     */
    @RequestMapping("/selectOptionList.do")
    public List<Map> selectOptionList(){
        return  brandService.selectOptionList();
    }


}
