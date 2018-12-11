<#ftl encoding="UTF-8">
${request.setHeader("icon", "fa fa-file-pdf-o")}
${request.setHeader("severity", "info")}
${request.setHeader("link", "#")}
${request.setHeader("subject", "Zamanlanmış Rapor Hazırlandı")}
${headers.messages["report.name." + headers.reportName]} : ${headers.info}