package com.ozguryazilim.telve.rest;

import com.ozguryazilim.telve.entities.StoredCommand;
import com.ozguryazilim.telve.messagebus.command.Command;
import com.ozguryazilim.telve.messagebus.command.ScheduledCommandService;
import com.ozguryazilim.telve.messagebus.command.ui.ScheduledCommandBrowse;
import com.ozguryazilim.telve.messagebus.command.ui.ScheduledCommandUIModel;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiresPermissions("admin")
@Path("/api/admin/scheduledCommand")
@Produces(MediaType.APPLICATION_JSON)
public class ScheduledCommandRest {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduledCommandRest.class);

    @Inject
    private ScheduledCommandService scheduledCommandService;

    @GET
    @Path("/storedCommands")
    public List<StoredCommand> getCommands() throws ClassNotFoundException {
        return scheduledCommandService.getStoredCommands();
    }

    @GET
    @Path("/storedCommands/{id}")
    public Response getCommand(@PathParam("id") Long id) throws ClassNotFoundException {
        if (id != null) {
            StoredCommand storedCommand = scheduledCommandService.getStoredCommandById(id);

            if (storedCommand != null) {
                return Response.status(Response.Status.OK).entity(storedCommand).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/storedCommands/execute/{id}")
    public Response executeCommand(@PathParam("id") Long id) throws ClassNotFoundException {
        if (id != null) {
            if (scheduledCommandService.runById(id)) {
                return Response.status(Response.Status.OK).build();
            }
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }


}
