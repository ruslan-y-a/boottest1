package home.boottest1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"home.boottest1.security"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    @Autowired
    private home.boottest1.security.CustomAuthencationProvider customAuthencationProvider;

    @Bean public BCryptPasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder(); };

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //   auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
       auth.authenticationProvider(customAuthencationProvider);
    }
/*
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }
*/
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
    /*     http.csrf().disable();
        http
                .authorizeRequests()
                .anyRequest().permitAll();
*/
       http.csrf().disable()
        .authorizeRequests()
               .antMatchers("/users/**").hasRole("ADMIN")
                       .antMatchers("/roles/**").hasRole("ADMIN")
                       .antMatchers("/tasks/**").hasRole("USER")
                       .antMatchers("/files/**").hasRole("USER")
                .antMatchers("/").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/logout").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/index").permitAll()
                .antMatchers("/about").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/myfiles/**").permitAll()
                .antMatchers("/403").permitAll()
                .and()
                .formLogin().loginPage("/login")
                .loginProcessingUrl("/login.html")
                .usernameParameter("login")
                .passwordParameter("password")
                .defaultSuccessUrl("/index").failureUrl("/login?error=true").permitAll()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login").permitAll()
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

    }
/*
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**"); // #3
    }*/
}
