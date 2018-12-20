package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.messagebus.command.CommandScheduler;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import com.ozguryazilim.telve.messagebus.command.ui.ScheduledCommandUIModel;
import com.ozguryazilim.telve.utils.ScheduleModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.ejb.Timer;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.shiro.subject.Subject;
import org.ocpsoft.prettytime.PrettyTime;

/**
 * Zamanlanmış raporların kontrol ve sunumu için Control sınıfı.
 *
 * @author Hakan Uygun
 */
@Named
@WindowScoped
public class ScheduledReportsControl implements Serializable {

    @Inject
    private CommandScheduler scheduler;

    @Inject
    private Subject identity;
    
    @Inject
    private Identity userIdentity;

    private List<ScheduledCommandUIModel> commands = new ArrayList<>();
   
    @PostConstruct
    public void init() {
        populateScheduledReports();
    }

    private void populateScheduledReports() {

        String user = userIdentity.getUserInfo().getId();
        //FIXME: Kullanıcının tercih etiği locale alınmalı
        PrettyTime prettyTime = new PrettyTime(new Locale("tr"));
        
        for (Timer t : scheduler.getTimers()) {
            ScheduledCommand c = (ScheduledCommand) t.getInfo();

            //Sadece rapor komutları ile ilgileniyoruz.
            if (c.getCommand() instanceof ReportCommand) {
                //Kullanıcı bizim kullanıcımız mı?
                //TODO: Belki burada ayrıca bir role yapabiliriz. Sadece zamanlanmış rapor yönetimi yetkisi olan bir kullanıcı, tüm kullanıcıların raporlarını mıncırabilir?
                if (user.equals(c.getCreateBy()) || userIdentity.isPermitted("viewAll:select:*")) {
                    
                    ScheduledCommandUIModel m = new ScheduledCommandUIModel();

                    m.setScheduledCommand((ScheduledCommand) t.getInfo());
                    m.setId(m.getScheduledCommand().getId());
                    m.setNextTimeout(t.getNextTimeout());
                    m.setTimeRemaining(t.getTimeRemaining());
                    //İnsan için kalan zaman :)
                    m.setTimeRemainingStr(prettyTime.format(new Date(System.currentTimeMillis() + m.getTimeRemaining())));
                    m.setScheduleStr(ScheduleModel.convertForHuman(m.getScheduledCommand().getSchedule()));
                    try {
                        m.setSchedule(t.getSchedule());
                    } catch (IllegalStateException e) {
                        //Farklı timer tiplerinde schedule alınamadığı için bu exception gelebiliyor. Passgeçiyoruz.
                    }

                    commands.add(m);
                }
            }
        }
    }

    public void refresh() {
        commands.clear();
        populateScheduledReports();
    }
    
    public void delete( String id ){
        scheduler.removeFromScedularBuID(id);
        refresh();
    }

    public List<ScheduledCommandUIModel> getCommands() {
        return commands;
    }

    public void setCommands(List<ScheduledCommandUIModel> commands) {
        this.commands = commands;
    }

}
