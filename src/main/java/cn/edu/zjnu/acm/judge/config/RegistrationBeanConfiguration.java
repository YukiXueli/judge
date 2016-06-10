/*
 * Copyright 2015 zhanhb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.edu.zjnu.acm.judge.config;

import cn.edu.zjnu.acm.judge.service.UserDetailService;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author zhanhb
 */
@Configuration
public class RegistrationBeanConfiguration {

    @Bean
    public ServletRegistrationBean connectorServlet(MultipartConfigElement multipartConfigElement) {
        Servlet servlet = new ConnectorServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "/ckfinder/core/connector/java/connector.java");
        servletRegistrationBean.addInitParameter("XMLConfig", "/WEB-INF/config.xml");
        servletRegistrationBean.addInitParameter("debug", "false");
        servletRegistrationBean.setMultipartConfig(multipartConfigElement);
        return servletRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean kaptcha() {
        Servlet servlet = new KaptchaServlet();
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(servlet, "/images/rand.jpg");
        servletRegistrationBean.addInitParameter(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_CONFIG_KEY, "word");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean sitemesh() {
        Filter filter = new com.opensymphony.sitemesh.webapp.SiteMeshFilter();
        return new FilterRegistrationBean(filter);
    }

}

@SuppressWarnings("MultipleTopLevelClassesInFile")
class ConnectorServlet extends com.ckfinder.connector.ConnectorServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UserDetailService.requireAdminLoginned(request);
        super.service(request, response);
    }

}

@SuppressWarnings("MultipleTopLevelClassesInFile")
class KaptchaServlet extends com.google.code.kaptcha.servlet.KaptchaServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getSession(); // create session for kaptcha servlet
        super.doGet(req, resp);
    }

}
