package com.ozguryazilim.telve.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dashbord veri modeli.
 * 
 * @author Hakan Uygun
 */
public class DashboardDataModel implements Serializable{
    
    
    /**
     * Tekbir tam ekran kolon
     */
    public static final int LAYOUT_ONE = 0;
    /**
     * İlki küçük 2 kolon
     */
    public static final int LAYOUT_ONE_TWO = 1;
    
    /**
     * İlki büyük 2 kolon
     */
    public static final int LAYOUT_TWO_ONE = 2;
    
    /**
     * İki eşit kolon
     */
    public static final int LAYOUT_TWO = 3;
    
    /**
     * Üç Eşit kolon
     */
    public static final int LAYOUT_TREE = 4;
    
    /**
     * Üstte 1 altta ilki küçük 2 kolon
     */
    public static final int LAYOUT_TOP_ONETWO = 5;
    
    /**
     * Üstte 1 altta ilki Büyük 2 kolon
     */
    public static final int LAYOUT_TOP_TWO_ONE = 6;
    
    /**
     * Üstte 1 altta eşit 2 kolon
     */
    public static final int LAYOUT_TOP_TWO = 7;
    
    /**
     * Altta 1 altta ilki küçük 2 kolon
     */
    public static final int LAYOUT_BOTTOM_ONETWO = 8;
    
    /**
     * Altta 1 altta ilki Büyük 2 kolon
     */
    public static final int LAYOUT_BOTTOM_TWO_ONE = 9;
    
    /**
     * Altta 1 altta eşit 2 kolon
     */
    public static final int LAYOUT_BOTTOM_TWO = 10;
    
    private String name;
    private List<String> column1 = new ArrayList<String>();
    private List<String> column2 = new ArrayList<String>();
    private List<String> column3 = new ArrayList<String>();
    private Integer layout = LAYOUT_ONE;
    private Boolean main = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getColumn1() {
        return column1;
    }

    public void setColumn1(List<String> column1) {
        this.column1 = column1;
    }

    public List<String> getColumn2() {
        return column2;
    }

    public void setColumn2(List<String> column2) {
        this.column2 = column2;
    }

    public List<String> getColumn3() {
        return column3;
    }

    public void setColumn3(List<String> column3) {
        this.column3 = column3;
    }

    public Integer getLayout() {
        if( layout == null ) layout = LAYOUT_ONE;
        return layout;
    }

    public void setLayout(Integer layout) {
        this.layout = layout;
    }

    public Boolean isMain() {
        return main;
    }

    public void setMain(Boolean main) {
        this.main = main;
    }
    
    /**
     * Bu modelde tanımlı olan tüm dashletleri döndürür
     * @return 
     */
    public List<String> getDashlets(){
        List<String> ls = new ArrayList<>();
        ls.addAll(column1);
        ls.addAll(column2);
        ls.addAll(column3);
        return ls;
    }
}
