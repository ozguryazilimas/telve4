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
import java.util.TreeMap;
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
import com.ozguryazilim.telve.messages.Messages;

/**
 * Zamanlanmış Görevler için tarama UI control sınıfı.
 * 
 * Standart browse sınıfı değildir.
 * 
 * @author Hakan Uygun
 */
@WindowScoped
@Named
@Transactional
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
    
    private String filterForType;
    private String filterForName;
    private String filterForInfo;
    private Date filterForStartDate;
    private Date filterForEndDate;
            
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
            
            //Zamanlanmış Raporları pas geçiyoruz
            ScheduledCommand scheduledCommand = (ScheduledCommand)t.getInfo();
            if(scheduledCommand.getCommand().getClass().getSimpleName().equals("ReportCommand")){
             continue;
            }

            ScheduledCommandUIModel m = new ScheduledCommandUIModel();
            
            m.setScheduledCommand(scheduledCommand);
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

            String simpleName = sc.getCommand().getClass().getSimpleName();
            
            String selectedType="";
            
            //Filtre üzerinde görev tipi seçilmemişse filterForType değişkeni  
            //üzerinde işlem yapmak hataya sebep oluyor.(replace yapılmıyor.)
            if(filterForType !=null){
                selectedType = filterForType.replace("Editor", "").toLowerCase();
            }
            
            //Eğer herhangi bir filtre girilmemiş ise zamanlanmış görev listeye direk eklenir.
            if (Strings.isNullOrEmpty(filterForName) && Strings.isNullOrEmpty(filterForInfo)  && Strings.isNullOrEmpty(filterForType) &&  filterForStartDate == null && filterForEndDate == null ){
               filteredItems.add(sc);
            }
            else if(sc.getName().contains(filterForName)  && sc.getInfo().contains(filterForInfo) && simpleName.toLowerCase().contains(selectedType)){ 

                //Herhangi bir zaman aralığı filtrelemesi varsa buraya girer
                if(filterForEndDate != null || filterForStartDate != null){
                    
                    //Zaman aralığı filtrelemesi var ancak göreve bir zamanlama
                    //girilmemişse bu görevi pas geçiyoruz.
                    if(sc.getNextTimeout() == null){
                        continue;
                    }
                    //Bitiş tarihi girilmemişse, başlangıç tarihinden sonraki 
                    //tüm görevler kabul edilir.
                    if(filterForEndDate == null){
                        if(filterForStartDate.compareTo(sc.getNextTimeout()) <= 0){
                            filteredItems.add(sc);
                        }
                    }
                    //Başlangıç tarihi girilmemişse, bitiş tarihinden önceki 
                    //tüm görevler kabul edilir.
                    else if (filterForStartDate == null){
                        if(filterForEndDate.compareTo(sc.getNextTimeout()) >= 0){
                            filteredItems.add(sc);
                        }
                    }
                    //Başlangıç ve Bitiş tarihi girilmişse, verilen zaman aralığı
                    //arasında kalan tüm görevler kabul edilir.
                    else if(filterForEndDate != null && filterForStartDate != null){
                        if(filterForStartDate.compareTo(sc.getNextTimeout()) <= 0 && filterForEndDate.compareTo(sc.getNextTimeout()) >= 0){
                            filteredItems.add(sc);
                        }  
                    }                    
                }
                else{
                    //Zaman aralığı filtresi uygulanmamış ancak diğer filtre
                    //koşullarını sağlayan görev, listeye eklenir.
                    filteredItems.add(sc);
                }
            }

        }
        
    }
    //Filtre Alanını temizlemek ve tüm görevleri listeletmek için kullanılır.
    public void cleanFilter() throws ClassNotFoundException{
        filterForType = null;
        filterForName = null;
        filterForInfo = null;
        filterForStartDate = null;
        filterForEndDate = null;
        populateFilteredItems();
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
        List<String> edNames = CommandEditorRegistery.getEditorNames();
        HashMap<String,String> hm = new HashMap<String,String>();
        String resName = "";
        for (String st : edNames) {
            resName = Messages.getMessage( "command.editor." + st);
            if (!Strings.isNullOrEmpty(resName)) {
                hm.put(resName, st );
            } else hm.put(st, st );
        }
        Map<String, String> treeMap = new TreeMap<String, String>(hm);
        
        return new ArrayList<String>(treeMap.values());
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
        //Eğer seçili item yoksa çıkalım.
        if( selectedItem == null ){
            FacesMessages.warn("options.message.NotSelected");
            return;
        }
               
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
    
    public void delete() throws ClassNotFoundException{
        //Eğer seçili item yoksa çıkalım.
        if( selectedItem == null ){
            FacesMessages.warn("options.message.NotSelected");
            return;
        }
        //Önce timer'a bir bakalım. Varsa onu bir silelim...
        if( selectedItem.getScheduledCommand() != null ){
            scheduler.removeFromScedular(selectedItem.getScheduledCommand());
        }
        entity = selectedItem.getStoredCommand();
        repository.remove(entity);
        search();
        selectedItem = null;
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
        //Eğer seçili item yoksa çıkalım.
        if( selectedItem == null ){
            FacesMessages.warn("options.message.NotSelected");
            return;
        }
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
        if( selectedItem == null ){
            FacesMessages.warn("options.message.NotSelected");
            return;
        }
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
        if( selectedItem == null ){
            FacesMessages.warn("options.message.NotSelected");
            return;
        }
        
        
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
        
         //Eğer seçili item yoksa çıkalım.
        if( selectedItem == null ){
            FacesMessages.warn("options.message.NotSelected");
            return;
        }
        
        auditLogger.actionLog("ScheduledCommand", 0l, selectedItem.getCommand().getName(), "ScheduledCommand", "EXEC", identity.getLoginName(), "");
        
        commandSender.sendCommand(selectedItem.getCommand());
    }

    public ScheduledCommandUIModel getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(ScheduledCommandUIModel selectedItem) {
        this.selectedItem = selectedItem;
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

    public String getFilterForType() {
        return filterForType;
    }

    public void setFilterForType(String filterForType) {
        this.filterForType = filterForType;
    }

    public String getFilterForName() {
        return filterForName;
    }

    public void setFilterForName(String filterForName) {
        this.filterForName = filterForName;
    }

    public String getFilterForInfo() {
        return filterForInfo;
    }

    public void setFilterForInfo(String filterForInfo) {
        this.filterForInfo = filterForInfo;
    }

    public Date getFilterForStartDate() {
        return filterForStartDate;
    }

    public void setFilterForStartDate(Date filterForStartDate) {
        this.filterForStartDate = filterForStartDate;
    }

    public Date getFilterForEndDate() {
        return filterForEndDate;
    }

    public void setFilterForEndDate(Date filterForEndDate) {
        this.filterForEndDate = filterForEndDate;
    }
 
}
