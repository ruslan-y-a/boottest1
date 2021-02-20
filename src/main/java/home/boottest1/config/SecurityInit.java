package home.boottest1.config;

import javax.servlet.ServletContext;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.multipart.support.MultipartFilter;

@Configuration
public class SecurityInit extends AbstractSecurityWebApplicationInitializer {  
	/*
	@Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {
        insertFilters(servletContext, new MultipartFilter());
    }*/
}