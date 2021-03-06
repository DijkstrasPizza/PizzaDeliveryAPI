package edu.neu.khoury.cs5500.dijkstraspizza.controller;

import edu.neu.khoury.cs5500.dijkstraspizza.controller.validator.PizzaStoreValidator;
import edu.neu.khoury.cs5500.dijkstraspizza.controller.validator.Validator;
import edu.neu.khoury.cs5500.dijkstraspizza.model.PizzaStore;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.PizzaStoreRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api(value = "stores", tags = {"pizza-store"})
@RestController
@RequestMapping("/stores")
public class PizzaStoreController {

  @Autowired
  private PizzaStoreRepository repository;

  @Autowired
  private Validator<PizzaStore> validator = new PizzaStoreValidator();

  /*===== GET Methods =====*/

  @ApiOperation(
      value = "Gets all stores in the database",
      response = PizzaStore.class,
      responseContainer = "List",
      produces = "application/json"
  )
  @RequestMapping(value = "/", method = RequestMethod.GET)
  public List getAllStores() {
    return repository.findAll();
  }

  @ApiOperation(
      value = "Gets a specific store by ID",
      response = PizzaStore.class,
      produces = "application/json"
  )
  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public PizzaStore getStoreById(
      @ApiParam(value = "ID of the store to return", required = true)
      @PathVariable("id") String id) {
    if (!repository.existsById(id)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Store with id=" + id + " not found.");
    }
    return repository.findById(id).get();
  }

  /*===== POST Methods =====*/

  @ApiOperation(
      value = "Creates a new store in the database",
      notes = "ID is assigned by the database and returned to the caller for further reference. Do not include ID in request.",
      response = PizzaStore.class,
      consumes = "application/json",
      produces = "application/json"
  )
  @RequestMapping(value = "/", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.CREATED)
  public PizzaStore newStore(
      @ApiParam(value = "JSON store object without an id field", required = true)
      @Valid @RequestBody PizzaStore store) {
    if (store.getId() != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "ID must be null."
      );
    }
    if (!validator.validate(store)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid store. All menus, pizzas, sizes, and ingredients must" +
          "be entities in the database."
      );
    }
    repository.save(store);
    return store;
  }

  /*===== PUT Methods =====*/

  @ApiOperation(
      value = "Updates an existing Store based on the ID in the provided object",
      notes = "ID must match an existing store",
      consumes = "application/json"
  )
  @RequestMapping(value = "/", method = RequestMethod.PUT)
  public void updateStoreById(
      @ApiParam(value = "JSON store object with an existing store ID and updated fields as needed", required = true)
      @Valid @RequestBody PizzaStore store) {
    String id = store.getId();
    if (!repository.existsById(id)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Store with id=" + id + " not found.");
    }
    if (!validator.validate(store)) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Invalid store. All menus, pizzas, sizes, and ingredients must" +
          "be entities in the database."
      );
    }
    repository.save(store);
  }

  /*===== DELETE Methods =====*/

  @ApiOperation(
      value = "Deletes a store from the database based on its ID"
  )
  @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
  public void deleteStoreById(
      @ApiParam(value = "ID of the store to delete", required = true)
      @PathVariable("id") String id) {
    if (!repository.existsById(id)) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "Store with id=" + id + " not found.");
    }
    repository.deleteById(id);
  }

}
