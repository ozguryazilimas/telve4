package com.ozguryazilim.telve.query;

/**
 * Tarih filtreleri için parametrik tarih değerleri
 * 
 * Today, Tomorrow gibi. Lazım olan tarihte hesaplanırlar.
 * 
 * @author Hakan Uygun
 *
 */
public enum DateValueType {

	/**
	 * Dorudan girilen bir tarih.
	 */
	Date,
	/**
	 * Tarih - Bugün
	 */
	Today,
	/**
	 * Tarih - Dün
	 */
	Yesterday,
	/**
	 * Tarih - Yarın
	 */
	Tomorrow,
	/**
	 * Tarih - Bu hafta
	 */
	ThisWeek,
	/**
	 * Tarih - Geçen Hafta
	 */
	LastWeek,
	/**
	 * Tarih - Önümüzdeki Hafta
	 */
	NextWeek,
	/**
	 * Tarih - Son 10 Gün
	 */
	LastTenDays,
	/**
	 * Tarih - Önümüzdeki 10 gün
	 */
	NextTenDays,
	/**
	 * Tarih - Bu AyYedi
	 */
	ThisMonth,
	/**
	 * Tarih - Geçen Ay
	 */
	LastMonth,
	/**
	 * Tarih - Önüüzdeki Ay
	 */
	NextMonth,
	FirstDayOfWeek,
	LastDayOfWeek,
	FirstDayOfMonth,
	LastDayOfMonth,
	FirstDayOfYear,
	LastDayOfYear,
	TenDaysBefore,
	TenDaysAfter,
	LastOneYear,
	NextOneYear
}
