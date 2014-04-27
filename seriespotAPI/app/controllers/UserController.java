package controllers;

import play.*;
import play.mvc.*;
import play.db.ebean.*;
import models.User;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import com.fasterxml.jackson.databind.node.ObjectNode;

import play.mvc.Http.RequestBody;

import java.util.*;
import java.io.StringWriter;
import javax.persistence.*;

import util.Wrapper;

import play.data.format.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import util.*;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import play.libs.XPath;

import org.apache.commons.codec.binary.Base64;

import annotations.*;

import static play.libs.Json.toJson;
import static play.mvc.Controller.request;
import static play.mvc.Controller.response;

import com.fasterxml.jackson.core.JsonProcessingException;

import play.data.validation.ValidationError;

@SuppressWarnings("unchecked")
public class UserController extends BaseController{

    public final static String XML_FORMAT = "Application/xml";
    public final static String JSON_FORMAT = "Application/json";

    @With(SecurityController.class)
    public static Result getUser(Integer id) throws javax.xml.bind.JAXBException, JsonProcessingException {

        User user = User.findById(id); 
        if(user == null){
            ErrorMessage error = new ErrorMessage("Not Found", 409, "No user found with ID equal to:'" + id +"'");
            return Results.notFound(error.marshalError());
        }
        return ok(ObjectResponseFormatter.objectResponse(user));     
    }

    @With(SecurityController.class)
    public static Result getAllUsers()  throws javax.xml.bind.JAXBException, JsonProcessingException {

        
        return ok(ObjectResponseFormatter.objectListResponse(User.findAll(), User.class, "/users"));
    }

    @FromXmlTo(User.class)
    @FromJsonTo(User.class)
    public static Result createUser() throws javax.xml.bind.JAXBException, JsonProcessingException{

        User user = bodyRequest(User.class);
        Map<String,String> userData = new HashMap();
        Logger.debug(user.getPassword());

        if(user.getEmailAddress()!= null) userData.put("emailAddress", user.getEmailAddress());
        if(user.getFullName()!= null)     userData.put("fullName", user.getFullName());
        if(user.getPassword()!= null)     userData.put("password", user.getPassword());

        Form<models.User> registerForm = Form.form(models.User.class).bind(userData);

        if (registerForm.hasErrors()) {
            String errorString = "The following errors has been detected: ";
            int i = 0;
            java.util.Map<java.lang.String,java.util.List<ValidationError>> map = registerForm.errors();
            for (Map.Entry<String, java.util.List<ValidationError>> entry : map.entrySet())
            {
                for(ValidationError error : entry.getValue()){

                        errorString = errorString + ++i + ") " + error.toString() + ". ";
                }
            }
            ErrorMessage errorMessage = new ErrorMessage("Bad Request", 400, errorString);
            return badRequest(errorMessage.marshalError());
        }
        // create user
        User userDB = new User(user.getEmailAddress(), user.getPassword(), user.getFullName());
        User.create(userDB);
        response().setHeader(LOCATION, user.getHrefResource());
        return created(ObjectResponseFormatter.objectResponse(User.findById(userDB.id)));

    }

    @FromXmlTo(User.class)
    @FromJsonTo(User.class)
    @With(SecurityController.class)
    public static Result updateUser(Integer id) throws javax.xml.bind.JAXBException, JsonProcessingException{

        User userToUpdate = bodyRequest(User.class);
        User user = User.findById(id); 

        if(user == null){
            ErrorMessage error = new ErrorMessage("Not Found", 409, "No user found with ID equal to:'" + id +"'.");
            return Results.notFound(error.marshalError());
        }
        if(userToUpdate.getEmailAddress()!= null) user.setEmailAddress(userToUpdate.getEmailAddress());
        if(userToUpdate.getFullName()!= null)     user.setFullName(userToUpdate.getFullName());
        if(userToUpdate.getPassword()!= null)     user.setPassword(userToUpdate.getPassword());

        User.update(user);

        return ok(ObjectResponseFormatter.objectResponse(user)); 

    }

    @With(SecurityController.class)
    public static Result deleteUser(Integer id) throws javax.xml.bind.JAXBException, JsonProcessingException{
        User user = User.findById(id); 
        if(user == null){
            ErrorMessage error = new ErrorMessage("Not Found", 409, "No user found with ID equal to:'" + id +"'.");
            return Results.notFound(error.marshalError());
        }
        
        User.deleteById(id);
        return noContent();
    }

}