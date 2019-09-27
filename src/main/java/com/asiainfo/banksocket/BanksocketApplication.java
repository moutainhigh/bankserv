package com.asiainfo.banksocket;

import com.asiainfo.banksocket.netty.StartSocketServer;
import com.asiainfo.banksocket.service.IBankService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@MapperScan(basePackages = {"com.asiainfo.banksocket.mapper"})
@ComponentScan(basePackages = {"com.asiainfo.banksocket.*"})
//@ImportResource(locations="httpUrl_jl.xml")
public class BanksocketApplication implements CommandLineRunner {

    StartSocketServer startSocketServer=new StartSocketServer();

    @Autowired
    IBankService bankService;

    public static void main(String[] args) {

        SpringApplication.run(BanksocketApplication.class, args);
    }

    @Override
    public void run(String... args) {
        startSocketServer.startServer();
    }
}
