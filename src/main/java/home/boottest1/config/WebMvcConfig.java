package home.boottest1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafView;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebMvc
@ComponentScan("home.boottest1.controllers")
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired private org.thymeleaf.extras.springsecurity5.dialect.SpringSecurityDialect ssDialect;
    @Autowired private WebApplicationContext applicationContext;

    @Bean
 public ClassLoaderTemplateResolver templateResolver() {
        ClassLoaderTemplateResolver secondaryTemplateResolver = new ClassLoaderTemplateResolver();
        secondaryTemplateResolver.setPrefix("/templates/");
        secondaryTemplateResolver.setSuffix(".html");
        secondaryTemplateResolver.setTemplateMode(TemplateMode.HTML);
        secondaryTemplateResolver.setCharacterEncoding("UTF-8");
        secondaryTemplateResolver.setOrder(1);
        secondaryTemplateResolver.setCheckExistence(true);
        //secondaryTemplateResolver.addTemplateAlias("/templates/","/templates/index");
    //    secondaryTemplateResolver.setTemplateAliases();
        return secondaryTemplateResolver;
        }

    @Bean
    @Description("Thymeleaf template engine with Spring integration")
    public SpringTemplateEngine templateEngine() {

        var templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        Set<IDialect> dialectSet = new HashSet(); dialectSet.add(ssDialect);
        templateEngine.setAdditionalDialects(dialectSet);
        return templateEngine;
    }

    @Bean
    @Description("Thymeleaf view resolver")
    public ViewResolver viewResolver() {

        var viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");

        return viewResolver;
    }

    @Bean(name="indexbean")
    @Scope("prototype")
    public ThymeleafView indexBean() {
        ThymeleafView view = new ThymeleafView("index"); // templateName = 'index'
       // view.setMarkupSelector("content");
        return view;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:static/css/");// .setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:static/img/");// .setCachePeriod(31556926);
        registry.addResourceHandler("/myfiles/**").addResourceLocations("classpath:static/myfiles/");// .setCachePeriod(31556926);
    }
    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
             registry.addViewController("/index").setViewName("index.html");
        registry.addViewController("/about").setViewName("about.html");
        registry.addViewController("/error").setViewName("error.html");
        registry.addViewController("/403").setViewName("403.html");
        registry.addViewController("/login").setViewName("login.html");
        registry.addViewController("/logout").setViewName("logout.html");


    }

    ///////////////////////////////////////////////////////////////////////////////////////


}
