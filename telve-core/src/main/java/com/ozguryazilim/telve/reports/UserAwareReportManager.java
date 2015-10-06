/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.reports;

import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.picketlink.Identity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kullananıcı yetkileri çerçevesinde raporlar için Manager
 * @author Hakan Uygun
*/
@Named
@SessionScoped
public class UserAwareReportManager extends ReportManager implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(UserAwareReportManager.class);
    
    @Inject @UserAware
    private Kahve kahve;
    
    @Inject
    private Identity identity;
    
    /**
     * Favori raporlar için rapor rating bilgisi.
     */
    private Map<String, Integer> reportRatings = new HashMap<>();
    
    @PostConstruct
    public void init(){
        initReports();
        initFavReports();
        //Todo : aşağıdai tür arayüzlere de ihtiyaç olacak.
        //ÜretlmişHazırRaporlar
        //ZamanlanmışRaporlar
        
        //Bu değerler özel flderlar olarak ağaca gelebileceği gibi Context menu üzerinden seçilebilen ve aacı komple değiştirebilen yapılar da olabilir.
        //Hatta her biri kendi arayüz ve kontrolüne sahip sayfalar oalrak implemente edilebilirler.
        //Bunun için cpntext menunun biraz daha becerikli hale gelmesi gerekiyor.
        
        
        
    }
    
    public void initReports(){
        for (Map.Entry<String, Report> e : ReportRegistery.getReports().entrySet()) {
            //Kullanıcının yetkisi var mı? Eğer permission tanımlanmamışsa Sınıf ismini kullan.
            String p = e.getValue().permission();
            if (p.isEmpty()) {
                p = e.getKey();
            }

            if (identity.hasPermission(p, "select")) {
                //Eğer kullanıcının yetkisi varsa sisteme ekliyoruz.
                addReport(e.getValue().path(), e.getKey());
            }
        }
    }
    
    /**
     * Option sistemi üzerinden kullanıcının favori raporlarının listesini
     * oluşturur.
     */
    protected void initFavReports() {
    
        ReportFolder favReports = findOrCreateFolder("/favorites", "favorite");
        
        //FIXME: Bir güvelik kontrolü de buraya gerekiyor. Bir rapotu favladığınız da bir daha kimse elinizden yetkinizi alamıyor.
        
        KahveEntry o = kahve.get("reports.fav.count");
        if (o == null) {
            //Kullanıcın favori raporu yok.
            return;
        }

        Integer fvc = o.getAsInteger();

        for (int i = 0; i < fvc; i++) {
            //İsmi kondu
            o = kahve.get("reports.fav." + i + ".name");
            String r = o.getAsString();
            favReports.getReports().add(r);
            //Rate'i de kondu.
            o = kahve.get("reports.fav." + i + ".rate");
            reportRatings.put(r, o.getAsInteger());
        }
    }

    /**
     * Favori rapor listesini Option sistemine kaydeder.
     */
    public void saveFavReports() {

        ReportFolder favReports = findOrCreateFolder("/favorites", "favorite");
        
        int i = 0;
        for (String r : favReports.getReports()) {
            kahve.put("reports.fav." + i + ".name", r);
            kahve.put("reports.fav." + i + ".rate", reportRatings.get(r));
            i++;
        }

        kahve.put( "reports.fav.count", favReports.getReports().size());
    }
    
}
