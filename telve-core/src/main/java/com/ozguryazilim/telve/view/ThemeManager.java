package com.ozguryazilim.telve.view;

import java.io.InputStream;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
@SessionScoped
@Named
public class ThemeManager implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(ThemeManager.class);
    
    private String layoutName;
    private String layoutTemplate;
    private String templateHeader;
    private String templateFooter;
    private String templateMainBar;
    private String templateSideBar;
    
    
    private String homeTemplate;
    private String loginTemplate;
    private String forgotPasswordTemplate;
    
    private String theme;
    private String skin;

    @PostConstruct
    public void init(){
        //FIXME: Configden değerlere bakarak template isimleri toparlanacak. 
        //Dosyaların classpath'de olup olmadığına bakılacak ve eğer yoksa default degere fallback olacak.
        layoutName = ConfigResolver.getPropertyValue("themeManager.layout", "Default");
        
        LOG.info("Using Layout Theme : {}", layoutName);
        
        layoutTemplate = templateCheckOrFallback( layoutName , "baseTemplate.xhtml" );
        templateHeader = templateCheckOrFallback( layoutName , "templateHeader.xhtml" );
        templateFooter = templateCheckOrFallback( layoutName , "templateFooter.xhtml" );
        templateMainBar = templateCheckOrFallback( layoutName , "templateMainBar.xhtml" );
        templateSideBar = templateCheckOrFallback( layoutName , "templateSideBar.xhtml" );
        
        loginTemplate = templateCheckOrFallback( layoutName , "loginTemplate.xhtml" );
        forgotPasswordTemplate = templateCheckOrFallback(layoutName, "forgotPasswordTemplate.xhtml");
        
        homeTemplate = ConfigResolver.getPropertyValue("themeManager.home", "/simpleHome.xhtml");
        
    }
    
    private String templateCheckOrFallback( String name, String template ){
        String templatePath = "/layout/" + name + "/" + template;
        InputStream is = this.getClass().getResourceAsStream("/META-INF/resources" + templatePath);
        if( is == null ){
            templatePath = "/layout/base/"+ template;
            LOG.debug("Fallback Base Template : {}", templatePath);
        }
        
        return templatePath;
    }
    
    
    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getLayoutTemplate() {
        return layoutTemplate;
    }

    public void setLayoutTemplate(String layoutTemplate) {
        this.layoutTemplate = layoutTemplate;
    }

    public String getHomeTemplate() {
        return homeTemplate;
    }

    public void setHomeTemplate(String homeTemplate) {
        this.homeTemplate = homeTemplate;
    }

    public String getTemplateHeader() {
        return templateHeader;
    }

    public void setTemplateHeader(String templateHeader) {
        this.templateHeader = templateHeader;
    }

    public String getTemplateFooter() {
        return templateFooter;
    }

    public void setTemplateFooter(String templateFooter) {
        this.templateFooter = templateFooter;
    }

    public String getTemplateMainBar() {
        return templateMainBar;
    }

    public void setTemplateMainBar(String templateMainBar) {
        this.templateMainBar = templateMainBar;
    }

    public String getTemplateSideBar() {
        return templateSideBar;
    }

    public void setTemplateSideBar(String templateSideBar) {
        this.templateSideBar = templateSideBar;
    }

    public String getLoginTemplate() {
        return loginTemplate;
    }

    public void setLoginTemplate(String loginTemplate) {
        this.loginTemplate = loginTemplate;
    }

    public String getForgotPasswordTemplate() {
        return forgotPasswordTemplate;
    }

    public void setForgotPasswordTemplate(String forgotPasswordTemplate) {
        this.forgotPasswordTemplate = forgotPasswordTemplate;
    }
}
