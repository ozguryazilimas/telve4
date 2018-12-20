package com.ozguryazilim.telve.idm.reports;

import java.io.Serializable;

import com.ozguryazilim.telve.idm.entities.Group;
import com.ozguryazilim.telve.reports.ReportDate;

/**
 *
 * @author Aydoğan Sel <aydogan.sel at iova.com.tr>
 *
 */
public class UserRoleFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private ReportDate beginDate;
    private ReportDate endDate;
    private Group group;
    private Boolean includeActives = true;
    private Boolean includePassives = true;

    public Boolean getIncludeActives() {
	return includeActives;
    }

    public void setIncludeActives(Boolean includeActives) {
	this.includeActives = includeActives;
    }

    public Boolean getIncludePassives() {
	return includePassives;
    }

    public void setIncludePassives(Boolean includePassives) {
	this.includePassives = includePassives;
    }

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
