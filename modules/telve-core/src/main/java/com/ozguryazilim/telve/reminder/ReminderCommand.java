package com.ozguryazilim.telve.reminder;

import com.ozguryazilim.telve.messagebus.command.AbstractCommand;
import java.util.HashMap;
import java.util.Map;

/**
 * Reminder işlemleri için komut.
 * 
 * Bu komut ScheduledCommand biçmine dönüştürülerek sisteme eklenir. Zamanı geldiğinde çalıştırılır.
 * 
 * Bu komutu kullanmak için @see RimenderService sınıfı kullanılır.
 * 
 * @author Hakan Uygun
 */
public class ReminderCommand extends AbstractCommand{
    
    /**
     * Hatırlatma kuralları için kullanılacak hatırlatma sınıfı.
     * 
     * Varsayılan hali generic hatırlatma. Eğer hiç bir kural bulunamazsa da bu kural seti geçerli olur.
     */
    private String reminderClass = "GENERIC";
    
    /**
     * Ne zaman gönderileceği.
     * 
     * FIXME: Format için açıklama yapılmalı.
     */
    private String schedule;    
    
    /**
     * Hatırlatma konusu
     */
    private String subject;
    
    /**
     * Hatırlatmanın hedef kitlesi.
     * 
     * Kişi olabileceği gibi grup, role v.b. olabilir.
     * 
     * FIXME: Format için açıklama gerek
     * 
     */
    private String target;
    
    /**
     * Hatırlatma içeriği.
     * 
     * Farklı hatırlatma sistemleri farklı formatlar kullanabilir. Bunun için templat üreticilerin kullanacağı parametreler bu veri içerisine doldurulur.
     * 
     * Şablon sistemi ile buraya konacak değerlerin bir biriyle örtüşmesinden yazılımcı sorumludur.
     * 
     * Ex: body=Merhaba Dünya,template=/templates/merhaba.tpl
     * 
     */
    private Map<String,Object> params = new HashMap<>();

    public String getReminderClass() {
        return reminderClass;
    }

    public void setReminderClass(String reminderClass) {
        this.reminderClass = reminderClass;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

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
    
    
    
}
