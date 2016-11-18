package gov.goias.exceptions;

import gov.goias.util.CircuitBreakerOpenException;
import org.apache.log4j.Logger;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author henrique-rh
 * @since 17/07/2014
 */
@ControllerAdvice
public class GlobalExceptionController {

    @Autowired
    HttpServletRequest request;

    private String errorMessage;

    @ModelAttribute("errorModal")
    public String getErrorModal() {
        String error = this.errorModal;
        this.errorModal = null;
        return error;
    }

    public void setErrorModal(String errorModal) {
        this.errorModal = errorModal;
    }

    private String errorModal;

    @ModelAttribute("errorMessage")
    public String getErrorMessage() {
        String error = this.errorMessage;
        this.errorMessage = null;
        return error;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    private static final Logger logger = Logger.getLogger(GlobalExceptionController.class);

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex, HttpServletRequest request) {
        logger.info("O ERRO DO GLOBALEXCEPTIONCONTROLLER ACONTECEU PARA A SESSAO "+request.getSession().getId());
        ModelAndView model = new ModelAndView("errors/500");
        model.addObject("message", ex.getMessage());
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        model.addObject("time", format.format(new Date()));
        logger.info("Uma Exception ocorreu: "+ex.getMessage());
        ex.printStackTrace();
        return model;
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CircuitBreakerOpenException.class)
    public ModelAndView handleException(CircuitBreakerOpenException cex,HttpServletResponse response) throws IOException {
        //todo handler nao estao sendo utilizado
        logger.debug("ENTREI NO HANDLER DE CircuitBreakerOpenException!!!!!!");


        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("status", 400);
        node.put("message", "O serviço est� temporariamente indisponível");
        node.put("modal", false);
        response.getWriter().write(node.toString());
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        logger.debug("to saindo dO HANDLER DE CircuitBreakerOpenException!!!!!!");

        return null;
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(org.springframework.validation.BindException.class)
    public ModelAndView handleModelException(HttpServletRequest request, HttpServletResponse response, Object handler,org.springframework.validation.BindException ex) throws IOException {
        List<FieldError> errors = ((BindingResult) ex.getBindingResult()).getFieldErrors();
        String errorsMessage="";
        for (FieldError error : errors){
            String errorMsg = error.getDefaultMessage();
            if (errorMsg.contains("Exception:"))
                errorMsg = errorMsg.substring(10+errorMsg.indexOf("Exception:"),errorMsg.length());
            if (error.isBindingFailure()){
                errorsMessage+=" Valor inválido em "+error.getField()+": "+errorMsg+";  \n\r";
            }else{
                errorsMessage+=" "+ error.getDefaultMessage()+"; \n\r";
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("status", 400);
        node.put("message", errorsMessage);
        response.getWriter().write(node.toString());
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return null;
    }


    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NFGException.class)
    public ModelAndView handleExpectedException(HttpServletRequest request, HttpServletResponse response, Object handler, NFGException ex) throws IOException {
        Boolean modal = ex.getModal();
        if(ex.getModelAndView() != null) {
            ex.getModelAndView().addObject("errorMessage", ex.getMessage());
            return ex.getModelAndView();
        }

        if(ex.getRequestMapping() != null) {
            setErrorMessage(ex.getMessage());
            RedirectView redirectView = new RedirectView(ex.getRequestMapping(), true);
            redirectView.setExposeModelAttributes(false);
            return new ModelAndView(redirectView);
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("status", 400);
        node.put("message", ex.getMessage());
        node.put("modal", modal);
        response.getWriter().write(node.toString());
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        return null;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class, ResourceNotFoundException.class, NoSuchRequestHandlingMethodException.class})
    public ModelAndView handle404(NoHandlerFoundException ex){
        ModelAndView model = new ModelAndView("errors/404");
        model.addObject("message", ex.getMessage());

        return model;
    }
}
