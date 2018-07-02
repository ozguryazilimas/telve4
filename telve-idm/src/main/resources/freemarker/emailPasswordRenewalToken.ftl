<#ftl encoding="UTF-8">
${request.setHeader("subject", headers.telveConfigResolver.getProperty("app.title") + " " + headers.messages["email.subject.PasswordRenewal"])}

${headers.messages["email.body.PasswordRenewal"]}

${headers.telveConfigResolver.getProperty("app.linkDomain")}/passwordrenewal.xhtml?token=${headers.token}
--
provided by telve email channel