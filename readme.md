Telve Java EE Framework
===================================

## Tanım

Telve Java EE 7 teknolojilerinin birarada kullanımını kolaylaştıran bir uygulama geliştirme çatısıdır.

Java EE 7 standart kütüphanelerinin üzerine Apache DeltaSpike, JBoss PicketLink, PrimeFaces, Apache Camel, ModeShape, Camunda, Bootstrap, Liquibase gibi önde gelen kütüphanelerin birarada kolayca kullanımını hedeflemektedir.

Uygulama geliştirici için temel ekran ve özellikler için yöntemler sağlarken, geliştiriciyi kısıtlamaz, Telve dışı kitaplıkları da kullanmasına olanak tanır.

Telve kullanımı ile birlikte uygulama geliştiriciler sadece geliştirecekleri uygulamaya odaklanabilirler.

JavaEE7 teknolojilerini kullanan Telve'nin yeni sürümü

## Özellikler

* Modülerlik : Telve uygulamaların modüler bir şekilde yazılabilmesi için CDI ve Maven kullanarak gerekli alt yapıyı sağlamaktadır.
* Uygulama navigasyonu: Telve Uygulama içi navigasyon ve yetki kontrollerini de içine alarak sadece bir tanımlama ile toplamakta ve oluşturmaktadır. Bir kullanıcının karşısına sadece yetkili olduğu menü seçeneklerini otomatik olarak sunmaktadır.
* Yetki kontrolü : Telve PicketLink ve Apache DeltaSpike bağlantılarını kullanarak rol ve aksiyon tabanlı yetkilendirme mekanizamasını içermektedir. Uygulama geliştiricinin sadece yetki domainlerini bir XML içerisinde tanımlaması yeterli olmaktadır. Seçimlik olarak kullanıcı tanımlama ve yetkilendirme ekranları hazır olarak kullanılabilir. Kullanıcı tanımları ve yetkileri, uygulama içerisinde veri tabanında kullanılabileceği gibi, LDAP ya da SSO üzerinden de alınabilir.
* Mesaj ve E-Posta : Telve içerisinde farklı mesaj kanallarının ( web push mesaj, e-posta v.b. ) ve şablonlarının kullanımına izin veren bir alt yapı ile gelmektedir. Uygulama geliştiricinin sadece ilgili API fonksiyonlarını çağırması yeterli olmaktadır.
* Sürüm ve Veri Tabanı değişim yönetimi : Telve içerisinde Liquibase ile gelmekte ve veri tabanı değişiklik yönetimini otomatik yapmaktadır. Maven sürüm politikalarını kullanarak gerekli sürüm ve güncelleme yönetimleri yapılabilir.
* Veri tabanı desteği : Telve, JPA 2.0 kullanmakta, dolayısı ile JPA 2.0’ın desteklediği her hangi bir veri tabanı ile çalışabilmektedir. Bu noktada uygulama geliştiriciler bir den fazla veri tabanını destekleyen uygulamalar geliştiribilirler.
* Çok dillilik : Telve içerisinde otomatik olarak çok dillilik desteği ile gelmektedir.
* Zamanlanmış görevler : Telve, zamanlanmış görev tanımları için alt yapı sağlamaktadır. Uygulama geliştiriciler zamanlanmış görev API’sini miras alan bir sınıf ile zamanlanmış görev tanımlamaları ve bu görevlerin uygulama yöneticisi tarafından kontrol edilebilmesi sağlanmaktadır.
* Uygulama Konsolu : Telve içerisinde uygulama ana konsolu ( dashborad ) için alt yapı barındırmaktadır. Kullanıcıların kendi konsollarını tanımlamasına imkan veren bu alt yapı için uygulama geliştirici sadece kendi dashlet’lerini yazması yeterli olmaktadır.
* Raporlama : Telve raporlar ve bunların yetkilendirmesi için alt yapı sağlamaktadır. Telve’ye gömülü olan JasperReports ile raporların hazırlanabileceği gibi farklı rapor motorları da eklenebilir.
* Tema desteği : Telve içinde gömülü olarak responsive uygulama geliştirmeye uygun Bootstrap CSS çatısı ile gelmektedir. PrimeFaces ve Bootstrap uygumlu temalar ile birlikte çalışabilir.
* Çevrim İçi Yardım : Telve seçimlik olarak kullanılabilecek, çevrim için yardım alt yapısı sunmaktadır. Bu yardım içeriği DITA ile hazırlanabileceği gibi Eclipse Help formatı destekleyen her hangi bir araç ile hazırlanabilir. Uygulama içinden bağlama göre gerekli yardım içeriği otomatik çağrılabilecektir.
* Süreç Motoru : Telve, seçimlik olarak kullanılabilecek, BPMN 2.0 standartına uygun Camnunda süreç motorunu gömülü olarak barındırmaktadır. Kullancı görev ekranları hazır olarak bulunmaktadır.
* Doküman Yönetim Sistemi : Telve, seçimlik olarak kullanılabilecek, JCR standartına uygun ModeShape doküman yönetim motorunu gömülü olarak barındırmaktadır. Uygulama içi belge yönetimi için kullanıma hazır çeşitli API ve arayüzler sunulmaktadır.
* Markalama : Telve’yi uygulama içinde kullanırken uygulamaya ait markalama işlemleri yapılması için gereken alt yapıyı sunmaktadır. Örneğin, Giriş ekranı arka plan resmi, Hakkında sayfası, Hoş geldiniz mesajı ve benzerleri uygulama geliştirici tarafından kolaylıkla değiştirilebilmektedir.
* Ekran / form hazırlama desteği : Telve seçimlik olarak kullanılabilecek, genel geçer ekran kontrolleri için taban sınıflar ve API’ler sağlamaktadır. Örneğin parametre tanım, ağaç parametre tanım, arama ekranları v.b. Uygulama geliştiriciler bu sınıfları miras alarak çok kısa sürede uygulama ekranlarını hazırlayabilmektedirler.
* Araç Desteği : Telve, seçimlik olarak, JBoss Forge bileşenleri ile kod üretme araçları sunmaktadır. JBoss Forge destekleyen ( NetBeans, Eclipse, Idea ) her hangi bir IDE ile birlikte ya da JBoss Forge konsolundan kullanılab

### Özgür Yazılım A.Ş. olarak,

Telve üzerinde geliştirme altyapıları kurulum, yapılandırma, eğitim ve destek hizmetlerinin yanı sıra, etkin kullanımınız için danışmanlık desteği veriyor ve kurumunuzun ihtiyaçları için özel eklentiler geliştiriyoruz.
