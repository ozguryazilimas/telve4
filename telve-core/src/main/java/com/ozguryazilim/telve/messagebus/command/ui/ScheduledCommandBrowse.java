/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.messagebus.command.ui;

import com.google.common.base.Strings;
import com.ozguryazilim.telve.audit.AuditLogger;
import com.ozguryazilim.telve.auth.Identity;
import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.forms.RefreshBrowserEvent;
import com.ozguryazilim.telve.messagebus.command.CommandScheduler;
import com.ozguryazilim.telve.messagebus.command.CommandSender;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommand;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.utils.ScheduleModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.NoMoreTimeoutsException;
import javax.ejb.Timer;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.deltaspike.core.api.scope.WindowScoped;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.ocpsoft.prettytime.PrettyTime;
import org.primefaces.event.SelectEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zamanlanmış Görevler için tarama UI control sınıfı.
 * 
 * Standart browse sınıfı değildir.
 * 
 * @author Hakan Uygun
 */
@WindowScoped
@Named
public class ScheduledCommandBrowse implements Serializable{
    
    private static final Logger LOG = LoggerFactory.getLogger(ScheduledCommandBrowse.class); 
    
    @Inject
    private CommandScheduler scheduler;
    
    @Inject
    private StoredCommandRepository repository;
    
    @Inject
    private CommandSender commandSender;
    
    @Inject
    private AuditLogger auditLogger;
    
    @Inject
    private Identity identity;

    private StoredCommand entity;
    private CommandEditorBase currentEditor;
    
    private ScheduledCommandUIModel selectedItem;
    
    private String filter;
    
    private Map<String,ScheduledCommandUIModel> items;
    private List<ScheduledCommandUIModel> filteredItems;
    
    private String scheduleType;
    private Date startDate;
    private Date endDate;
    private String schedule;
    
    
    /**
     * Scheduler ve Repository'den verileri toplar.
     * @throws ClassNotFoundException 
     */
    protected void populateItems() throws ClassNotFoundException{
        items.clear();
        
        //FIXME: Kullanıcının tercih etiği locale alınmalı
        PrettyTime prettyTime = new PrettyTime(new Locale("tr"));
        
        //Önce timer'ları bir toplayalım.
        for( Timer t : scheduler.getTimers()){
            
            ScheduledCommandUIModel m = new ScheduledCommandUIModel();
            
            m.setScheduledCommand((ScheduledCommand)t.getInfo());
            m.setId( m.getScheduledCommand().getId());
            try{
            m.setNextTimeout(t.getNextTimeout());
            m.setTimeRemaining(t.getTimeRemaining());
            //İnsan için kalan zaman :)
            m.setTimeRemainingStr( prettyTime.format(new Date(System.currentTimeMillis() + m.getTimeRemaining())));
            } catch ( NoMoreTimeoutsException e ){
                //FIXME: Eğer geriye tektiklemelik bir şey kalmamış ise geliyor. Ne yapalım?
                // Aslında başlangıç ve bitiş tarihleri arasında yapılacak herşey yapılmış. Timer silinebilir. Hatta komutun kendisi silininebilir.
                LOG.warn("No more time out for : {}", m.getScheduledCommand().getCommand().getName());
            }
            m.setScheduleStr(ScheduleModel.convertForHuman(m.getScheduledCommand().getSchedule()));
            try{
                m.setSchedule(t.getSchedule());
            } catch (IllegalStateException e){
                //Farklı timer tiplerinde schedule alınamadığı için bu exception gelebiliyor. Passgeçiyoruz.
            }
            
            items.put( m.getId(), m );
        }
        
        //Şimdide repository'deki storedCommandları toparlayalım.
        List<StoredCommand> cmds = repository.findAll();
        for( StoredCommand sc : cmds ){
            String id = "sc-" + sc.getId();
            ScheduledCommandUIModel m = items.get(id);
            if( m == null ){
                m = new ScheduledCommandUIModel();
                m.setId(id);
                items.put(id, m);
            }
            
            m.setStoredCommand(sc);
            m.setStorableCommand(repository.convert(sc));
             
        }
    }
    
    /**
     * Ad ya da açıklama alınında filter değerine göre süzme yapar.
     */
    protected void populateFilteredItems() throws ClassNotFoundException{
        
        if( items == null ){
            items = new HashMap<>();
        }
        
        populateItems();
        
        filteredItems.clear();
        
        for( ScheduledCommandUIModel sc : items.values() ){
            if( Strings.isNullOrEmpty(filter) || sc.getName().contains(filter) || sc.getInfo().contains(filter) ){
                filteredItems.add(sc);
            }
        }
        
    }

    /**
     * Filtrelenmiş listeyi döndürür.
     * @return
     * @throws ClassNotFoundException 
     */
    public List<ScheduledCommandUIModel> getFilteredItems() throws ClassNotFoundException {
        if( filteredItems == null ){
            filteredItems = new ArrayList<>();
            populateFilteredItems();
        }
        
        return filteredItems;
    }
    
    /**
     * Filtered items'ın yeniden toplanmasını sağlar.
     * @throws ClassNotFoundException 
     */
    public void search() throws ClassNotFoundException{
        populateFilteredItems();
    }
    
    
    public List<String> getEditorNames(){
        return CommandEditorRegistery.getEditorNames();
    }

    /**
     * Adı verilen editor ile yeni bir kayıt oluşturur.
     * @param name 
     */
    public void createNew( String name ){
     
        CommandEditorBase ce = CommandEditorRegistery.getEditorByName(name);
        if( ce == null ){
            LOG.error("CommandEditor not found : {}", name);
            FacesMessages.error( "general.message.editor.NotFound");
            return;
        }
        ce.createNew();
        currentEditor = ce;
    }
    
    /**
     * Geriye editör/komut için tanıtılmış icon varsa onu döndürür.
     * 
     * Bulamazsa standart icon döndürür.
     * 
     * @return 
     */
    public String getIconPath( String name ){
        //FIXME: Doğru düzgün registery'den alınmalı.
        return "/img/ribbon/large/report.png";
    }

    /**
     * Aktif olan editor'ü döndürür.
     * @return 
     */
    public CommandEditorBase getCurrentEditor() {
        return currentEditor;
    }
    
    public void edit(){
        //Eğer stored command değilse çık
        if( selectedItem.getStoredCommand() == null ) return;
        
        entity = selectedItem.getStoredCommand();
        CommandEditorBase ce = CommandEditorRegistery.getEditorByCommand(entity.getType());
        if( ce == null ){
            LOG.error("CommandEditor not found for : {}", entity.getType());
            FacesMessages.error("general.message.editor.NotFound");
            return;
        }
        currentEditor = ce;
        try {
            ce.edit(entity);
        } catch (ClassNotFoundException ex) {
            //FIXME: Burası için aslında generik hata sayfasına gidilebilir...
            LOG.error("Command Class not found: {}", entity.getType(), ex);
            FacesMessages.error("general.message.editor.NotFound");
        }
    }
    
    @Transactional
    public void delete() throws ClassNotFoundException{
        //Önce timer'a bir bakalım. Varsa onu bir silelim...
        if( selectedItem.getScheduledCommand() != null ){
            scheduler.removeFromScedular(selectedItem.getScheduledCommand());
        }
        entity = selectedItem.getStoredCommand();
        repository.remove(entity);
        search();
    }

    /**
     * RefreshBrowseEvent'i dinlenir ve ilgili domain ise search komutu çalıştırılır.
     * @param event 
     */
    public void refreshListener( @Observes RefreshBrowserEvent event ) throws ClassNotFoundException{
        if( event.getDomain().equals( "StoredCommand" ) ){
            search();
        }
    }
    
    /**
     * UI tarafından schedule popup öncesi çağrılır.
     */
    public void startSchedule(){
        startDate = null;
        endDate = null;
        scheduleType = "O";
        schedule = "";
    }
    
    /**
     * Seçili olan item'ı timer'dan kaldırır.
     */
    public void stopSchedule() throws ClassNotFoundException{
        //Eğer seçili item yoksa çıkalım.
        if( selectedItem == null ) return;
        //Eğer komut hali hazırda zamanlanmış durumda ise önce onu siler.
        if( selectedItem.getScheduledCommand() != null ){
            scheduler.removeFromScedular(selectedItem.getScheduledCommand());
        }
        search();
    }
    
    /**
     * Seçili olan komutu zamanlı çalışmak üzere gerekenleri yapar.
     */
    public void schedule() throws ClassNotFoundException{
        //Eğer seçili item yoksa çıkalım.
        if( selectedItem == null ) return;
        
        
        String s = buildScheduleExpression();
        //Eğer tanımlı bir schedule yoksa çık
        if( Strings.isNullOrEmpty(s)) return;
        
        //Eğer komut hali hazırda zamanlanmış durumda ise önce onu siler.
        if( selectedItem.getScheduledCommand() != null ){
            scheduler.removeFromScedular(selectedItem.getScheduledCommand());
        }
        
        //Şimdi komutu scheduler'a ekliyoruz.
        //TODO: Kullanıcı bilgisini alıp eklemek lazım.
        ScheduledCommand sc = new ScheduledCommand("sc-" + selectedItem.getStoredCommand().getId(), s, "SYSTEM", selectedItem.getStorableCommand());
        scheduler.addToSceduler(sc);
        
        search();
        
    }
    
    /**
     * UI'dan gelen verileri kullanarak SC için schdule oluşturur.
     * @return 
     */
    protected String buildScheduleExpression(){
        
        switch ( scheduleType ){
            case "O" : return ScheduleModel.getOnceExpression(startDate);
            case "P" : return ScheduleModel.getPeriodExpression(schedule, startDate);
            case "S" : return ScheduleModel.getShortScheduleExpression(schedule, startDate, endDate);
            case "SE" : return ScheduleModel.getScheduledExpression(schedule, startDate, endDate);
        }
        
        return "";
    }
    
    public void run(){
        
        auditLogger.actionLog("ScheduledCommand", 0l, selectedItem.getCommand().getName(), "ScheduledCommand", "EXEC", identity.getLoginName(), "");
        
        commandSender.sendCommand(selectedItem.getCommand());
    }

    public ScheduledCommandUIModel getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ScheduledCommandUIModel selectedItem) {
        this.selectedItem = selectedItem;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
    
                
    public void onRowSelect(SelectEvent event) {
        selectedItem = (ScheduledCommandUIModel) event.getObject();
    }

    /**
     * Komut için zamanlama betiğini döndürür.
     * 
     * @return 
     */
    public String getSchedule() {
        return schedule;
    }

    /**
     * Bir komutun zamanlaması için gereken zamanlama betiğini setler.
     * 
     * UI tarafından kullanıcının seçtiği zamanlama'yı setlemek için kullanılır.
     * @param schedule 
     */
    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(String scheduleType) {
        this.scheduleType = scheduleType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
}
