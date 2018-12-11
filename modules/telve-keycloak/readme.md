
# Telve - Keycloak entegrasyonu

Telve yetki kontrolü için Shiro kullanıyor. Dolayısı ile aslında yapılan işlem bir Shiro filtresi hazırlamak ve KeyCloak filtresi ile ilişkilendirmek oldu.

Öncelikle KeyCloak Servlet filtresi gelen bütün requestleri ( javax.faces.resource ve primepush dışında kalanları ) dinliyor ve auth olmamış ise KeyCloak'a yönlendiriyor.
Auth olmuş olan requestleri Shiro filtresi yakalıyor ve eğer shiro login değil ise shiroya login işlemini gerçekleştiriyor. 
Bu sırada TelveKeyCloakRealm sınıfı devreye giriyor ve eğer kullanıcı Telve IDM User listesinde yoksa ekliyor. JWT üzerinden gelen rollere ait yetkileri topluyor ve sisteme salıyor.
Son olarak her page geçişi sırasında DeltaSpike Security filtresi araya giriyor ve Shiro ya ilgili yere girmek için gereken Permission var mı diye soruyor.

## Çalışması için yapılması gerekenler

1. KeyCloak üzerinde uygulama registration'ı yapılmalı
2. WEB-INF/keycloak.json dosyasına registration'dan alınan config değerleri konmalı
3. Web projesi bağımlılık listesine telve-keycloak eklenmeli
4. shiro.ini dosyasına şu tanımlar eklenmeli 

```
keycloakRealm=com.ozguryazilim.telve.idm.TelveKeyCloakRealm
keycloakFilter=com.ozguryazilim.telve.idm.KeyCloakAuhtcFilter

;securityManager.realms= $telveRealm
securityManager.realms= $keycloakRealm

```

TelveKeyCloakRealm, TelveIdmRealm'ı miras alıyor. Dolayısı ile onuniçin geçerli ayarların hepsini devir alabilir.

## Yapılması gerekenler

1. Logout konusunda biraz fikir yürütmeli
2. Shiro filtresini iptal edip sadece KeyCloak filtresini miras alan bir filtre yapılabilir mi? Bu login/logout kontrollerinde işimizi kolaylaştırabilir.
3. Telve IDM Kullanıcı, Role ve Group tanım ekranlarının biraz değişmesi / KeyCloak varken kullanıma kapanması gibi seçenekleri bir düşünmeli.