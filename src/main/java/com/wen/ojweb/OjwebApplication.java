package com.wen.ojweb;

import com.wen.ojweb.config.UserLoginInterceptor;
import com.wen.ojweb.service.OjuserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@SpringBootApplication
@Configuration
@EnableCaching
public class OjwebApplication extends WebMvcConfigurerAdapter {
	@Autowired
	private UserLoginInterceptor userLoginInterceptor;

	@Autowired
	OjuserService ojuserService;

	/**
	 * 注册拦截器
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		super.addInterceptors(registry);
		//添加对用户是否登录的拦截器，并添加过滤项、排除项
		registry.addInterceptor(userLoginInterceptor).addPathPatterns("/**")
				.excludePathPatterns("/css/**","/js/**","/img/**","/font/**","/images/**","/lay/**","/favicon.ico","/layui.all.js","/layui.js")//排除样式、脚本、图片等资源文件
				.excludePathPatterns("/")//排除登录页面
				.excludePathPatterns("/login")//排除登录操作
				.excludePathPatterns("/registerPage")//排除注册页面
				.excludePathPatterns("/register")//排除注册操作
				.excludePathPatterns("/countdownPage")//排除倒计时页面
				.excludePathPatterns("/uploadfile");
	}
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		//登录首页
		registry.addViewController("/").setViewName("login");
//		registry.addViewController("/main").setViewName("/user/main");
//        registry.addViewController("/adminMain").setViewName("/admin/main");
		registry.addViewController("/countdownPage").setViewName("countdown");
		registry.addViewController("/registerPage").setViewName("register");


		registry.addViewController("/admin/main").setViewName("/admin/main");
		registry.addViewController("/admin/info/questionInfo").setViewName("/admin/info/questionInfo");
		registry.addViewController("/admin/info/AddQuestionInfo").setViewName("/admin/info/AddQuestionInfo");

		registry.addViewController("/user/main").setViewName("/user/main");
//		registry.addViewController("/").setViewName("/admin/main");
		registry.addViewController("/user/index").setViewName("/user/index");
		registry.addViewController("/admin/index").setViewName("/admin/index");
		registry.addViewController("/admin/info/userInfo").setViewName("/admin/info/userInfo");
		registry.addViewController("/admin/info/AddUserInfo").setViewName("/admin/info/AddUserInfo");
	}

	public static void main(String[] args) {
		SpringApplication.run(OjwebApplication.class, args);
	}
}
