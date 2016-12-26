package gov.goias.exceptions;

import org.springframework.web.servlet.ModelAndView;

public class NFGException extends RuntimeException {

    private ModelAndView modelAndView;

    private String requestMapping;

    public Boolean getModal() {
        return modal;
    }

    public void setModal(Boolean modal) {
        this.modal = modal;
    }

    private Boolean modal;
    /**
     * Retorno será json
     * @param message
     */
    public NFGException(String message) {
        super(message);
    }

    public NFGException(String message, Boolean modal) {
        super(message);
        this.modal = modal;
    }

    public NFGException(String message, ModelAndView modelAndView) {
        super(message);
        if(modelAndView != null) {
            modelAndView.addObject("errorMessage", message);
            this.modelAndView = modelAndView;
        }
    }

    public NFGException(String message, String requestMapping) {
        super(message);
        this.requestMapping = requestMapping;
    }

    public ModelAndView getModelAndView() {
        return modelAndView;
    }

    public void setModelAndView(ModelAndView modelAndView) {
        this.modelAndView = modelAndView;
    }

    public String getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(String requestMapping) {
        this.requestMapping = requestMapping;
    }
}
