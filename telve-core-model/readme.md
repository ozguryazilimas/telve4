Liquibase Kullanımı
===================

Türkçe yerel üzerinde "İ" ile ilgili sorun çıkıyor LANG=en_US.UTF-8 ile locale değiştirin.

Uygulanacak değişiklikleri çıkarma
-----------------------

`mvn clean compile liquibase:diff -Pliquibase` komutu ile Entity tanımları ile veri tabanı arasındaki fark çıkartılır. 
Yeni çıkartılan fark bilgisi src/main/resources/liquibase/migration/changelog-XXXXX.xml dosyasına kaydedilir. XXXX yerine buildTimeStamp gelir.

Değişiklikleri Uygulama
----------------------------
`mvn liquibase:update -Pliquibase` komutu ile değişiklikler veri tabanına uygulanır.


Tabloları silme
------------------------
`mvn liquibase:dropAll -Pliquibase` komutu ile tablolar silinir


Durum kontrolü
------------------------
`mvn liquibase:status -Pliquibase` komutu ile durum kontrolü yapılabilir