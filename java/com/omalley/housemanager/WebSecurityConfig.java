package com.omalley.housemanager;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    Environment env;

    @Autowired
    private AuthenticationService authenticationService;


    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        http.authorizeRequests().antMatchers("/css/**").permitAll().anyRequest().authenticated().and().authorizeRequests().antMatchers("/**").hasAnyRole("ADMIN",
                                                                                                                                                         "USER").and().formLogin().loginPage("/login").permitAll().and().logout().permitAll().and().headers().cacheControl().disable().csrf().disable();

    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
    {
        ShaPasswordEncoder encoder = new ShaPasswordEncoder();
        auth.userDetailsService(this.authenticationService).passwordEncoder(encoder);
    }


    @Bean
    public DataSource getDataSource()
    {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(this.env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(this.env.getProperty("spring.datasource.url"));
        dataSource.setUsername(this.env.getProperty("spring.datasource.username"));
        dataSource.setPassword(this.env.getProperty("spring.datasource.password"));
        return dataSource;
    }
}