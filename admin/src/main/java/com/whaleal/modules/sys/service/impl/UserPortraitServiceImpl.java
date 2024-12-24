package com.whaleal.modules.sys.service.impl;

import com.whaleal.modules.sys.entity.po.ActivityEntity;
import com.whaleal.modules.sys.service.ActivityService;
import com.whaleal.modules.sys.service.InvoiceService;
import com.whaleal.modules.sys.service.SysUserService;
import com.whaleal.modules.sys.service.UserPortraitService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyz
 * @desc
 * @create: 2024-11-21 10:05
 **/
@Service
public class UserPortraitServiceImpl implements UserPortraitService {

    private final SysUserService sysUserService;

    private final ActivityService activityService;

    private final InvoiceService invoiceService;

    public UserPortraitServiceImpl(SysUserService sysUserService, ActivityService activityService, InvoiceService invoiceService) {
        this.sysUserService = sysUserService;
        this.activityService = activityService;
        this.invoiceService = invoiceService;
    }

    @Override
    public Map<String,List> findUserGrap(Date startTime, Date endTime) {
        Map<String,List> result = new HashMap<>();

        List<String> username = new ArrayList<>();
        List<Long> graped = new ArrayList<>();
        List<Long> complete = new ArrayList<>();

        List<Map<String, Object>> maps = activityService.listOrderBetween(1, startTime, endTime);
        for (Map<String,Object> map : maps){
            username.add(map.get("createName").toString());
//            Long graped1 = Long.valueOf(map.get("graped").toString());
//            Long complete1 = Long.valueOf(map.get("complete").toString());
//            if(graped1 + complete1 == 0){
//
//            }

            graped.add(Long.valueOf(map.get("graped").toString()));
            complete.add(Long.valueOf(map.get("complete").toString()));
        }

        // 计算这个时间段 已成单的记录

        result.put("username",username);
        result.put("graped",graped);
        result.put("complete",complete);
        return result;
    }

    @Override
    public Map listInvoicePrice(Date start, Date end) {
        Map result = new HashMap();

        List<Map<String, Object>> userInvoice = invoiceService.findUserInvoice(start, end);

        List<String> username = new ArrayList<>();
        List<BigDecimal> totalPrice = new ArrayList<>();
        for(Map<String ,Object> map : userInvoice){
            username.add(map.get("createName").toString());

            totalPrice.add(BigDecimal.valueOf(Double.parseDouble(map.get("totalPrice").toString())));
        }
        result.put("username",username);
        result.put("invoice",totalPrice);
        return result;
    }

    @Override
    public Map<String,List> getWeChatCount(Date start, Date end) {
        Map<String,List> result = new HashMap<>();

        List<String> username = new ArrayList<>();
        List<Long> wechat = new ArrayList<>();
        List<Long> phone = new ArrayList<>();

        List<Map<String, Object>> wechatCount = activityService.getWechatCount(start, end);
        for (Map<String ,Object> map : wechatCount){
            username.add(map.get("createName").toString());
            wechat.add(Long.parseLong(map.get("wechat").toString()));
            phone.add(Long.parseLong(map.get("phone").toString()));
        }
        result.put("username",username);
        result.put("wechat",wechat);
        result.put("phone",phone);

        return result;
    }

//    @Override
//    public Object getOrderRate(Date start, Date end) {
//        Map<String,List> result = new HashMap<>();
//
//        List<String> username = new ArrayList<>();
//        List<Long> rate = new ArrayList<>();
//
//        List<Map<String, Object>> maps = activityService.listOrderBetween(1, start, end);
//
//        for (Map<String,Object> map : maps){
//            username.add(map.get("createName").toString());
//
//            map.get("graped")
//        }
//
//        return result;
//    }
}
