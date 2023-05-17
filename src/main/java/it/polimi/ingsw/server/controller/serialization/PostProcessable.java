package it.polimi.ingsw.server.controller.serialization;

public interface PostProcessable {
      default void gsonPostProcess() {}
  }

