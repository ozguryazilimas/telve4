/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ozguryazilim.telve.query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.deltaspike.data.api.criteria.Criteria;
import org.joda.time.DateTime;

/**
 * Tarih Filtresi
 *
 * @author Hakan Uygun
 * @param <E>
 */
public class DateFilter<E> extends Filter<E, Date> {

    private static List<DateValueType> inTypes;
    private static List<DateValueType> betweenTypes;

    private DateValueType valueType = DateValueType.Today;
    private DateValueType valueType2 = DateValueType.Today;

    public DateFilter(SingularAttribute<? super E, Date> attribute, String label) {
        super(attribute, label);

        setOperands(Operands.getDateOperands());
        setOperand(FilterOperand.Equal);
    }

    @Override
    public void decorateCriteria(Criteria<E, ?> criteria) {
        if (getValue() != null) {

            switch (getOperand()) {
                case Equal:
                    calcDates();
                    criteria.eq(getAttribute(), getValue());
                    break;
                case NotEqual:
                    calcDates();
                    criteria.notEq(getAttribute(), getValue());
                    break;
                case Greater:
                    calcDates();
                    //criteria.gt(getAttribute(), getValue());
                    break;
                case GreaterOrEqual:
                    criteria.gtOrEq(getAttribute(), getValue());
                    break;
                case Lesser:
                    calcDates();
                    //criteria.lt(getAttribute(), getValue());
                    break;
                case LesserOrEqual:
                    calcDates();
                    criteria.ltOrEq(getAttribute(), getValue());
                    break;
                case In:
                    calcDates();
                    criteria.between(getAttribute(), getValue(), getValue2());
                    break;
                case Between:
                    calcDates();
                    criteria.between(getAttribute(), getValue(), getValue2());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public String getTemplate() {
        return "dateFilter";
    }

    /**
     * Veri tiplerine bakarak gerekli tarih hesaplamalarını yapar
     */
    public void calcDates() {

        DateTime dt = new DateTime();

        switch (getValueType()) {
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
            case ThisWeek:
                setValue(dt.dayOfWeek().withMinimumValue().toDate());
                setValue2(dt.dayOfWeek().withMaximumValue().toDate());
                break;
            case LastWeek:
                dt = dt.minusWeeks(1);
                setValue(dt.dayOfWeek().withMinimumValue().toDate());
                setValue2(dt.dayOfWeek().withMaximumValue().toDate());
                break;
            case NextWeek:
                dt = dt.plusWeeks(1);
                setValue(dt.dayOfWeek().withMinimumValue().toDate());
                setValue2(dt.dayOfWeek().withMaximumValue().toDate());
                break;
            case ThisMonth:
                setValue(dt.dayOfMonth().withMinimumValue().toDate());
                setValue2(dt.dayOfMonth().withMaximumValue().toDate());
                break;
            case NextMonth:
                dt = dt.plusMonths(1);
                setValue(dt.dayOfMonth().withMinimumValue().toDate());
                setValue2(dt.dayOfMonth().withMaximumValue().toDate());
                break;
            case LastMonth:
                dt = dt.minusMonths(1);
                setValue(dt.dayOfMonth().withMinimumValue().toDate());
                setValue2(dt.dayOfMonth().withMaximumValue().toDate());
                break;
            case NextTenDays:
                setValue(dt.toDate());
                setValue2(dt.plusDays(10).toDate());
                break;
            case LastTenDays:
                setValue(dt.minusDays(10).toDate());
                setValue2(dt.toDate());
                break;
            case LastOneYear:
                setValue(dt.minusYears(1).toDate());
                setValue2(dt.toDate());
                break;
            case NextOneYear:
                setValue(dt.toDate());
                setValue2(dt.plusYears(1).toDate());
                break;
        }

        //Tek günlü değerler için valueType 2 lazım onlara da bakıyoruz.
        if (FilterOperand.Between.equals(getOperand())) {
            switch (getValueType2()) {
                case Today:
                    setValue2(dt.toDate());
                    break;
                case Tomorrow:
                    setValue2(dt.plusDays(1).toDate());
                    break;
                case Yesterday:
                    setValue2(dt.minusDays(1).toDate());
                    break;
                case FirstDayOfMonth:
                    setValue2(dt.dayOfMonth().withMinimumValue().toDate());
                    break;
                case LastDayOfMonth:
                    setValue2(dt.dayOfMonth().withMaximumValue().toDate());
                    break;
                case FirstDayOfWeek:
                    setValue2(dt.dayOfWeek().withMinimumValue().toDate());
                    break;
                case LastDayOfWeek:
                    setValue2(dt.dayOfWeek().withMaximumValue().toDate());
                    break;
                case FirstDayOfYear:
                    setValue2(dt.dayOfYear().withMinimumValue().toDate());
                    break;
                case LastDayOfYear:
                    setValue2(dt.dayOfYear().withMaximumValue().toDate());
                    break;
                case TenDaysBefore:
                    setValue2(dt.minusDays(10).toDate());
                    break;
                case TenDaysAfter:
                    setValue2(dt.plusDays(10).toDate());
                    break;
            }
        }

    }

    /**
     * Operanda bakarak olası veri tiplerini geriye döner.
     *
     * @return
     */
    public List<DateValueType> getValueTypes() {

        if (getOperand() == FilterOperand.In) {
            if (inTypes == null) {
                inTypes = new ArrayList<>();
                inTypes.add(DateValueType.ThisWeek);
                inTypes.add(DateValueType.ThisMonth);
                inTypes.add(DateValueType.LastWeek);
                inTypes.add(DateValueType.LastTenDays);
                inTypes.add(DateValueType.LastMonth);
                inTypes.add(DateValueType.NextWeek);
                inTypes.add(DateValueType.NextTenDays);
                inTypes.add(DateValueType.NextMonth);
                inTypes.add(DateValueType.NextOneYear);
                inTypes.add(DateValueType.LastOneYear);
            }

            return inTypes;

        } else {

            if (betweenTypes == null) {
                betweenTypes = new ArrayList<>();
                betweenTypes.add(DateValueType.Date);
                betweenTypes.add(DateValueType.Today);
                betweenTypes.add(DateValueType.Tomorrow);
                betweenTypes.add(DateValueType.Yesterday);
                betweenTypes.add(DateValueType.FirstDayOfWeek);
                betweenTypes.add(DateValueType.FirstDayOfMonth);
                betweenTypes.add(DateValueType.FirstDayOfYear);
                betweenTypes.add(DateValueType.LastDayOfWeek);
                betweenTypes.add(DateValueType.LastDayOfMonth);
                betweenTypes.add(DateValueType.LastDayOfYear);
                betweenTypes.add(DateValueType.TenDaysAfter);
                betweenTypes.add(DateValueType.TenDaysBefore);
            }

            return betweenTypes;
        }

    }

    public DateValueType getValueType() {
        return valueType;
    }

    public void setValueType(DateValueType valueType) {
        this.valueType = valueType;
    }

    public DateValueType getValueType2() {
        return valueType2;
    }

    public void setValueType2(DateValueType valueType2) {
        this.valueType2 = valueType2;
    }

}
