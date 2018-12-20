package com.ozguryazilim.telve.reports;

import com.ozguryazilim.telve.query.filters.DateValueType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDate;

/**
 * Raporlar için özel date veri alma modeli.
 *
 * Bu model doğrudan tarih yerine çeşitli makrolar tutar. Bu sayede raporlar
 * için parametre olarak ayın ilk günü gibi makro değerleri girilebilir.
 *
 * @author Hakan Uygun
 */
public class ReportDate implements Serializable {

    private DateValueType valueType = DateValueType.Today;
    private Date date;
    private Date value;

    private static final List<DateValueType> VALUE_TYPES = new ArrayList<>();
    
    public ReportDate() {
    }

    public ReportDate(Date date) {
        this.date = date;
        this.valueType = DateValueType.Date;
    }

    public ReportDate(DateValueType valueType) {
        this.valueType = valueType;
    }

    public DateValueType getValueType() {
        return valueType;
    }

    public void setValueType(DateValueType valueType) {
        this.valueType = valueType;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getValue() {
        if (value == null) {
            calcDates();
        }
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }
    
    public Date getCalculatedValue() {
        value = null;
        return getValue();
    }

    /**
     * Veri tiplerine bakarak gerekli tarih hesaplamalarını yapar
     */
    public void calcDates() {

        LocalDate dt = new LocalDate();

        switch (getValueType()) {
            case Date:
                setValue(date);
                break;
            case Today:
                setValue(dt.toDate());
                break;
            case Tomorrow:
                setValue(dt.plusDays(1).toDate());
                break;
            case Yesterday:
                setValue(dt.minusDays(1).toDate());
                break;
            case FirstDayOfMonth:
                setValue(dt.dayOfMonth().withMinimumValue().toDate());
                break;
            case LastDayOfMonth:
                setValue(dt.dayOfMonth().withMaximumValue().toDate());
                break;
            case FirstDayOfWeek:
                setValue(dt.dayOfWeek().withMinimumValue().toDate());
                break;
            case LastDayOfWeek:
                setValue(dt.dayOfWeek().withMaximumValue().toDate());
                break;
            case FirstDayOfYear:
                setValue(dt.dayOfYear().withMinimumValue().toDate());
                break;
            case LastDayOfYear:
                setValue(dt.dayOfYear().withMaximumValue().toDate());
                break;
            case TenDaysBefore:
                setValue(dt.minusDays(10).toDate());
                break;
            case TenDaysAfter:
                setValue(dt.plusDays(10).toDate());
                break;
        }

    }
    
    public List<DateValueType> getValueTypes(){
        return new ArrayList(VALUE_TYPES);
    }

    static {
        VALUE_TYPES.add(DateValueType.Date);
        VALUE_TYPES.add(DateValueType.Today);
        VALUE_TYPES.add(DateValueType.Tomorrow);
        VALUE_TYPES.add(DateValueType.Yesterday);
        VALUE_TYPES.add(DateValueType.FirstDayOfWeek);
        VALUE_TYPES.add(DateValueType.LastDayOfWeek);
        VALUE_TYPES.add(DateValueType.TenDaysBefore);
        VALUE_TYPES.add(DateValueType.TenDaysAfter);
        VALUE_TYPES.add(DateValueType.FirstDayOfMonth);
        VALUE_TYPES.add(DateValueType.LastDayOfMonth);
        VALUE_TYPES.add(DateValueType.FirstDayOfYear);
        VALUE_TYPES.add(DateValueType.LastDayOfYear);
    }
}
