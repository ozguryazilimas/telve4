package com.ozguryazilim.telve.audit.housekeep;

import com.ozguryazilim.telve.messagebus.command.AbstractStorableCommand;

/**
 *
 * Belirlenen tarihten eski güvenlik loglarını temizlemek için komut.
 *
 *
 *
 * @author Ceyhun Onur
 */
public class AuditLogClearCommand extends AbstractStorableCommand {

    /**
     * interval = Ne kadar önceki değere bakacak?
     */
    private String interval;
    private String domain;
    private String category;
    private String action;

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
