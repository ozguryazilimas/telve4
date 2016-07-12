# Telve Unit

Birim tipine göre miktar tutlan alanlarda, örneğin : Faturalarda verilen Ürün, Servis miktarları için kullanılacak olan bir kütüphane.

Birim listesi kullanıcıdan alınabilir. Fakat bununla ilgili bu kütüphane içerisinde bir kod bulunmamaktadır. Gerekn kodların uygulama tarafında hazırlanması gerekir.

TODO: Unit ve Quantity için JPA, JSF converter'ları hazırlanacak.
TODO: Hazır Dimension'lar sağlanacak : Mass, Time, Length, Volume, 
TODO: Hzır dimension'ların kullanımı için conf yapısı gerekir 

## Birim Sistemi Unit, UnitName, UnitSet


### UnitSet

Sistemde bir birinden bağımsız faklı birimler bulunabilir. Bunların toplandığı gruplar `UnitSet` - Birim Kümesi olarak adlandırıyoruz.

Her UnitSet kendine ait bir isme sahiptir. Sistemde kodlanmış olarak bulunan UnitSet'lerin yanında kullanıcıya tanıttırılacak, veri tabanında saklanacak boyutlarda bulunabilir.

Her UnitSet, `UnitSetRegistery` de tanıtılmış olması gerekir ki sistem bunların içerisinde gerekli çevrim işlemlerini yapabilsin.

### UnitName

Bir birimin tanımı `UNITSET:NAME` formatı ile yapılmaktadır. `UnitName` sınıfı bu ismi doğrular, parse eder ve gerekli karşılaştırma işlemlerini sağlar.

```java

public void test(){

    UnitName isgunu = UnitName.of( "ZAMAN", "ISGUNU" );

    System.out.println( isgunu );

}

```


### Unit

Birimlerin bir birleriyle olan temel tanımını taşır. 

Bir birim bir başka birim ile olan çevrimini de beraberinde getirecektir. Bu `BaseUnit` olarak adlandırılıyor.

Sistemde çevrimlerin sağlıklı olması için temel birim kullanılcak en küçük birim olacaktır. Örneğin dakika seviyesin de birim tanımlanacak ise Dimension için BaseUnit dakika olmalı ve diğerleri buna relatif olarak tanımlanmalıdır.

```java
UnitSetBuilder.create("ZAMAN", "SAAT")
                .addUnit("GUN", Quantity.of( 24, "ZAMAN:SAAT"))
                .addUnit("ISGUN", Quantity.of( 8, "ZAMAN:SAAT"))
                .addUnit("EGGUN", Quantity.of( 6, "ZAMAN:SAAT"))
                .register();
```


## Quantity

Temel Kavramımız `Quantity`.

İçerisinde `BigDecimal` olarak miktar ve o miktarın birimini ( `Unit` ) bulundurur.

Imutable'dır. Dolayısı ile bir değer sadece bir kez oluşturulur.

Örnek kullanım : 

```java

public void test(){

      Quantity q1 = Quantity.of( 5, "ZAMAN:GUN" );
      Quantity q2 = Quantity.of( 15, "ZAMAN:GUN" );

      Quantity result = q1.add( q2 );

      System.out.println( result );
}

```

Quantity'i oluşturuken birim isimlerini `UNITSET:NAME` formatında verilebilir. Ayrıca önceden tanımlanmış birim isimleri varsa onlarda kullanılabilir.

```java

public void test(){

      Quantity q1 = Quantity.of( 5, MassUnitSet.GRAM );
      Quantity q2 = Quantity.of( 15, MassUnitSet.GRAM );

      Quantity result = q1.add( q2 );

      System.out.println( result );
}

```

