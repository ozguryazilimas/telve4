package com.ozguryazilim.telve.notify;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Notify mesajlarını kahve üzerinde saklar.
 *
 * @author Hakan Uygun
 */
@Named
@ApplicationScoped
public class NotifyStore implements Serializable {

    private static final Logger LOG = LoggerFactory.getLogger(NotifyStore.class);
    
    @Inject
    @Default
    Kahve kahve;

    /**
     * Verilen NotifyMessage'ı kahve üzerinde saklar
     *
     * @param message
     */
    public void save(NotifyMessage message) {

        //Herkes'e gidenleri saklayamıyoruz :(
        if ("*".equals(message.getTo())) {
            return;
        }

        String key = "notify.count." + message.getTo();

        KahveEntry e = kahve.get(key, 0);
        Integer ix = e.getAsInteger();

        if (message.getTimestamp() == null) {
            message.setTimestamp(System.currentTimeMillis());
        }

        Gson gson = new Gson();
        String data = gson.toJson(message);

        //notify.msg.telve.0 gibi bir key
        kahve.put("notify.msg." + message.getTo() + "." + ix, data);
        kahve.put(key, ix + 1);
    }

    /**
     * Geriye verilen identity için notify mesaj listesini döndürür.
     *
     * @param identity
     * @return
     */
    public List<NotifyMessage> getNotifies(String identity) {
        List<NotifyMessage> ls = new ArrayList<>();

        String key = "notify.count." + identity;
        KahveEntry e = kahve.get(key, 0);

        Gson gson = new Gson();
        for (int i = 0; i < e.getAsInteger(); i++) {
            KahveEntry ee = kahve.get("notify.msg." + identity + "." + i, "");
            NotifyMessage m = gson.fromJson(ee.getAsString(), NotifyMessage.class);
            //JSON'a çevrilirken "=" escape'leniyor.
            if ( m != null ) {
                m.setLink(m.getLink().replaceAll("u003d", "="));
                ls.add(m);
            }
        }

        return ls;
    }

    /**
     * Verilen identity için mesaj sayısını döndürür.
     *
     * @param identity
     * @return
     */
    public Integer getNotifyCount(String identity) {
        String key = "notify.count." + identity;
        KahveEntry e = kahve.get(key, 0);
        return e.getAsInteger();
    }

    /**
     * Verilen identiye ait notify mesajlarını temizler.
     *
     * @param identity
     */
    public void clear(String identity) {
        String key = "notify.count." + identity;
        KahveEntry e = kahve.get(key, 0);

        Gson gson = new Gson();
        for (int i = 0; i < e.getAsInteger(); i++) {
            kahve.remove("notify.msg." + identity + "." + i);
        }

        kahve.remove(key);
    }

    /**
     * Kullanıcıya ait ilgili notify ları siler ve notify listelerini günceller.
     * @param identity
     * @param notifies
     */
    public void clearNotifiesByNotifies(String identity, List<NotifyMessage> notifies) {
        List<NotifyMessage> userNotifies = getNotifies(identity);
        userNotifies.removeAll(notifies);
        clear(identity);
        save(identity, userNotifies);
    }
    
    /**
     * Tıklanılan mesajı önce listeden siler sonra eğer üzerinde link varsa ona gider.
     * @param identity
     * @param msgId 
     */
    public void go( String identity, String msgId ){
        List<NotifyMessage> ls = getNotifies(identity);
        NotifyMessage rs = null;
                
        for( NotifyMessage nm : ls ){
            if( nm.getId().equals(msgId)){
                rs = nm;
                break;
            }
        }

        if( rs == null ) return;
        
        //Önce seçileni aradan bir çıkartalım.
        ls.remove(rs);
        
        //Listenin hepsini yeniden yazalım.
        clear(identity);
        save(identity, ls);
        
        //Şimdi de linki varsa gidelim.
        if( !Strings.isNullOrEmpty(rs.getLink())){
            FacesContext fc = FacesContext.getCurrentInstance();
            try {
                String root = fc.getExternalContext().getApplicationContextPath();
                fc.getExternalContext().redirect( root + rs.getLink());
            } catch (IOException ex) {
                LOG.error("Notify Redirect Error",ex);
            }
        }
    }
    
    public void save(String identity, List<NotifyMessage> messages) {
        int ix = 0;
        for( NotifyMessage message : messages ){
            Gson gson = new Gson();
            String data = gson.toJson(message);
        
            //notify.msg.telve.0 gibi bir key
            kahve.put("notify.msg." + message.getTo() + "." + ix, data);
            ix++;
        }
        String key = "notify.count." + identity;
        kahve.put(key, ix);
    }
}
