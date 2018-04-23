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

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;

import com.ozguryazilim.telve.config.TelveConfigResolver;
import com.ozguryazilim.telve.idm.reports.config.IdmReportPages;
import com.ozguryazilim.telve.query.filters.DateValueType;
import com.ozguryazilim.telve.reports.JasperReportBase;
import com.ozguryazilim.telve.reports.Report;
import com.ozguryazilim.telve.reports.ReportDate;

/**
 * AuditLog'lar için standart rapor
 *
 * @author Aydoğan Sel <aydogan.sel at iova.com.tr>
 */
@Report( filterPage = IdmReportPages.UserRoleReport.class, permission="userRoleReport", path="/admin/user", template = "userRoleReport")
public class UserRoleReport extends JasperReportBase{

    @Inject
    private TelveConfigResolver telveConfigResolver;

    @Inject
    @Named("messages")
    private transient Map<String, String> messages;

    private UserRoleFilter filter;

    public UserRoleFilter getFilter() {
	if (filter == null) {
            buildFilter();
        }
        return filter;
    }

    public void setFilter(UserRoleFilter filter) {
        this.filter = filter;
    }

    public void buildFilter() {
        filter = new UserRoleFilter();

        filter.setEndDate(new ReportDate(DateValueType.Today));
        filter.setBeginDate(new ReportDate(DateValueType.Yesterday));
    }

    @Override
    protected boolean buildParam(Map<String, Object> params) {

        String logo = telveConfigResolver.getProperty("brand.company.reportLogo");
        String title = telveConfigResolver.getProperty("brand.company.reportTitle");

        if (logo != null) {
            BufferedImage image;
	    try {
		image = ImageIO.read(getClass().getResource("/META-INF/resources/brand/img/".concat(logo)));
		params.put("FIRM_LOGO", image);
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
        }
        params.put("FIRM_TITLE", title);
        params.put("GROUP_ID", getFilter().getGroup() == null ? 0 : getFilter().getGroup().getId());
        params.put("ISACTIVE", getFilter().getIncludeActives() && getFilter().getIncludePassives() ? 2:getFilter().getIncludeActives()?1:getFilter().getIncludePassives()?0:-1);

        params.put("ISACTIVE_PARAM_VALUE", getFilter().getIncludeActives() && getFilter().getIncludePassives() ? messages.get("general.value.All"):getFilter().getIncludeActives()?messages.get("general.label.Active"):getFilter().getIncludePassives()?messages.get("general.hint.Passive"):"");

        return true;
    }
}
