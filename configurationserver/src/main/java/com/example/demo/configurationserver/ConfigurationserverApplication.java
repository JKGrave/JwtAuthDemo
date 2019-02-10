package com.example.demo.configurationserver;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigurationserverApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationserverApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
		pbeEnc.setAlgorithm("PBEWithMD5AndDES");
		pbeEnc.setPassword("test"); //2번 설정의 암호화 키를 입력

		String enc = pbeEnc.encrypt("http://jkyo11.iptime.org"); //암호화 할 내용
		System.out.println("enc = " + enc); //암호화 한 내용을 출력

		//테스트용 복호화
		String des = pbeEnc.decrypt(enc);
		System.out.println("des = " + des);
	}
}
