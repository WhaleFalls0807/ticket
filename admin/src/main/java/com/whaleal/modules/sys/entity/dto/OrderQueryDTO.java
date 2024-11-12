//package com.whaleal.modules.sys.entity.dto;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.util.ObjectUtils;
//
//import java.lang.reflect.Field;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author lyz
// * @desc
// * @create: 2024-11-12 16:36
// **/
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Schema(title = "单子列表查询DTO")
//public class OrderQueryDTO {
//
//    @Schema(title = "负责人id")
//    private Long ownerId;
//
//    @Schema(title = "order状态")
//    private Long orderStatus;
//
//    @Schema(title = "查询关键字")
//    private String keyword;
//
//    @Schema(title = "0：公海 1：待成单 2：已成单",required = true)
//    private Integer deal;
//
//    @Schema(title = "排序字段，当前只支持单字段排序")
//    private String sortField;
//
//    @Schema(title = "是否升序")
//    private Boolean isAsc;
//
//    @Schema(title = "当前页码，从1开始",required = true)
//    private Integer page;
//
//    @Schema(title = "每页显示记录数",required = true)
//    private Integer limit;
//
//    @Schema(title = "开始时间")
//    private Date startDate;
//
//    @Schema(title = "结束时间")
//    private Date endDate;
//
//    public Map<String, Object> convertToMap() throws IllegalAccessException {
//        Map<String, Object> resultMap = new HashMap<>();
//
//        // 获取类的所有字段
//        Field[] fields = this.getClass().getDeclaredFields();
//
//        // 遍历字段
//        for (Field field : fields) {
//            field.setAccessible(true);  // 设置字段可访问
//            if(!ObjectUtils.isEmpty(field.get(this))){
//                resultMap.put(field.getName(), field.get(this));  // 将字段名和字段值放入Map中
//            }
//        }
//        return resultMap;
//    }
//
//}
