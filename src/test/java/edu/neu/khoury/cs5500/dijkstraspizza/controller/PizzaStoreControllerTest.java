package edu.neu.khoury.cs5500.dijkstraspizza.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.neu.khoury.cs5500.dijkstraspizza.controller.validator.Validator;
import edu.neu.khoury.cs5500.dijkstraspizza.model.Address;
import edu.neu.khoury.cs5500.dijkstraspizza.model.Ingredient;
import edu.neu.khoury.cs5500.dijkstraspizza.model.Menu;
import edu.neu.khoury.cs5500.dijkstraspizza.model.Pizza;
import edu.neu.khoury.cs5500.dijkstraspizza.model.PizzaStore;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.MenuRepository;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.PizzaRepository;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.PizzaStoreRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(PizzaStoreController.class)
@ContextConfiguration(classes =
    {TestContext.class, WebApplicationContext.class, PizzaStoreController.class,
        MenuController.class, PizzaController.class})
@WebAppConfiguration
// @AutoConfigureMockMvc
public class PizzaStoreControllerTest {

  @MockBean
  Validator<PizzaStore> validator;
  @MockBean
  Validator<Menu> menuValidator;
  @MockBean
  Validator<Pizza> pizzaValidator;
  @Autowired
  private WebApplicationContext context;
  @Autowired
  private MockMvc mvc;
  @MockBean
  private PizzaStoreRepository pizzaStoreRepository;
  @MockBean
  private MenuRepository menuRepository;
  @MockBean
  private PizzaRepository pizzaRepository;
  @Autowired
  private ObjectMapper mapper;

  // Set up pizzas and behavior here ===============
  private Menu vegMenu = new Menu();
  private Menu nonVegMenu = new Menu();
  private Menu glutenFreeMenu = new Menu();
  private Address firstAddr, storeAddr2, storeAddr3;

  private PizzaStore first, store2, store3, store4;

  private Menu regular, glutenFree;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.webAppContextSetup(context).build();

    // Ingredients Setup
    Ingredient spinach = new Ingredient("Spinach", "Veggie", true);
    Ingredient mushroom = new Ingredient("Mushrooms", "Veggie", true);
    Ingredient ham = new Ingredient("Ham", "Meat", true);
    Ingredient sausage = new Ingredient("Sausage", "Meat", true);
    Ingredient pepperoni = new Ingredient("Pepperoni", "Meat", true);
    Ingredient gfDough = new Ingredient("glutenFreeDough", "Crust", true);
    spinach.setId("spinachId");
    mushroom.setId("mushroomsId");
    ham.setId("hamId");
    sausage.setId("sausageId");
    pepperoni.setId("pepperoniId");
    gfDough.setId("glutenFreeDoughId");

    // Pizza Setup
    // spinach
    Pizza spinachPizza = new Pizza();
    spinachPizza.setId("spinachPizza");
    spinachPizza.setIngredients(new ArrayList<>(Collections.singletonList(spinach)));
    // mushroom
    Pizza mushroomPizza = new Pizza();
    mushroomPizza.setId("mushroomPizza");
    mushroomPizza.setIngredients(new ArrayList<>(Collections.singletonList(mushroom)));
    // veggie
    Pizza vegPizza = new Pizza();
    vegPizza.setId("vegPizza");
    vegPizza.setIngredients(new ArrayList<>(Arrays.asList(spinach, mushroom)));
    // ham
    Pizza hamPizza = new Pizza();
    hamPizza.setId("hamPizza");
    hamPizza.setIngredients(new ArrayList<>(Collections.singletonList(ham)));
    // sausage
    Pizza sausagePizza = new Pizza();
    sausagePizza.setId("sausagePizza");
    sausagePizza.setIngredients(new ArrayList<>(Collections.singletonList(sausage)));
    // meat
    Pizza meatPizza = new Pizza();
    meatPizza.setId("meatPizza");
    meatPizza.setIngredients(new ArrayList<>(Arrays.asList(ham, sausage)));
    // gf pizza
    Pizza gfPizza = new Pizza();
    gfPizza.setId("glutenFreePizzaId");
    gfPizza.setIngredients(new ArrayList<>(Arrays.asList(gfDough, pepperoni)));

    vegMenu.setId("vegMenuId");
    vegMenu.setIngredients(new HashSet<>(Arrays.asList(spinach, mushroom)));
    vegMenu.setPizzas(new HashSet<>(Arrays.asList(spinachPizza, mushroomPizza, vegPizza)));

    nonVegMenu.setId("nonVegMenuId");
    nonVegMenu.setIngredients(new HashSet<>(Arrays.asList(ham, sausage)));
    nonVegMenu.setPizzas(new HashSet<>(Arrays.asList(hamPizza, sausagePizza, meatPizza)));

    glutenFreeMenu.setId("glutenFreeMenuId");
    glutenFreeMenu.setIngredients(new HashSet<>(Arrays.asList(gfDough, pepperoni)));
    glutenFreeMenu.setPizzas(new HashSet<>(Collections.singletonList(gfPizza)));

    firstAddr = new Address("6010 15th Ave NW", "Seattle", "WA", "98107");
    storeAddr2 = new Address("601 N 34th St", "Seattle", "WA", "98103");
    storeAddr3 = new Address("1302 6th Ave", "Seattle", "WA", "98101");

    first = new PizzaStore(firstAddr);
    first.setMenu(regular);
    first.setId("firstId");

    store2 = new PizzaStore(storeAddr2);
    store2.setMenu(glutenFree);
    store2.setId("secondId");

    store3 = new PizzaStore(storeAddr3);
    store3.setMenu(glutenFree);
    store4 = new PizzaStore(storeAddr3);
    store4.setMenu(glutenFree);


  }

  @Test
  public void testGetAllStoresNoStores() throws Exception {
    Behavior.set(pizzaStoreRepository).hasNoPizzasStores();
    mvc.perform(get("/stores/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("[]"));

  }

  // End setup Pizzas and behavior here======================

  @Test
  public void getAllStoresSomeStores() throws Exception {
    Behavior.set(pizzaStoreRepository).returnPizzasStores(first, store2);
    String stores = mapper.writeValueAsString(Arrays.asList(first, store2));
    mvc.perform(get("/stores/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(stores));
  }

  @Test
  public void testGetStoreByIdSomeStoresMatch() throws Exception {
    Behavior.set(pizzaStoreRepository).returnPizzasStores(first, store2);
    String stores = mapper.writeValueAsString(first);
    mvc.perform(get("/stores/firstId"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(stores));
  }

  @Test
  public void testGetStoreByIdSomeStoresNoMatch() throws Exception {
    Behavior.set(pizzaStoreRepository).returnPizzasStores(first, store2);
    mvc.perform(get("/stores/Id"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testGetStoreByIdNoStores() throws Exception {
    Behavior.set(pizzaStoreRepository).hasNoPizzasStores();
    mvc.perform(get("/stores/anId"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testNewStore() throws Exception {
    Behavior.set(pizzaStoreRepository, validator).returnSame().isValid();
    String content = mapper.writeValueAsString(new PizzaStore(storeAddr2));
    mvc.perform(post("/stores/")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON_UTF8)).
        andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(content));
  }

  @Test
  public void testNewStoreNotNull() throws Exception {
    Behavior.set(pizzaStoreRepository, validator).returnSame().isValid();
    PizzaStore store = new PizzaStore(storeAddr2);
    store.setId("id");
    String content = mapper.writeValueAsString(store);
    mvc.perform(post("/stores/")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON_UTF8)).
        andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testNewStoreInvalid() throws Exception {
    Behavior.set(pizzaStoreRepository, validator).returnSame().isNotValid();
    PizzaStore store = new PizzaStore(storeAddr2);
    String content = mapper.writeValueAsString(store);
    mvc.perform(post("/stores/")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON_UTF8)).
        andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testUpdateStoreById() throws Exception {
    Behavior.set(pizzaStoreRepository, validator).returnPizzasStores(first, store2).isValid();
    Address addressNew = new Address("test", "test", "test", "test");
    first.setAddress(addressNew);
    String store = mapper.writeValueAsString(first);
    mvc.perform(put("/stores/")
        .content(store)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testUpdateStoreByIdInvalid() throws Exception {
    Behavior.set(pizzaStoreRepository, validator).returnPizzasStores(first, store2).isNotValid();
    Address addressNew = new Address("test", "test", "test", "test");
    first.setAddress(addressNew);
    String store = mapper.writeValueAsString(first);
    mvc.perform(put("/stores/")
        .content(store)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testDeleteStoreById() throws Exception {
    Behavior.set(pizzaStoreRepository).returnPizzasStores(first);
    mvc.perform(delete("/stores/firstId"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void deleteStoreByIdSomeStoresHasMatch() throws Exception {
    store2.setId("secondId");
    Behavior.set(pizzaStoreRepository).returnPizzasStores(first, store2);
    mvc.perform(delete("/stores/secondId"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void deleteStoreByIdSomeStoresHasNoMatch() throws Exception {
    store2.setId("secondId");
    Behavior.set(pizzaStoreRepository).returnPizzasStores(first, store2);
    mvc.perform(delete("/stores/badId"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").doesNotExist());
  }

  private static class Behavior {

    PizzaStoreRepository pizzaStoreRepository;
    Validator<PizzaStore> validator;

    public static Behavior set(PizzaStoreRepository pizzaRepository) {
      Behavior behavior = new Behavior();
      behavior.pizzaStoreRepository = pizzaRepository;
      return behavior;
    }

    public static Behavior set(PizzaStoreRepository pizzaRepository,
        Validator validator) {
      Behavior behavior = new Behavior();
      behavior.pizzaStoreRepository = pizzaRepository;
      behavior.validator = validator;
      return behavior;
    }

    public Behavior isValid() {
      when(validator.validate(any())).thenReturn(true);
      return this;
    }

    public Behavior isNotValid() {
      when(validator.validate(any())).thenReturn(false);
      return this;
    }

    public Behavior hasNoPizzasStores() {
      when(pizzaStoreRepository.findAll()).thenReturn(Collections.emptyList());
      // when(pizzaRepository.findByCategory(anyString())).thenReturn(Collections.emptyList());
      when(pizzaStoreRepository.findById(anyString())).thenReturn(Optional.empty());
      when(pizzaStoreRepository.existsById(anyString())).thenReturn(false);
      return this;
    }

    public Behavior returnSame() {
      when(pizzaStoreRepository.save(any()))
          .thenAnswer(invocation -> invocation.getArguments()[0]);
      return this;
    }

    public Behavior returnPizzasStores(PizzaStore... pizzaStore) {
      when(pizzaStoreRepository.findAll()).thenReturn(Arrays.asList(pizzaStore));
      when(pizzaStoreRepository.findById(any()))
          .thenAnswer(invocationOnMock -> {
            for (PizzaStore p : pizzaStore) {
              if (p.getId().equals(invocationOnMock.getArguments()[0])) {
                return Optional.of(p);
              }
            }
            return Optional.empty();
          });

      when(pizzaStoreRepository.existsById(anyString())).thenAnswer(invocationOnMock -> {
        for (PizzaStore p : pizzaStore) {
          if (p.getId().equals(invocationOnMock.getArguments()[0])) {
            return true;
          }
        }
        return false;
      });
      return this;
    }
  }

}
