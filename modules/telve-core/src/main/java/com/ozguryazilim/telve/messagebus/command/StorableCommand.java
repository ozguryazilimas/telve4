package com.ozguryazilim.telve.messagebus.command;

/**
 * Kullanıcıdan / Servisler'den alınan verilerle saklanabilecek komut türlerini tanımlar.
 * 
 * Serializable gibi sadece bir işaretçi.
 * 
 * Özellikle Günsonu vb. tanımlanacak işler için kullanılır. Bu tür komutlar için CommandEditor'ler hazırlanabilir.
 * 
 * @author Hakan Uygun
 */
public interface StorableCommand extends Command{
    
    /**
     * Kullanıcı tarafından komut adı değiştirilebilir.
     * 
     * Bu sayede aynı komutlar sistemde farklı komut olarak değerlendirilip dolaşabilir.
     * 
     * @param name 
     */
    void setName( String name );
    
}
