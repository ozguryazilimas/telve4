/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class AuditLogClearCommand extends AbstractStorableCommand{
    
    /**
     * Ne kadar önceki değere bakacak?
     */
    private String interval;

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
    
    
}
