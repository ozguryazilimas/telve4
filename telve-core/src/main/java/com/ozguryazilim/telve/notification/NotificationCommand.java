/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notification;

import com.ozguryazilim.telve.messagebus.command.AbstractCommand;
import java.util.HashMap;
import java.util.Map;
import javax.activation.DataHandler;

/**
 * Notification gönderimi için komut.
 * 
 * Bir notification mesajı üretildiğinde contact listesi, template, channel ve tabii ki üretilecek olan mesajın body'sini içermeli.
 * 
 * Notification mesajları örneğin şunlar olabilir : 
 * 
 * Reminder, Alert, Event v.b.
 * 
 * Template'ler kanallara göre değişklik gösterebilirler. Ayrıca mesaj içerisinde template'in kullanması için gerekli bütün bilginin taşınıyor olması gerekir.
 * 
 * 
 * @author Hakan Uygun
 */
public class NotificationCommand extends AbstractCommand{
 
    /**
     * Notification sınıfı.
     * 
     * Bu sınıf bilgisine göre configuration üzerinden hangi channelların kullanılacağı bilgisi kontrol edilir.
     * 
     */
    private String notificationClass;
    
    /**
     * Gönderi konusu.
     * 
     * Özellikle ileri tarihli bir gönderi için görünmesi gerekiyor.
     */
    private String subject;
    
    /**
     * Gönderiyi kimin yaptığı.
     * 
     * Sistem gönderileri için SYSTEM kullanılır. Geri kalanda Kullanıcı adı olmalı.
     */
    private String sender;
    
    /**
     * Gönderinin hedef kitlesi.
     * 
     * Kişi olabileceği gibi grup, role v.b. olabilir.
     * 
     * FIXME: Format için açıklama gerek
     * 
     */
    private String target;
    
    /**
     * Gönderi şablonu değiştirmek için ara anahtar.
     * 
     * ConfigResolver yeteneği ile 
     * Aynı şekilde ProjectStage'de kullanılabilir.
     * channelTemplate.{channel}.{notificationClass}.{template} şeklinde aranır.
     * 
     */
    private String template = "GENERIC";
    
    /**
     * Gönderi içeriği.
     * 
     * Farklı hatırlatma sistemleri farklı formatlar kullanabilir. Bunun için template üreticilerin kullanacağı parametreler bu veri içerisine doldurulur.
     * 
     * Şablon sistemi ile buraya konacak değerlerin bir biriyle örtüşmesinden yazılımcı sorumludur.
     * 
     * Ex: body=Merhaba Dünya,template=/templates/merhaba.tpl
     * 
     */
    private Map<String,Object> params = new HashMap<>();
    
    /**
     * Mesajlara ek attachment listesi.
     * 
     * Özellikle e-posta içerisine eklenecek olan dosyalar v.s. için kullanılır.
     * 
     * İlgilenmeyen channellar bu bilgiyi pass geçer.
     */
    private Map<String,DataHandler> attachments = new HashMap<>();

    public String getNotificationClass() {
        return notificationClass;
    }

    public void setNotificationClass(String notificationClass) {
        this.notificationClass = notificationClass;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Aşağıdaki formatta bir hedef sorgusu gönderilir.
     * 
     * cs=contactSourceName;key=value;key=value||cs=contactSourceName;key=value;key=value
     * 
     * Örnek: cs=contact;id=bişi;firstname=Hakan;lastname=Uygun;email=ccc@example.com;mobile=+9052222222||.... 
     * @return 
     */
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public Map<String, DataHandler> getAttachments() {
        return attachments;
    }

    public void setAttachments(Map<String, DataHandler> attachments) {
        this.attachments = attachments;
    }
    
    
    
}
