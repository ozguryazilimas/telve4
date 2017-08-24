/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.dashlets.bookmark;

import com.google.common.base.Strings;
import com.ozguryazilim.mutfak.kahve.Kahve;
import com.ozguryazilim.mutfak.kahve.KahveEntry;
import com.ozguryazilim.mutfak.kahve.annotations.UserAware;
import com.ozguryazilim.telve.dashboard.AbstractDashlet;
import com.ozguryazilim.telve.dashboard.Dashlet;
import com.ozguryazilim.telve.dashboard.DashletCapability;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Uygulama içinde bookmark sağlayacak dashlet.
 * 
 * @author Hakan Uygun
 */
@Dashlet(capability = {DashletCapability.canHide,  DashletCapability.canMinimize, DashletCapability.canRefresh})
public class BookmarkDashlet extends AbstractDashlet{

    @Inject @UserAware
    private Kahve kahve;
    
    private List<Bookmark> bookmarks = new ArrayList<>();
    private Bookmark bookmark = new Bookmark();

    /**
     * Kullanıcıya ait bookmark listesi.
     * @return 
     */
    public List<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    /**
     * New ya da Edit için kullanılacak olan değişken.
     * @return 
     */
    public Bookmark getBookmark() {
        return bookmark;
    }

    public void setBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    /**
     * Kahve üzerinden kayıtlı bookmarkları yükler.
     */
    @Override
    public void load() {
        bookmarks.clear();
        
        KahveEntry ke = kahve.get("bookmarkDashlet.count", 0 );
        int c = ke.getAsInteger();
        for( int i = 1; i <= c ; i++){
            Bookmark b = new Bookmark();
            ke = kahve.get("bookmarkDashlet.title." + i, 0 );
            b.setTitle(ke.getAsString());
            ke = kahve.get("bookmarkDashlet.link." + i, 0 );
            b.setLink(ke.getAsString());
            bookmarks.add(b);
        }
    }

    /**
     * Dashlet edit üzerinden gelen save komutunu işler.
     */
    @Override
    public void save() {
        //Eğer edit ediyorsak sadece saklayacağız.
        if( bookmarks.contains(bookmark)){
            saveAll();
        } else {
            addBookmark(bookmark);
        }
        //Değişkeni yeni veri girişine hazırlıyoruz.
        bookmark = new Bookmark();
    }
    
    /**
     * Verilen classifier boş değilse başlığa ekleyerek bookmark oluşturur.
     * @param title
     * @param classifier
     * @param link 
     */
    public void addBookmark( String title, String classifier, String link ){
        String t = title + ( Strings.isNullOrEmpty(classifier) ? "" : ( "[ " + classifier + " ]"));
        addBookmark(t, link);
    }
    
    /**
     * Verilen başlık ve linkle bookmark kaydı oluşturur.
     * @param title
     * @param link 
     */
    public void addBookmark( String title, String link ){
        Bookmark b = new Bookmark();
        b.setTitle(title);
        b.setLink(link);
        addBookmark(b);
    }
    
    /**
     * Verilen bookmark'ı listenin sonuna ekleyerek saklar.
     * @param bookmark 
     */
    public void addBookmark( Bookmark bookmark ){
        bookmarks.add(bookmark);
        saveAll();
    }
    
    /**
     * Tüm listeyi kahve üzerine saklar.
     */
    public void saveAll(){
        KahveEntry ke = new KahveEntry();
        ke.setAsInteger(bookmarks.size());
        kahve.put( "bookmarkDashlet.count", ke);
        
        int i = 1;
        for( Bookmark b : bookmarks ){
            ke = new KahveEntry();
            ke.setAsString(b.getTitle());
            kahve.put( "bookmarkDashlet.title." + i, ke);
            
            ke = new KahveEntry();
            ke.setAsString(b.getLink());
            kahve.put( "bookmarkDashlet.link." + i, ke);
            
            i++;
        }
    }
    
    /**
     * UI'den gelen index'i edit için değişkene alır.
     * @param inx 
     */
    public void edit( int inx ){
        bookmark = bookmarks.get(inx);
    }
    
    /**
     * UI'den gelen index'e sahip olan item'ı siler.
     * @param inx 
     */
    public void delete( int inx ){
        bookmarks.remove(inx);
        saveAll();
    }
    
    /**
     * UI'den yeni bookmark tanımı için komut.
     */
    public void newBookmark(){
        bookmark = new Bookmark();
    }
}
