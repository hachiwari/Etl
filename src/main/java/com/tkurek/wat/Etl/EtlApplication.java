package com.tkurek.wat.Etl;

import com.tkurek.wat.Etl.mapper.TestMapper;
import com.tkurek.wat.Etl.mapper.TwoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class EtlApplication implements CommandLineRunner {

	@Autowired
	TestMapper testMapper;

	@Autowired
	TwoMapper twoMapper;

	public static void main(String[] args) {
		SpringApplication.run(EtlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			String a = testMapper.testSelect(2L);
			String b = twoMapper.getVersion();
			System.out.println(a + " " + b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
