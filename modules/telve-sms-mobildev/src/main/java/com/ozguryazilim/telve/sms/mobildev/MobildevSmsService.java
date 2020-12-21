package com.ozguryazilim.telve.sms.mobildev;

import com.ozguryazilim.telve.channel.sms.SmsService;
import com.ozguryazilim.telve.utils.StringUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SMS Channel Mobildev servisleri için implementasyon
 *
 * @author Hakan Uygun
 */
public class MobildevSmsService implements SmsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobildevSmsService.class);

    @Override
    public void send(String to, String body) {
        if ("POST".equals(ConfigResolver.getPropertyValue("mobildev.apiMode"))) {
            try {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                try {
                    HttpPost request = new HttpPost(ConfigResolver.getPropertyValue("mobildev.api") + "/sms?access_token=" + ConfigResolver.getPropertyValue("mobildev.token"));
                    StringEntity params = new StringEntity(
                            buildSmsBody(to, body),
                            ContentType.APPLICATION_JSON
                    );
                    request.setEntity(params);
                    HttpResponse rawResponse = httpClient.execute(request);
                    //FIXME: handle response here...
                    LOGGER.debug("Response : {}", rawResponse.toString());

                } finally {
                    httpClient.close();
                }
            } catch (IOException ioe) {
                LOGGER.error("SMS Not Send", ioe);
            }
        } else if ("POSTXML".equals(ConfigResolver.getPropertyValue("mobildev.apiMode"))) {
            try {
                CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                try {
                    HttpPost request = new HttpPost(ConfigResolver.getPropertyValue("mobildev.api"));
                    StringEntity params = new StringEntity(
                            buildXMLSmsBody(to, body),
                            ContentType.APPLICATION_SOAP_XML
                    );
                    request.setEntity(params);
                    HttpResponse rawResponse = httpClient.execute(request);
                    //FIXME: handle response here...
                    LOGGER.debug("Response : {}", rawResponse.toString());
                    
                    HttpEntity responseEntity = rawResponse.getEntity();
                    if(responseEntity!=null) {
                        LOGGER.debug("Response Entity : {}", EntityUtils.toString(responseEntity));
                    }
                    

                } finally {
                    httpClient.close();
                }
            } catch (IOException ioe) {
                LOGGER.error("SMS Not Send", ioe);
            }
        } else if ("GET".equals(ConfigResolver.getPropertyValue("mobildev.apiMode"))) {
            try {
                String URL = ConfigResolver.getPropertyValue("mobildev.api");
                Executor executor = Executor.newInstance()
                        .auth("telve", "telve");

                URL = ConfigResolver.getPropertyValue("mobildev.api");
                Request req = Request.Get(URL + "?" + buildSmsGetBody(to, body))
                        .connectTimeout(1000000)
                        .socketTimeout(1000000);

                LOGGER.debug(req.toString());
                LOGGER.debug("Response : {}", req.toString());

                HttpResponse response = executor.execute(req)
                        .returnResponse();
                String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
                LOGGER.debug(responseString);
                LOGGER.debug("Response : {}", responseString);
                if (!responseString.contains("200")) {
                    LOGGER.error("Send SMS");
                } else {
                    LOGGER.error("Cannot send message");
                }

            } catch (IOException ex) {
                LOGGER.error("Cannot access server", ex);
            }

        }
    }

    private String buildSmsBody(String to, String body) {
        StringBuilder sb = new StringBuilder();
        sb.append("{ 'mtId': ").append(ConfigResolver.getPropertyValue("mobildev.mtId")).append(",")
                .append("'originator':'").append(ConfigResolver.getPropertyValue("mobildev.originator")).append("',")
                .append("'messageBody':'").append(body).append("',")
                .append("'msisdns':['").append(to).append("'],")
                .append("'sendTime':'").append("2016-04-14 12:36:00").append("',")
                .append("'tags':[").append(ConfigResolver.getPropertyValue("mobildev.tags")).append("],")
                .append("'smsPerPacket':").append(ConfigResolver.getPropertyValue("mobildev.smsPerPacket")).append(",")
                .append("'packetPerMin':").append(ConfigResolver.getPropertyValue("mobildev.packetPerMin")).append(",")
                .append("'blacklist':").append(ConfigResolver.getPropertyValue("mobildev.blacklist")).append(",")
                .append("'sp':").append(ConfigResolver.getPropertyValue("mobildev.sp")).append(",")
                .append("'credit':").append(ConfigResolver.getPropertyValue("mobildev.credit"))
                .append("}");

        return sb.toString();

    }
    
    private String buildXMLSmsBody(String to, String body) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
            .append("<MainmsgBody>")
            .append("<UserName>").append(ConfigResolver.getPropertyValue("mobildev.userName")).append("</UserName>")
            .append("<PassWord>").append(ConfigResolver.getPropertyValue("mobildev.userPassword")).append("</PassWord>")
            .append("<Action>0</Action>")
            .append("<Mesgbody>").append(body).append("</Mesgbody>")
            .append("<Numbers>").append(to).append("</Numbers>")
            //.append("<Originator>").append("İOVA YAZILIM").append("</Originator>")                
            .append("</MainmsgBody>");    
        LOGGER.debug("XML String : {}", sb.toString()); 
        return sb.toString();

    }
    

    private String buildSmsGetBody(String to, String body) {
        StringBuilder sb = new StringBuilder();
        String bodyURLString = body;
        try {
            LOGGER.debug("SMS Body : " + body);
            body = StringUtils.escapeTurkish(body);
            LOGGER.debug("SMS Body Escape Turkish: " + body);
            bodyURLString = URLEncoder.encode(body, StandardCharsets.UTF_8.toString());
            LOGGER.debug("SMS Body Encoded : " + bodyURLString);
            //+ "StandardCharsets.ISO_8859_9.toString()
        } catch (UnsupportedEncodingException ex) {
            LOGGER.error("SMS URL Encode error", ex);
        }
        sb.append("username=").append(ConfigResolver.getPropertyValue("mobildev.userName")).append("&")
                .append("password=").append(ConfigResolver.getPropertyValue("mobildev.userPassword")).append("&")
                .append("company=").append(ConfigResolver.getPropertyValue("mobildev.company")).append("&")
                .append("action=0").append("&")
                //.append("Originator=IOVA").append("&")
                .append("message=").append(bodyURLString).append("&")
                .append("numbers=").append(to);

        return sb.toString();
    }
}
