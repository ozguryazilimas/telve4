<#ftl encoding="UTF-8">
${headers.messages["email.entity.Title"]} ${headers.entity.firstName} ${headers.entity.lastName}

${headers.messages["user.label.UserName"]} : ${headers.entity.loginName}
${headers.messages["user.label.Password"]} : ${headers.password}

${headers.messages["email.body.AccessApp"]} ${headers.telveConfigResolver.getProperty("app.linkDomain")} ${headers.messages["email.body.AccessAppFollowUp"]}.
--
provided by telve email channel