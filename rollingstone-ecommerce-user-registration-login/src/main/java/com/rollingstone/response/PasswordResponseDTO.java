package com.rollingstone.response;



/**
 *
 * @author Binit Datta
 */
public class PasswordResponseDTO {
    private boolean result;

    public PasswordResponseDTO() {}

    public PasswordResponseDTO(boolean result) {
		this.result = result;
	}

	public boolean getResult() {
        return result;
    }
 
    public void setResult(boolean result) {
        this.result = result;
    }
 
    
}
