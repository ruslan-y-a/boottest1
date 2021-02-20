package home.boottest1;

import home.boottest1.config.WebMvcConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
/*
@EnableAutoConfiguration(exclude = { //
		WebMvcConfigurer.class, //
		})  */
@EnableAutoConfiguration
@SpringBootApplication(scanBasePackages={"home.boottest1"}) //,excludeName={"home.boottest0.controllers"}
//@Import({WebMvcConfig.class})
public class Boottest1Application {
	@Autowired
	private static ServletContext servletContext;

	public static void main(String[] args) {
		SpringApplication.run(Boottest1Application.class, args);

		AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
		appContext.register(WebMvcConfig.class);
		//   appContext.register(WebSecurityConfig.class);
		servletContext.addListener(new ContextLoaderListener(appContext));
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
				"SpringDispatcher", new DispatcherServlet(appContext));
		dispatcher.setLoadOnStartup(1);
		//dispatcher.addMapping("/*");
		dispatcher.addMapping("/");
	//	dispatcher.addMapping("/index");
	}


}
