package edu.neu.khoury.cs5500.dijkstraspizza.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.neu.khoury.cs5500.dijkstraspizza.model.Ingredient;
import edu.neu.khoury.cs5500.dijkstraspizza.repository.IngredientRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(IngredientController.class)
@ContextConfiguration(classes =
    {TestContext.class, WebApplicationContext.class, IngredientController.class})
@WebAppConfiguration
public class IngredientControllerTest {

  @Autowired
  private WebApplicationContext context;

  @Autowired
  private MockMvc mvc;

  @MockBean
  private IngredientRepository ingredientRepository;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mvc = MockMvcBuilders.webAppContextSetup(context).build();
  }

  @Autowired
  private ObjectMapper mapper;

  private final Ingredient mushrooms = new Ingredient("mushroom", "vegetable", true);
  private final Ingredient sausage = new Ingredient("sausage", "meat", true);
  private final Ingredient nonGfCrust = new Ingredient("crust", "crust", false);
  private final Ingredient gFCrust = new Ingredient("glutenFreeCrust", "crust", true);
  private final Ingredient spinach = new Ingredient("spinach", "vegetable", true);
  private final Ingredient ham = new Ingredient("ham", "meat", true);

  private static class Behavior {
    IngredientRepository ingredientRepository;

    public static Behavior set(IngredientRepository ingredientRepository) {
      Behavior behavior = new Behavior();
      behavior.ingredientRepository = ingredientRepository;
      return behavior;
    }

    public Behavior hasNoIngredients() {
      when(ingredientRepository.findAll()).thenReturn(Collections.emptyList());
      when(ingredientRepository.findByCategory(anyString())).thenReturn(Collections.emptyList());
      when(ingredientRepository.findById(anyString())).thenReturn(Optional.empty());
      return this;
    }

    public Behavior returnSame() {
      when(ingredientRepository.save(any()))
          .thenAnswer(invocation -> invocation.getArguments()[0]);
      return this;
    }

    public Behavior returnIngredients(Ingredient... ingredients) {
      when(ingredientRepository.findAll()).thenReturn(Arrays.asList(ingredients));
      when(ingredientRepository.findByCategory(any()))
          .thenAnswer(invocationOnMock -> Arrays.stream(ingredients)
              .filter(ingredient -> ingredient.getCategory()
                  .equals(invocationOnMock.getArguments()[0])).collect(Collectors.toList()));
      for (Ingredient ingredient : ingredients) {
        when(ingredientRepository.findById(ingredient.getId()))
            .thenReturn(Optional.of(ingredient));
        when(ingredientRepository.save(ingredient)).thenReturn(ingredient);
      }
      return this;
    }
  }


  @Test
  public void getAllIngredientsNoIngredients() throws Exception {
    mvc.perform(get("/ingredients/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("[]"));
  }

  @Test
  public void getAllIngredientsSomeIngredients() throws Exception {
    Behavior.set(ingredientRepository).returnIngredients(mushrooms, sausage);
    List<Ingredient> ingredients = Arrays.asList(mushrooms, sausage);
    String ingredientsContent = mapper.writeValueAsString(ingredients);
    mvc.perform(get("/ingredients/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(ingredientsContent));
  }

  @Test
  public void getAllIngredientsOneIngredient() throws Exception {
    Behavior.set(ingredientRepository).returnIngredients(spinach);
    String ingredientsContent = mapper.writeValueAsString(spinach);
    mvc.perform(get("/ingredients/"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(String.format("[%s]", ingredientsContent)));
  }

  @Test
  public void getIngredientsByCategoryNoIngredients() throws Exception {
    Behavior.set(ingredientRepository).hasNoIngredients();
    mvc.perform(get("/ingredients/filter?category=meat"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("[]"));
  }

  @Test
  public void getIngredientsByCategoryOneIngredientOfMany() throws Exception {
    Behavior.set(ingredientRepository).returnIngredients(spinach, mushrooms, ham);
    String hamContent = mapper.writeValueAsString(ham);
    mvc.perform(get("/ingredients/filter?category=meat"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json(String.format("[%s]", hamContent)));
  }

  @Test
  public void getIngredientsByCategoryNoMatch() throws Exception {
    Behavior.set(ingredientRepository).returnIngredients(spinach, mushrooms, ham);
    String hamContent = mapper.writeValueAsString(ham);
    mvc.perform(get("/ingredients/filter?category=jetfuel"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(content().json("[]"));
  }


    @Test
  public void getIngredientById() {
  }

  @Test
  public void newIngredient() {
  }

  @Test
  public void updateIngredientById() {
  }

  @Test
  public void deleteIngredientById() {
  }
}