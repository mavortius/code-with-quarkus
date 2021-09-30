package org.acme;

import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

public class GlobalErrorHandler {
  @ServerExceptionMapper(NotFoundException.class)
  public Response handleNotFoundException(NotFoundException e) {
    return Response.status(404).build();
  }
}
