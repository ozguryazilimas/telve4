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

package com.ozguryazilim.telve.idm.reports;

import com.ozguryazilim.telve.reports.ReportDate;
import com.ozguryazilim.telve.idm.entities.Group;
import java.io.Serializable;

/**
 * 
 * @author Aydoğan Sel <aydogan.sel at iova.com.tr>
 *
 */
public class UserRoleFilter implements Serializable {

    
    private ReportDate beginDate;
    private ReportDate endDate;
    private Group group;

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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
    
    
    
}
