/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ozguryazilim.telve.query;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import com.ozguryazilim.telve.query.filters.Filter;
import com.ozguryazilim.telve.entities.ViewModel;
import com.ozguryazilim.telve.data.RepositoryBase;
import com.ozguryazilim.telve.entities.EntityBase;
import com.ozguryazilim.telve.messages.FacesMessages;
import com.ozguryazilim.telve.messages.Messages;
import com.ozguryazilim.telve.query.columns.Column;
import com.ozguryazilim.telve.utils.DateUtils;
import com.ozguryazilim.telve.utils.StringUtils;
import com.ozguryazilim.telve.view.PageTitleResolver;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sorgular View Control Sınıfları için taban sınıf.
 * 
 * FIXME: Repository'lere transfer edilen methodlar temizlenmeli
 * 
 * @author Hakan Uygun
 * @param <E> Entity sınıfı
 * @param <R> Result sınıfı
 */
public abstract class QueryControllerBase<E extends EntityBase,R extends ViewModel> implements Serializable{

    private static final Logger LOG = LoggerFactory.getLogger(QueryControllerBase.class);

    private static final String CSV = ".csv";
    private static final String CSV_MIME = "application/csv";
    
    /**
     * Sorgu bilgilerini tutan sınıf
     */
    private QueryDefinition<E, R> queryDefinition = new QueryDefinition<>();
    
    private List<R> entityList = Collections.emptyList();
    
    protected R selectedItem;
    protected R[] selectedItems;
    
    //Sorguyu saklamak için kullanılacak değerler.
    private String queryName;
    //Yeni sorgu ismi için geçici alan.
    private String newQueryName;
    private Boolean defaultQuery = false;
    private Boolean personal = true;
    
    private Map<String,QueryModel> systemQueries = new HashMap<>();
    
    @Inject @UserAware
    Kahve kahve;
    
    @Inject
    PageTitleResolver pageTitleResolver;

    @Inject
    private FacesContext facesContext;
    
    /**
     * GUI için sorgu özellikleri tanımlanır.
     * @param queryDefinition 
     */
    protected abstract void buildQueryDefinition( QueryDefinition<E, R> queryDefinition );
    
    protected abstract RepositoryBase<E,R> getRepository();
    
    @PostConstruct
    public void init(){
        buildQueryDefinition(queryDefinition);
        loadSystemQueries();
        loadDefaultQuery();
        
    }

    
    public QueryDefinition<E, R> getQueryDefinition() {
        return queryDefinition;
    }

    protected void decorateFilters( Criteria<E,R> criteria ){
        for( Filter<E, ?, ?> f : queryDefinition.getFilters() ){
            f.decorateCriteria(criteria);
        }
    }
    
    /**
     * Sıralamaları ekler.
     * @param criteria 
     */
    protected void decorateOrders( Criteria<E,R> criteria ){
        //FIXME: İçeriği yazılacak
    }
    
    /**
     * Alt sınıflar bu methodu override ederek sorguya yeni şeyler ekleyebilirler.
     * @param criteria 
     */
    protected void decorateCriteria( Criteria<E,R> criteria ){
        
    }
    
    /**
     * Reporsitory üzerinden sorgu düzenler.
     * 
     * repositorye UI'dan seçilen parametreleri gönderir.
     * 
     * @return 
     */
    protected List<R> executeCriteria(){
        return getRepository().browseQuery(queryDefinition);
    }
    
    public void search(){
        entityList = executeCriteria();
        selectedItem = null;
        selectedItems = null;
    }
    
    /**
     * Sadece ana filtrelerin çalıştırılıp, quick filterların çalıştırılmadığı sorgu
     */
    public void mainSearch(){
        
        //Öncesinde quick filterların içini boşaltıyoruz.
        for( Filter f : queryDefinition.getQuickFilters() ){
            f.setValue(null);
        }
        
        search();
    }

    public List<R> getEntityList() {
        return entityList;
    }

    public R getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(R selectedItem) {
        this.selectedItem = selectedItem;
    }

    public R[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(R[] selectedItems) {
        this.selectedItems = selectedItems;
    }
    
    public void save(){
    
        //Eğer yeni bir sorgu kaydı ise newQueryName dolu olacaktır. Aksi halde eski bir sorgu güncelleniyordur.
        if( !Strings.isNullOrEmpty(newQueryName)){
            queryName = newQueryName;
            newQueryName = "";
        }
        
        QueryModel model = new QueryModel();
        model.setName(queryName);
        model.setResultLimit(queryDefinition.getResultLimit());
        model.setRowLimit(queryDefinition.getRowLimit());
        
        for( Column c : queryDefinition.getColumns()){
            model.getColumns().add(c.getName());
        }
        
        //Sadece visible filtreleri saklayacağız. Visible olmayanlar programla yönetiliyorlar.
        for( Filter c : queryDefinition.getVisibleFilters()){
            model.getFilters().add(c.getAttribute().getName());
            model.getFilterValues().put(c.getAttribute().getName(), c.serialize());
        }
        
        
        //Sorterları saklıyoruz
        for( Column c : queryDefinition.getSorters()){
            model.getSorters().add(c.getName());
            model.getSorterValues().put(c.getName(), c.getSortAsc() ? "A" : "D" );
        }
        
        Gson gson = new Gson();
        String s = gson.toJson(model);

        System.out.println(s);
        
        kahve.put( getQueryKey( queryName ), s );
        
        if( defaultQuery ){
            kahve.put( getDefaultQueryKey(), queryName);
        }
        
        //Sorgu adı yoksa listeye ekleniyor. 
        List<String> ls = new ArrayList(getQueryNames());
        if( !ls.contains(queryName)){
            ls.add(queryName);
        }
        
        String ss = Joiner.on(',').join(ls);
        kahve.put(getQueryNamesKey(), ss );
    }

    public void deleteQuery(){
        deleteQuery(queryName);
    }
    
    public void deleteQuery(String name){
        kahve.remove(getQueryKey( name ));
        
        List<String> ls = new ArrayList(getQueryNames());
        if( ls.contains(name)){
            ls.remove(name);
        }
        
        String ss = Joiner.on(',').join(ls);
        kahve.put(getQueryNamesKey(), ss );
        //Uygulama tanımlı ilk sorguya dönüyoruz.
        loadQuery("");
    }
    
    public void loadFromSaved( String name ){
        KahveEntry e = kahve.get( getQueryKey(name) );
        if( e == null ) {
            LOG.error("Saved Query not found : {}", getQueryKey(name) );
            return;
        }
        
        Gson gson = new Gson();
        QueryModel model = gson.fromJson(e.getAsString(), QueryModel.class);
        
        loadFromModel(model);
    }
    
    protected void loadFromModel( QueryModel model ){
        queryName = model.getName();
        queryDefinition.setResultLimit(model.getResultLimit());
        queryDefinition.setRowLimit(model.getRowLimit());
        
        queryDefinition.getColumns().clear();
        for( String s : model.getColumns() ){
            Column< ? super E > c = queryDefinition.findColumnByName(s);
            if( c != null ){
                queryDefinition.getColumns().add( c );
            }
        }
        
        //Filtre değerlerini setliyoruz
        for( String s : model.getFilters() ){
            Filter f = queryDefinition.findFilterByName(s);
            if( f != null ){
                String val = model.getFilterValues().get(s);
                if( val != null ){
                    f.deserialize(val);
                }   
            }
        }
        
        //Sıralama bilgilerini düzenliyoruz.
        queryDefinition.getSorters().clear();
        for( String s : model.getSorters()){
            Column c = queryDefinition.findColumnByName(s);
            String val = model.getFilterValues().get(s);
            c.setSortAsc("A".equals(val));
            queryDefinition.getSorters().add( c );
        }
    }
    
    protected void loadFromSystem( String name ){
        QueryModel model = systemQueries.get(name);
        if( model != null ){
            loadFromModel(model);
        }
    }
    
    /**
     * Saklanmış tüm sorguları siler.
     */
    public void deleteAll(){
        List<String> ls = getQueryNames();
        for( String s : ls ){
            kahve.remove( getQueryKey(s));
        }
        kahve.remove(getQueryNamesKey());
        kahve.remove(getDefaultQueryKey());
    }
    
    /**
     * saklanmış olan sorguların isim listesini döner.
     * @return 
     */
    public List<String> getQueryNames(){
        KahveEntry e = kahve.get(getQueryNamesKey());
        List<String> ls;
        if( e != null ){
            ls = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(e.getAsString());
        } else {
            ls = new ArrayList<>();
        }
        
        return ls;
    }
    
    /**
     * Sistem sorgularının isim listesini döndürür.
     * @return 
     */
    public List<String> getSystemQueryNames(){
        return new ArrayList<>(systemQueries.keySet());
    }
    
    /**
     * Verilen isimli sorguyu yükler
     * @param name 
     */
    public void loadQuery( String name ){
        queryName = name;
        if( systemQueries.containsKey(name)){
            loadFromSystem(name);
        } else {
            loadFromSaved( name );
        }
        kahve.put( getDefaultQueryKey(), queryName);
        search();
    }
    
    /**
     * Varsayılan sorguyu yükler yosak olduğu gibi bırakır.
     */
    public void loadDefaultQuery(){
        KahveEntry e = kahve.get(getDefaultQueryKey());
        if( e != null ){
            loadQuery(e.getAsString());
        } else {
            loadQuery(getDefaultQueryKey());
        }
    }

    
    /**
     * QueryController için sistem tarafından önceden tanımlanmış olan sorguları yükler.
     * 
     * Bu sorgular /Sınıfİsmi.queries.json dosyalarında bulunur.
     * 
     * Aynı isimli dosya birden fazla jar içinde tanımlanabilir. Böylece farklı modüller farklı sorgular tanımlayabilirler.
     * 
     * JSON formatı QueryModel listesinden oluşur.
     */
    protected void loadSystemQueries(){
        systemQueries.clear();
        
        try {
            Enumeration<URL> jsons = getClass().getClassLoader().getResources("/" +getClass().getSimpleName() + ".queries.json");
            
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<QueryModel>>() {}.getType();
            
            while( jsons.hasMoreElements() ){
                InputStream is = jsons.nextElement().openStream();
                if( is != null ){
                    Reader r = new InputStreamReader(is);
                    List<QueryModel> models = gson.fromJson( r, collectionType);
                    for( QueryModel m : models ){
                        systemQueries.put(m.getName(), m);
                    }
                }
            }
        } catch (IOException ex) {
            LOG.error("IO Error on SystemQueries Load", ex);
        }

        /*
        InputStream is = getClass().getResourceAsStream("/" +getClass().getSimpleName() + ".queries.json");
        if( is != null ){
            Reader r = new InputStreamReader(is);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<List<QueryModel>>() {}.getType();
            List<QueryModel> models = gson.fromJson( r, collectionType);
            for( QueryModel m : models ){
                systemQueries.put(m.getName(), m);
            }
        }*/
    }
    
    public String getQueryName() {
        if( Strings.isNullOrEmpty(queryName)){
            queryName = pageTitleResolver.getPageTitle();
        }
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public Boolean getDefaultQuery() {
        return defaultQuery;
    }

    public void setDefaultQuery(Boolean defaultQuery) {
        this.defaultQuery = defaultQuery;
    }

    public Boolean getPersonal() {
        return personal;
    }

    public void setPersonal(Boolean personal) {
        this.personal = personal;
    }

    /**
     * Geriye ilgili browse için sorgu isimlerinin saklandığı kahve key'ini döndürür.
     * @return 
     */
    protected String getQueryNamesKey(){
        return "query.names." + getClass().getSimpleName();
    }
    
    /**
     * Geriye ilgili brose için sorgu key'ini döndürür.
     * @param s
     * @return 
     */
    protected String getQueryKey( String s ){
        return "query." + getClass().getSimpleName() + "." + s;
    }
    
    /**
     * Geriye ilgili brose için varsa default sorgunun saklandığı key'i döndürür.
     * @return 
     */
    protected String getDefaultQueryKey(){
        return "query.default." + getClass().getSimpleName();
    }

    public String getNewQueryName() {
        return newQueryName;
    }

    public void setNewQueryName(String newQueryName) {
        this.newQueryName = newQueryName;
    }

    /**
     * Verilen isimli sorgunun sistem sorgusu olup olamadığı.
     * @param name
     * @return 
     */
    public Boolean getIsSystemQuery( String name ){
        return systemQueries.containsKey(name);
    }
    
    public Boolean getIsSystemQuery(){
        return getIsSystemQuery( queryName );
    }
    
    /**
     * Query Sonucunu CVS olarak export eder.
     */
    public void export() throws IOException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        
        //Önce sorguyu bir daha çalıştıralım.
        search();
        
        StringWriter doc = new StringWriter();
        
        //Önce başlığı bir yazalım.
        exportHeader( doc );
        
        for( R e : entityList ){
            exportRow( e, doc );
        }
        
        LOG.debug(doc.toString());
        
        sendExport(doc.toString().getBytes("UTF-8"));
    }
    
    protected void sendExport( byte[] data ){
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.reset();
        response.setContentType(CSV_MIME);
        
        //TODO: FileName nasıl olsun?
        String fileName = getExportFileName() + CSV;
        
        response.setHeader("Content-disposition", "attachment;filename=" + fileName );
        response.setContentLength(data.length);

        try {

            try (OutputStream out = response.getOutputStream()) {
                out.write(data);

                out.flush();
            }

            facesContext.responseComplete();
        } catch (IOException ex) {
            FacesMessages.error("Error while downloading the file: " + fileName );
        }

    }

    protected String getExportFileName(){
        String f = pageTitleResolver.getPageTitle();
        String qn = getQueryName();
        //Eğer sorgu ismi yoksa default aynı değer
        boolean addqn = !f.equals(qn);
            
        String result = Messages.getMessage(f);
        
        if( addqn ) {
            result = result + "-" + qn;
        }
        
        result = result + "-" + DateUtils.getDateTimeFormatter().print(new DateTime());
        
        //TODO: isimdeli türkçe karakter, boşluk v.s temizlenmeli.
        result = result.replaceAll(" ", "_");
        
        result = StringUtils.escapeTurkish(result);
        
        return result;
    }
    
    protected void exportHeader( Writer doc) throws IOException{
        boolean firstColumn = true;
        
        for( Column c : queryDefinition.getColumns()){
            if( !firstColumn ){
                doc.write(",");
            } else {
                firstColumn = false;
            }
            doc.write(Messages.getMessage(c.getLabelKey()));
        }
        doc.write("\n");
    }
    
    protected void exportRow( R row, Writer doc) throws IOException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, NoSuchMethodException{
        boolean firstColumn = true;
        
        for( Column c : queryDefinition.getColumns()){
            if( !firstColumn ){
                doc.write(",");
            } else {
                firstColumn = false;
            }
            
            c.export(row, doc);
            
        }
        doc.write("\n");
    }
    
}
