package it.polimi.ingsw.server.model;

public interface PostProcessable {
      default void gsonPostProcess() {}
  }

