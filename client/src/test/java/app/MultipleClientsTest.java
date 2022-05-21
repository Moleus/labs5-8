package app;

import commands.ExecutionPayload;
import communication.*;
import communication.packaging.BaseRequest;
import model.data.ModelDto;
import model.data.Coordinates;
import model.data.FlatDto;
import model.data.House;
import model.data.View;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import user.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MultipleClientsTest {
  private static final List<ClientTransceiver> transceivers = new ArrayList<>();
  public static final int CONNECTIONS = 10;
  private static ModelDto dto;
  private static User user = new User();
  private static List<BaseRequest> requestList = new ArrayList<>();

  @BeforeAll
  public static void prepare() {
    for (int i = 0; i < CONNECTIONS; i++) {
      Session clientSession = new ConnectionSession("localhost", 2222);
      clientSession.connect();
      ClientTransceiver clientTransceiver = new ClientTransceiver(clientSession.getSocketChannel());
      transceivers.add(clientTransceiver);
    }
    initUser();
    initDto();
    initRequest();
  }

  private static void initUser() {
    user.setLogin("b");
    user.setPassword("b".getBytes());
  }

  private static void initDto() {
    Coordinates coordinates = new Coordinates(COORD_X, COORD_Y);
    House house = new House(HOUSE_NAME, HOUSE_YEAR, HOUSE_FLOORS, HOUSE_LIFTS);
    dto = new FlatDto(NAME, coordinates, AREA, ROOMS, HAS_FURNITURE, IS_NEW, VIEW, house);
  }

  private static void initRequest() {
    requestList.add(BaseRequest.of(RequestPurpose.CHANGE_COLLECTION, ExecutionPayload.of("add", "", dto, user), user));
    requestList.add(BaseRequest.of(RequestPurpose.INIT_COLLECTION, ExecutionPayload.of("login", "", dto, user), user));
    requestList.add(BaseRequest.of(RequestPurpose.GET_CHANGELIST, 1L, user));
//    requestList.add(BaseRequest.of(RequestPurpose.CHANGE_COLLECTION, ExecutionPayload.of("clear", "2", dto, user), user));
//    requestList.add(BaseRequest.of(RequestPurpose.REGISTER, ExecutionPayload.of("login", "", dto, user), user));
  }

  @Test
  public void sendConcurrentRequestsTest() {
    Set<Thread> threads = new HashSet<>();

    for (int i = 0; i < CONNECTIONS; i++) {
      Transceiver transceiver = transceivers.get(i);
      BaseRequest baseRequest = requestList.get(i % 3);
      threads.add(
          new Thread(() -> {
            try {
              transceiver.send(baseRequest);
            } catch (IOException ignored) {
            }
          }));
    }

    for (Thread thread : threads) {
      thread.start();
    }
  }

  public static final String NAME = "flatName";
  public static final Integer AREA = 12;
  public static final Long ROOMS = 12L;
  public static final boolean HAS_FURNITURE = true;
  public static final boolean IS_NEW = true;
  public static final View VIEW = View.TERRIBLE;
  public static final double COORD_X = 0;
  public static final Integer COORD_Y = 0;
  public static final String HOUSE_NAME = "house12";
  public static final Integer HOUSE_YEAR = 100;
  public static final Integer HOUSE_FLOORS = 10;
  public static final Integer HOUSE_LIFTS = 2;
}