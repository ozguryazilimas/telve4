package com.ozguryazilim.telve.reports;

/**
 *
 * @author @author Aydoğan Sel <aydogan.sel at iova.com.tr>>
 */
public interface ScheduledReportHandler {
    
    void execute(ReportCommand command);
    
}
