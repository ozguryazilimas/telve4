/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.calendar;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.deltaspike.core.api.provider.BeanProvider;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author oyas
 */
@Path("/calendar")
public class CalendarEventSourceService {

    private static final Logger LOG = LoggerFactory.getLogger(CalendarEventSourceService.class);

    @GET
    @Path("/events/{id}.fc")
    public Response getFCEvents(
            @PathParam("id") String id,
            @QueryParam("start") String startStr,
            @QueryParam("end") String endStr) {

        List<CalendarEventModel> ls = getEvents( id, startStr, endStr );

        
        
        List<String> rs = new ArrayList<>();
        
        for( CalendarEventModel se : ls ){
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append("\"id\":\"").append(se.getId()).append("\",");
            sb.append("\"title\":\"").append(se.getTitle()).append("\",");
            sb.append("\"description\":\"").append(se.getDescription()).append("\",");
            
            if( se.getAllDay() ){
               sb.append("\"start\":\"").append(getFormatedDate(se.getStart())).append("\",");
            } else {
               sb.append("\"start\":\"").append(getFormatedDateTime(se.getStart())).append("\",");
               sb.append("\"end\":\"").append(getFormatedDateTime(se.getEnd())).append("\",");
            }
            
            if( se.getEditable() ){
                sb.append("\"editable\":\"true\"");
            } else {
                sb.append("\"editable\":\"false\"");
            }
            sb.append("}");
            rs.add(sb.toString());
        }
        

        String result = "[" + Joiner.on(",").join(rs) + "]";
        
        //İçeriği yollayalım.
        return Response.ok(result, MediaType.APPLICATION_JSON).build();
        
    }

    @GET
    @Path("/events/{id}.jCal")
    public Response getjCalEvents(
            @PathParam("id") String id,
            @QueryParam("start") String startStr,
            @QueryParam("end") String endStr) {

        LOG.debug("EventSource : {}, Type: {}", id, "jCal");
        LOG.debug("Request Filter: {} {}", startStr, endStr);

        //CalendarEventSource cec = (CalendarEventSource) BeanProvider.getContextualReference(id);
        //cec.loadEvents(this, start, end);
        //İçeriği yollayalım.
        return Response.ok("jCal formated", "application/json").build();

    }

    protected List<CalendarEventModel> getEvents(String id, String startStr, String endStr) {
        LOG.debug("EventSource : {}, Type: {}", id, "FC");
        LOG.debug("Request Filter: {} {}", startStr, endStr);

        if( !hasPermission( id) ){
            LOG.debug("User has not permission for : {}", id );
            return Collections.emptyList();
        }
        
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        Date sdt = df.parseDateTime(startStr).toDate();
        Date edt = df.parseDateTime(endStr).toDate();
        
        //FIXME: burada yetki kontrolü gerek
        
        

        try {
            CalendarEventSource cec = (CalendarEventSource) BeanProvider.getContextualReference(id);
            if (cec != null) {
                return cec.getEvents(sdt, edt);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            LOG.warn("Error getting events", e);
            return Collections.emptyList();
        }

    }
    
    protected boolean hasPermission( String id ){
        //Böyle bir eventSource var mı?
        com.ozguryazilim.telve.calendar.annotations.CalendarEventSource esm = CalendarEventSourceRegistery.getMetadata(id);
        if( esm == null ) return false;
        
        //Puplic mi?
        if( Strings.isNullOrEmpty(esm.permission())) return true;
        
        Subject currentUser = SecurityUtils.getSubject();
        
        //Login olması zorunlu mu?
        if( "NEED_LOGIN".equals(esm.permission()) && currentUser == null ) return false;
        
        //Yetki var mı?
        return currentUser.isPermitted( esm.permission() + ":select"  );
    }
    
    protected String getFormatedDateTime( Date date ){
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd'T'hh:mm:ss");
        return df.print(new DateTime(date));
    }
    
    protected String getFormatedDate( Date date ){
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        return df.print(new DateTime(date));
    }
}
