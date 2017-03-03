/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 *
 * @author oyas
 */
public class CalendarEventModel {

    private String id;
    private String title;
    private String description;
    private Boolean allDay;
    private Boolean editable;
    private Date start;
    private Date end;
    private String source;
    
    public CalendarEventModel() {
        this.id = UUID.randomUUID().toString();
        this.editable = Boolean.FALSE;
        this.allDay = Boolean.FALSE;
    }

    public CalendarEventModel(String title, Date start) {
        this.id = UUID.randomUUID().toString();
        this.editable = Boolean.FALSE;
        this.allDay = Boolean.FALSE;
        this.title = title;
        this.start = start;
    }

        
    
    
    public CalendarEventModel(String id, String title, String description, Date start, Date end) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.allDay = Boolean.FALSE;
        this.editable = Boolean.FALSE;
    }

    
    
    public CalendarEventModel(String id, String title, String description, Boolean allDay, Date start) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.allDay = allDay;
        this.start = start;
        this.editable = Boolean.FALSE;
    }

    public CalendarEventModel(String id, String title, String description, Date start) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.start = start;
        this.allDay = Boolean.TRUE;
        this.editable = Boolean.FALSE;
    }

    
    
    public CalendarEventModel(String title, String description, Date start, Date end) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.start = start;
        this.end = end;
        this.allDay = Boolean.FALSE;
        this.editable = Boolean.FALSE;
    }
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.title);
        hash = 41 * hash + Objects.hashCode(this.description);
        hash = 41 * hash + Objects.hashCode(this.allDay);
        hash = 41 * hash + Objects.hashCode(this.editable);
        hash = 41 * hash + Objects.hashCode(this.start);
        hash = 41 * hash + Objects.hashCode(this.end);
        hash = 41 * hash + Objects.hashCode(this.source);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CalendarEventModel other = (CalendarEventModel) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.allDay, other.allDay)) {
            return false;
        }
        if (!Objects.equals(this.editable, other.editable)) {
            return false;
        }
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        if (!Objects.equals(this.end, other.end)) {
            return false;
        }
        return true;
    }
    
    

        
    
    
}
