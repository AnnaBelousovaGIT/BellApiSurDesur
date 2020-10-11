package data;

public class RegisterUnSuccess {

    private String error;

    public RegisterUnSuccess(){
    }

    public RegisterUnSuccess(String error){
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
