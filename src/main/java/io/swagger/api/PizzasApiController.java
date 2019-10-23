package io.swagger.api;

import io.swagger.model.Error;
import io.swagger.model.Pizza;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-11T13:23:56.479615-07:00[America/Los_Angeles]")
@Controller
public class PizzasApiController implements PizzasApi {

    private static final Logger log = LoggerFactory.getLogger(PizzasApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public PizzasApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> deletePizzaById(@Min(0)@ApiParam(value = "a pizza's unique ID",required=true, allowableValues="") @PathVariable("pizzaId") Integer pizzaId) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Pizza> getPizzaById(@Min(0)@ApiParam(value = "a pizza's unique ID",required=true, allowableValues="") @PathVariable("pizzaId") Integer pizzaId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Pizza>(objectMapper.readValue("{\n  \"sizeDesc\" : \"Small\",\n  \"price\" : 19.99,\n  \"menusOn\" : [ {\n    \"pizzas\" : [ null, null ],\n    \"storesOffering\" : [ {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    }, {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    } ],\n    \"ingredients\" : [ null, null ],\n    \"id\" : 1\n  }, {\n    \"pizzas\" : [ null, null ],\n    \"storesOffering\" : [ {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    }, {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    } ],\n    \"ingredients\" : [ null, null ],\n    \"id\" : 1\n  } ],\n  \"ingredients\" : [ {\n    \"name\" : \"Sausage\",\n    \"isGlutenFree\" : true,\n    \"id\" : 1,\n    \"category\" : \"Meat\"\n  }, {\n    \"name\" : \"Sausage\",\n    \"isGlutenFree\" : true,\n    \"id\" : 1,\n    \"category\" : \"Meat\"\n  } ],\n  \"id\" : 1,\n  \"sizeInches\" : 11\n}", Pizza.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Pizza>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Pizza>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Pizza> newPizza(@ApiParam(value = "A JSON encoded pizza object" ,required=true )  @Valid @RequestBody Pizza body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Pizza>(objectMapper.readValue("{\n  \"sizeDesc\" : \"Small\",\n  \"price\" : 19.99,\n  \"menusOn\" : [ {\n    \"pizzas\" : [ null, null ],\n    \"storesOffering\" : [ {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    }, {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    } ],\n    \"ingredients\" : [ null, null ],\n    \"id\" : 1\n  }, {\n    \"pizzas\" : [ null, null ],\n    \"storesOffering\" : [ {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    }, {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    } ],\n    \"ingredients\" : [ null, null ],\n    \"id\" : 1\n  } ],\n  \"ingredients\" : [ {\n    \"name\" : \"Sausage\",\n    \"isGlutenFree\" : true,\n    \"id\" : 1,\n    \"category\" : \"Meat\"\n  }, {\n    \"name\" : \"Sausage\",\n    \"isGlutenFree\" : true,\n    \"id\" : 1,\n    \"category\" : \"Meat\"\n  } ],\n  \"id\" : 1,\n  \"sizeInches\" : 11\n}", Pizza.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Pizza>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Pizza>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Pizza> updatePizzaById(@ApiParam(value = "A pizza store with updated information" ,required=true )  @Valid @RequestBody Pizza body,@Min(0)@ApiParam(value = "a pizza's unique ID",required=true, allowableValues="") @PathVariable("pizzaId") Integer pizzaId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Pizza>(objectMapper.readValue("{\n  \"sizeDesc\" : \"Small\",\n  \"price\" : 19.99,\n  \"menusOn\" : [ {\n    \"pizzas\" : [ null, null ],\n    \"storesOffering\" : [ {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    }, {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    } ],\n    \"ingredients\" : [ null, null ],\n    \"id\" : 1\n  }, {\n    \"pizzas\" : [ null, null ],\n    \"storesOffering\" : [ {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    }, {\n      \"specials\" : [ {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      }, {\n        \"offeredAt\" : [ null, null ],\n        \"id\" : 1,\n        \"desc\" : \"50% off two large pizzas\"\n      } ],\n      \"id\" : 1\n    } ],\n    \"ingredients\" : [ null, null ],\n    \"id\" : 1\n  } ],\n  \"ingredients\" : [ {\n    \"name\" : \"Sausage\",\n    \"isGlutenFree\" : true,\n    \"id\" : 1,\n    \"category\" : \"Meat\"\n  }, {\n    \"name\" : \"Sausage\",\n    \"isGlutenFree\" : true,\n    \"id\" : 1,\n    \"category\" : \"Meat\"\n  } ],\n  \"id\" : 1,\n  \"sizeInches\" : 11\n}", Pizza.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Pizza>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Pizza>(HttpStatus.NOT_IMPLEMENTED);
    }

}