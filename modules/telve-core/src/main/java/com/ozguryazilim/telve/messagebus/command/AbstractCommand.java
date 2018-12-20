package com.ozguryazilim.telve.messagebus.command;

/**
 * Command interface'i için taban sınıf.
 * 
 * Kullanıcının göreceği komut adını sınıf adı olarak döndürür.
 * 
 * @author Hakan Uygun
 */
public abstract class AbstractCommand implements Command{

    /**
     * Geriye komut adı olarak sınıf adını döndürür.
     * @return 
     */
    @Override
    public String getName(){
        return this.getClass().getSimpleName();
    }

}
