package com.pinyougou.service;

import com.pinyougou.pojo.TbSeller;
import com.pinyougou.sellergoods.service.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {


    //注入sellerService,通过xml配置
    private SellerService sellerService;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * 认证类
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        //经历了
        System.out.println("经历了UserDetailsServiceImpl");

        //grantedAuthorities角色类的集合
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        //给集合添加角色
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_SELLER"));

        //得到商家对象
        TbSeller seller=sellerService.findOne(username);

        if (seller!=null){
            if (seller.getStatus().equals("1")){
                return new User(seller.getSellerId(),seller.getPassword(),grantedAuthorities);
            }


        }

        return null;



        //return new User(username,"123456",grantedAuthorities);
    }
}
