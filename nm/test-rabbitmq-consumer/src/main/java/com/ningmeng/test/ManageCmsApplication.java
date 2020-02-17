package com.ningmeng.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by 1 on 2020/2/11.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.ningmeng.test"})
//扫描common工程下的类
@ComponentScan(basePackages = {"com.ningmeng.framework"})
public class ManageCmsApplication {

   public static void main(String[] args){
       SpringApplication.run(ManageCmsApplication.class,args);
   }

}
