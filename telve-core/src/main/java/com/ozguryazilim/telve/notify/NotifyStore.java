/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.notify;

import com.google.gson.Gson;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Notify mesajlarını kahve üzerinde saklar.
 *
 * @author Hakan Uygun
 */
@Named
@ApplicationScoped
public class NotifyStore implements Serializable {

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
}
