package com.whaleal.modules.sys.controller;

import com.whaleal.modules.sys.service.CustomerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lyz
 * @desc
 * @create: 2024-10-29 18:48
 **/
@RestController
@RequestMapping("/customer")
@Tag(name = "客户管理")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

}
