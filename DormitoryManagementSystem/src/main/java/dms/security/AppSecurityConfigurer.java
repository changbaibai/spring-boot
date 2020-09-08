package dms.security;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

import dms.service.UserService;

/**
 * 自定义Spring Security认证处理类的时候
 * 我们需要继承自WebSecurityConfigurerAdapter来完成，相关配置重写对应 方法即可。 
 * */
@Configuration
public class AppSecurityConfigurer extends WebSecurityConfigurerAdapter{

	// 依赖注入用户服务类
	@Autowired
    private UserService userService;
	
	//数据源 
	@Autowired
	DataSource dataSource;
	
	// 依赖注入加密接口
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	// 依赖注入用户认证接口
	@Autowired
    private AuthenticationProvider authenticationProvider;
	
	// 依赖注入认证处理成功类，验证用户成功后处理不同用户跳转到不同的页面
	@Autowired
	AppAuthenticationSuccessHandler appAuthenticationSuccessHandler;
	
	/*
	 *  BCryptPasswordEncoder是Spring Security提供的PasswordEncoder接口是实现类
	 *  用来创建密码的加密程序，避免明文存储密码到数据库
	 */
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	JdbcTokenRepositoryImpl jdbcTokenRepository() {
	    JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
	    jdbcTokenRepository.setDataSource(dataSource);
	    return jdbcTokenRepository;
	}
	 
	// DaoAuthenticationProvider是Spring Security提供AuthenticationProvider的实现
	@Bean
    public AuthenticationProvider authenticationProvider() {
		System.out.println("AuthenticationProvider authenticationProvider");
		// 创建DaoAuthenticationProvider对象
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        // 不要隐藏"用户未找到"的异常
        provider.setHideUserNotFoundExceptions(false);
        // 通过重写configure方法添加自定义的认证方式。
        provider.setUserDetailsService(userService);
        // 设置密码加密程序认证
        provider.setPasswordEncoder(passwordEncoder);
        
        return provider;
    }
	
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("AppSecurityConfigurer configure auth......");
    	// 设置认证方式。
    	auth.authenticationProvider(authenticationProvider);
    	

    }

    /**
     * 设置了登录页面，而且登录页面任何人都可以访问，然后设置了登录失败地址，也设置了注销请求，注销请求也是任何人都可以访问的。 
     * permitAll表示该请求任何人都可以访问，.anyRequest().authenticated(),表示其他的请求都必须要有权限认证。
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	System.out.println("AppSecurityConfigurer configure http......");
	   	 http.sessionManagement()
	     .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
	     .invalidSessionUrl("/invalidSession.html"); 
    	http.csrf().disable(); //取消放置csrf攻击的防护
        http.headers().frameOptions().disable();  //添加security对iframe的兼容
    	http.authorizeRequests()
    	// spring-security 5.0 之后需要过滤静态资源
    	.antMatchers("/login","/toRegist","/regist","/toIndex","/css/**","/js/**","/img/**","/main/**","/quar/**","/bpus/**","/np/**","/favicon.ico","/bootstrap-table/**","/bootstrap-3.3.7-dist/**","/superAdmin/queryNoticeInfo","/superAdmin/queryHealthRatingInfo","/superAdmin/listSliderInfoAll","/superAdmin/listSmallSliderInfoAll","/temp-rainy/**","/superAdmin/addRepair","/superAdmin/queryUerItemRecordInfo","/superAdmin/queryUerRepairInfo","/superAdmin/checkerUser").permitAll() 
	  	.antMatchers("/home","/superAdmin/addLeaveSchool","/superAdmin/addStudentId","/superAdmin/getLeaveSchool","/superAdmin/updateLeaveSchool","/superAdmin/getShowPage","/superAdmin/queryUerLeaveSchoolInfo","/superAdmin/queryMeetingOrderInfo","/superAdmin/addItemRecord","/superAdmin/queryUerItemRecordInfo","/superAdmin/checkerQuestionnaire","/superAdmin/addQuestionnaire").hasRole("STUDENT")  //学生可访问路径
	  	.antMatchers("/superAdmin/showPageTimeSheet","/superAdmin/getAdminDormInfo","/superAdmin/getOneDormStudentInfo","/superAdmin/addTimeSheet","superAdmin/addVisitInfo","superAdmin/showPageVisitInfo","/superAdmin/delsVisitInfo").hasAnyRole("ADMIN","SUPERADMIN")  //宿舍管理员可访问路径
	  	.antMatchers("/superAdmin/**").hasRole("SUPERADMIN")   //SUPERADMIN角色可访问路径	
	  	.anyRequest().authenticated()
	  	.and()
	  	.formLogin().loginPage("/login").successHandler(appAuthenticationSuccessHandler)
	  	.usernameParameter("username").passwordParameter("password")
	  	.and()
	  	.logout().permitAll()
	  	.and()
        .rememberMe()//记住我功能
        .userDetailsService(userService)//设置用户业务层
        .tokenRepository(jdbcTokenRepository())//设置持久化token
        .tokenValiditySeconds(168* 60 * 60)//记住登录7天(168小时 * 60分钟 * 60秒)
	  	.and()
	  	.exceptionHandling().accessDeniedPage("/accessDenied");
    	  // 禁用缓存
    	  http.headers().cacheControl();
    	  
    }


		
}
