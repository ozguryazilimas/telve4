${request.setHeader("icon", "fa fa-tasks")}
${request.setHeader("severity", "info")}
${request.setHeader("link", "/bpm/taskConsole.jsf?task="+ headers.TaskId)}
${request.setHeader("subject", "Yeni bir görev atandı")}
${headers.messages["processName." + headers.TaskProcess]} : ${headers.TaskSubject}