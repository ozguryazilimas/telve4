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
	
	public enum CategoryEnum {
		AUTH,
		PARAM,
		ENTITY,
		STATE_CHANGE
	}
	
	public enum ActionEnum {
		AUTH,
		INSERT,
		UPDATE,
		CREATE,
		DELETE,
		won,
		approved
	}

    
    private ReportDate beginDate;
    
    private ReportDate endDate;

    private String user;
    
    private String domain;
    
    private CategoryEnum categoryEnum;
    
    private ActionEnum actionEnum;
    
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public CategoryEnum getCategoryEnum() {
		return categoryEnum;
	}

	public void setCategoryEnum(CategoryEnum categoryEnum) {
		this.categoryEnum = categoryEnum;
	}

	public ActionEnum getActionEnum() {
		return actionEnum;
	}

	public void setActionEnum(ActionEnum actionEnum) {
		this.actionEnum = actionEnum;
	}
  
}
