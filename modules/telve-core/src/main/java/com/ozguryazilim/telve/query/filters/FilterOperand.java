package com.ozguryazilim.telve.query.filters;

/**
 * Filtreler için operand türleri.
 *
 * @author Hakan Uygun
 */
public enum FilterOperand {

    /**
     * Hiçbiri
     */
    None,
    /**
     * Hepsi
     */
    All,
    /**
     * Eşit
     */
    Equal,
    /**
     * Eşit Değil
     */
    NotEqual,
    /**
     * Büyük
     */
    Greater,
    /**
     * Büyük Eşit
     */
    GreaterOrEqual,
    /**
     * Küçük
     */
    Lesser,
    /**
     * Küçük Eşit
     */
    LesserOrEqual,
    /**
     * String - içerir
     */
    Contains,
    /**
     * String - içermez
     */
    NotContains,
    /**
     * String - başlar
     */
    BeginsWith,
    /**
     * String - Biter
     */
    EndsWith,
    /**
     * Bir aralık, lise içinde bulunma hali. Tarih tipi için özel olarak hafta,
     * ay gibi tarih aralığı demek
     */
    In,
    /**
     * İki değer arasında olma hali. Tarih, sayısal değerler için
     */
    Between,
    /**
     * Ağaç tipi hierarşiler için seçilen nodun altında olma
     */
    Under
}
