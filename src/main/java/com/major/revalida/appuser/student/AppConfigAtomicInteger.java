package com.major.revalida.appuser.student;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfigAtomicInteger {

	 @Bean
	    public AtomicInteger sequence() {
	        return new AtomicInteger(1);
	    }
	
}
