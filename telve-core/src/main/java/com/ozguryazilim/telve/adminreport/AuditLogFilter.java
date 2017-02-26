/*
 *   Copyleft 2013 IOVA SOFTWARE
 *   
 *  Distributable under LGPL license.
 *  See terms of license at gnu.org.
 *  http://www.gnu.org/licenses/lgpl.html
 *   
 *  www.openohs.com
 *  www.iova.com.tr
 */

package com.ozguryazilim.telve.adminreport;

import com.ozguryazilim.telve.reports.ReportDate;
import java.io.Serializable;

/**
 * 
 * @author erkin.eskier
 *
 */
public class AuditLogFilter implements Serializable {

    
    private ReportDate beginDate;
    
    
    private ReportDate endDate;

    public ReportDate getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(ReportDate beginDate) {
        this.beginDate = beginDate;
    }

    public ReportDate getEndDate() {
        return endDate;
    }

    public void setEndDate(ReportDate endDate) {
        this.endDate = endDate;
    }
    
    
    
    
}
