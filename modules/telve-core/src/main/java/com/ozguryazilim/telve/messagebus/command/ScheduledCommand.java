package com.ozguryazilim.telve.messagebus.command;

import java.util.Date;
import java.util.Objects;

/**
 * Zamanlanmış komut komutu.
 * 
 * Her hangi bir komutu zamanlanmış bir komut haline dönüştürmek için kullanılır.
 * 
 * @author Hakan Uygun
 */
public class ScheduledCommand extends AbstractCommand{
    
    private String id;
    private String schedule;
    private Date createDate;
    private String createBy;
    private Command command;

    /**
     * Yeni bir zamanlanmış komut oluşturur.
     * @param schedule 
     * @param createBy
     * @param command 
     */
    /*
    public ScheduledCommand(String schedule, String createBy, Command command) {
        //FIXME: Burada UUID oluşturmalı.
        this.id = "1234";
        this.schedule = schedule;
        this.createBy = createBy;
        this.command = command;
        this.createDate = new Date();
    }*/
    
    public ScheduledCommand(String id, String schedule, String createBy, Command command) {
        this.id = id;
        this.schedule = schedule;
        this.createBy = createBy;
        this.command = command;
        this.createDate = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScheduledCommand other = (ScheduledCommand) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}
