package com.ozguryazilim.telve.sms.mobildev;

import com.ozguryazilim.telve.channel.sms.SmsService;
import java.io.IOException;
import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SMS Channel Mobildev servisleri için implementasyon
 * 
 * @author Hakan Uygun
 */
public class MobildevSmsService implements SmsService{

    private static final Logger LOGGER = LoggerFactory.getLogger(MobildevSmsService.class);
    
    @Override
    public void send(String to, String body) {
        try {

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            try {
                HttpPost request = new HttpPost(ConfigResolver.getPropertyValue("mobildev.api") + "/sms?access_token=" + ConfigResolver.getPropertyValue("mobildev.token"));
                StringEntity params = new StringEntity(
                        buildSmsBody( to, body ),
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
    }
    
    
    private String buildSmsBody( String to, String body ){
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
}
