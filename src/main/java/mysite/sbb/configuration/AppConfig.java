package mysite.sbb.configuration;

import mysite.sbb.configuration.util.CommonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
	@Bean
	public CommonUtil commonUtil(){
		return new CommonUtil();
	}
}
