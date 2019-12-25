package dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootApplication指定这是一个 spring boot的应用程序.
@SpringBootApplication
public class App 
{
  public static void main( String[] args )
  {
  	// SpringApplication 用于从main方法启动Spring应用的类。
	  String s =new BCryptPasswordEncoder().encode("123");
	  System.out.print(s);
  	SpringApplication.run(App.class, args);
  }
}