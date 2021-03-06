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
import edu.neu.khoury.cs5500.dijkstraspizza.model.Ingredient;
import edu.neu.khoury.cs5500.dijkstraspizza.model.Pizza;
import edu.neu.khoury.cs5500.dijkstraspizza.model.PizzaSize;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.IngredientRepository;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.PizzaRepository;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.PizzaSizeRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
@WebMvcTest(PizzaController.class)
@ContextConfiguration(classes =
    {TestContext.class, WebApplicationContext.class, PizzaController.class})
@WebAppConfiguration
public class PizzaControllerTest {

  // Set up pizzas and behavior here ===============
  private final Ingredient mushrooms = new Ingredient("mushroom", "vegetable", true);
  private final Ingredient sausage = new Ingredient("sausage", "meat", true);
  private final Ingredient spinach = new Ingredient("spinach", "vegetable", true);
  private final Ingredient ham = new Ingredient("ham", "meat", true);
  private final Double price = 11.68;
  private final Pizza meat = new Pizza();
  private final Pizza veggie = new Pizza();
  PizzaSize size;
  @Autowired
  private WebApplicationContext context;
  @Autowired
  private MockMvc mvc;
  @MockBean
  private PizzaRepository pizzaRepository;
  @MockBean
  private PizzaSizeRepository pizzaSizeRepository;
  @MockBean
  private IngredientRepository ingredientRepository;
  @MockBean
  private Validator<Pizza> validator;
  @Autowired
  private ObjectMapper mapper;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
    mushrooms.setId("mushroomId");
    sausage.setId("sausageId");
    spinach.setId("spinachId");
    ham.setId("hamId");
    veggie.setIngredients(new ArrayList<Ingredient>(Arrays.asList(spinach, mushrooms)));
    meat.setIngredients(new ArrayList<Ingredient>(Arrays.asList(ham, sausage)));
    size = new PizzaSize(12, "small", price);
    size.setId("sizeId");
    meat.setSizeDesc(size);
    veggie.setSizeDesc(size);
    meat.setId("meatId");
    veggie.setId("veggieId");

  }

  @Test
  public void testGetAllPizzasHasNoPizzas() throws Exception {
    Behavior.set(pizzaRepository).hasNoPizzas();
    mvc.perform(get("/pizzas/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("[]"));
  }

  // End setup Pizzas and behavior here======================

  @Test
  public void testGetAllPizzasHasSomePizzas() throws Exception {
    Behavior.set(pizzaRepository).returnPizzas(veggie, meat);
    List<Pizza> pizzas = Arrays.asList(veggie, meat);
    String pizzasContent = mapper.writeValueAsString(pizzas);
    mvc.perform(get("/pizzas/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(pizzasContent));
  }

  @Test
  public void testGetPizzaByIdNoPizzas() throws Exception {
    Behavior.set(pizzaRepository).hasNoPizzas();
    mvc.perform(get("/pizzas/hamId"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testGetPizzaByIdSomePizzasMatch() throws Exception {
    Behavior.set(pizzaRepository).returnPizzas(veggie, meat);
    String pizzasContent = mapper.writeValueAsString(meat);
    mvc.perform(get("/pizzas/meatId"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(pizzasContent));
  }

  @Test
  public void testGetPizzaByIdSomePizzasNoMatch() throws Exception {
    Behavior.set(pizzaRepository).returnPizzas(veggie, meat);
    mvc.perform(get("/pizzas/someId"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testNewPizza() throws Exception {
    meat.setId(null);
    Behavior.set(pizzaRepository, ingredientRepository, pizzaSizeRepository, validator).returnSame()
        .returnIngredients(ham, sausage).returnSizes(size).isValid();
    String pizzaContent = mapper.writeValueAsString(meat);
    mvc.perform(post("/pizzas/")
        .content(pizzaContent)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(pizzaContent));
  }

  @Test
  public void testNewPizzaInvalidSize() throws Exception {
    meat.setId(null);
    Behavior.set(pizzaRepository, ingredientRepository, pizzaSizeRepository, validator)
        .returnSame().hasNoSizes().returnIngredients(ham, sausage).isNotValid();
    String pizzaContent = mapper.writeValueAsString(meat);
    mvc.perform(post("/pizzas/")
        .content(pizzaContent)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testNewPizzaInvalidIngredient() throws Exception {
    meat.setId(null);
    Behavior.set(pizzaRepository, ingredientRepository, pizzaSizeRepository, validator)
        .returnSame().returnSizes(size).returnIngredients(sausage).isNotValid();
    String pizzaContent = mapper.writeValueAsString(meat);
    mvc.perform(post("/pizzas/")
        .content(pizzaContent)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testNewPizzaPreExistingId() throws Exception {
    Behavior.set(pizzaRepository, ingredientRepository, pizzaSizeRepository, validator)
        .returnSame().returnSizes(size).returnIngredients(ham, sausage).isValid();
    String pizzaContent = mapper.writeValueAsString(meat);
    mvc.perform(post("/pizzas/")
        .content(pizzaContent)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testUpdatePizzaByIdNoMatch() throws Exception {
    Behavior.set(pizzaRepository).returnPizzas(veggie);
    String content = mapper.writeValueAsString(meat);
    mvc.perform(put("/pizzas/")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testUpdatePizzaByInvalidIngredient() throws Exception {
    Behavior.set(pizzaRepository, ingredientRepository, pizzaSizeRepository, validator)
        .returnPizzas(veggie, meat).returnSizes(size).hasNoIngredients().isNotValid();
    String content = mapper.writeValueAsString(meat);
    mvc.perform(put("/pizzas/")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testUpdatePizzaByInvalidSize() throws Exception {
    Behavior.set(pizzaRepository, ingredientRepository, pizzaSizeRepository, validator)
        .returnPizzas(veggie, meat).hasNoSizes().returnIngredients(mushrooms, spinach).isNotValid();
    String content = mapper.writeValueAsString(veggie);
    mvc.perform(put("/pizzas/")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testDeletePizzaById() throws Exception {
    Behavior.set(pizzaRepository).returnPizzas(meat, veggie);
    mvc.perform(delete("/pizzas/meatId"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").doesNotExist());
  }

  @Test
  public void testDeletePizzaByIdNoPizzas() throws Exception {
    Behavior.set(pizzaRepository).hasNoPizzas();
    mvc.perform(delete("/pizzas/meatId"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$").doesNotExist());
  }

  private static class Behavior {

    PizzaRepository pizzaRepository;
    IngredientRepository ingredientRepository;
    PizzaSizeRepository pizzaSizeRepository;
    private Validator<Pizza> validator;

    public static Behavior set(PizzaRepository pizzaRepository) {
      Behavior behavior = new Behavior();
      behavior.pizzaRepository = pizzaRepository;
      return behavior;
    }

    public static Behavior set(PizzaRepository pizzaRepository,
        IngredientRepository ingredientRepository,
        PizzaSizeRepository pizzaSizeRepository,
        Validator validator) {
      Behavior behavior = new Behavior();
      behavior.pizzaRepository = pizzaRepository;
      behavior.ingredientRepository = ingredientRepository;
      behavior.pizzaSizeRepository = pizzaSizeRepository;
      behavior.validator = validator;
      return behavior;
    }

    public Behavior hasNoIngredients() {
      when(ingredientRepository.existsById(anyString())).thenReturn(false);
      when(validator.validate(any())).thenReturn(false);
      return this;
    }

    public Behavior hasNoSizes() {
      when(pizzaSizeRepository.existsById(any())).thenReturn(false);
      when(validator.validate(any())).thenReturn(false);
      return this;
    }

    public Behavior returnIngredients(Ingredient... ingredients) {
      when(ingredientRepository.existsById(anyString())).thenAnswer(invocationOnMock -> {
        for (Ingredient ingredient : ingredients) {
          if (ingredient.getId().equals(invocationOnMock.getArguments()[0])) {
            return true;
          }
        }
        return false;
      });
      return this;
    }

    public Behavior returnSizes(PizzaSize... sizes) {
      when(pizzaSizeRepository.existsById(anyString())).thenAnswer(invocationOnMock -> {
        for (PizzaSize size : sizes) {
          if (size.getId().equals(invocationOnMock.getArguments()[0])) {
            return true;
          }
        }
        return false;
      });
      return this;
    }

    public Behavior isValid() {
      when(validator.validate(any())).thenReturn(true);
      return this;
    }

    public Behavior isNotValid() {
      when(validator.validate(any())).thenReturn(false);
      return this;
    }

    public Behavior hasNoPizzas() {
      when(pizzaRepository.findAll()).thenReturn(Collections.emptyList());
      // when(pizzaRepository.findByCategory(anyString())).thenReturn(Collections.emptyList());
      when(pizzaRepository.findById(anyString())).thenReturn(Optional.empty());
      when(pizzaRepository.existsById(anyString())).thenReturn(false);
      return this;
    }

    public Behavior returnSame() {
      when(pizzaRepository.save(any()))
          .thenAnswer(invocation -> invocation.getArguments()[0]);
      return this;
    }

    public Behavior returnPizzas(Pizza... pizzas) {
      when(pizzaRepository.findAll()).thenReturn(Arrays.asList(pizzas));
      when(pizzaRepository.findById(any()))
          .thenAnswer(invocationOnMock -> {
            for (Pizza p : pizzas) {
              if (p.getId().equals(invocationOnMock.getArguments()[0])) {
                return Optional.of(p);
              }
            }
            return Optional.empty();
          });

      when(pizzaRepository.existsById(anyString())).thenAnswer(invocationOnMock -> {
        for (Pizza p : pizzas) {
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
