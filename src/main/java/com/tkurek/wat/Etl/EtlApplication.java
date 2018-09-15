package com.tkurek.wat.Etl;

import com.tkurek.wat.Etl.mapper.SourceOneMapper;
import com.tkurek.wat.Etl.mapper.SourceTwoMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:/spring/application.xml")
public class EtlApplication implements CommandLineRunner {

	SourceOneMapper sourceOneMapper;
	SourceTwoMapper sourceTwoMapper;

	public static void main(String[] args) {
		SpringApplication.run(EtlApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		try {
			String a = this.sourceOneMapper.testSelect(2L);
			String b = this.sourceTwoMapper.testSelect2(9L);
			System.out.println(a + " " + b);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void setSourceOneMapper(SourceOneMapper sourceOneMapper) {
		this.sourceOneMapper = sourceOneMapper;
	}

	public void setSourceTwoMapper(SourceTwoMapper sourceTwoMapper) {
		this.sourceTwoMapper = sourceTwoMapper;
	}
}
