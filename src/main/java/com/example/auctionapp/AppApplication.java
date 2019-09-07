package com.example.auctionapp;

import lombok.extern.slf4j.Slf4j;
//import org.mybatis.spring.annotation.MapperScan;
import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Slf4j
@EnableTransactionManagement
@MapperScan("com.example.auctionapp.dao")
@EnableAsync
public class AppApplication {

	public static void main(String[] args) {
		log.info("启动开始......");
		SpringApplication.run(AppApplication.class, args);
		log.info("启动完成......");
	}
}
