/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.uploader.tus;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.desair.tus.server.TusFileUploadService;
import org.apache.deltaspike.core.api.provider.BeanProvider;

/**
 *
 * @author oyas
 */
@WebServlet(urlPatterns = "/tus/*", asyncSupported = true)
public class TusFileUploadServlet extends HttpServlet{

    /**
     * Servlet API'den gelen tüm requestler TusFileUploadService'e iletiliyor.
     * 
     * TODO: burada ek kontrollere ihtiyaç olabilir.
     * 
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException 
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getFileUploadService().process(req, resp);
    }

        
    protected TusFileUploadService getFileUploadService(){
        return BeanProvider.getContextualReference(TusFileUploadService.class, true);
    }
    
}
