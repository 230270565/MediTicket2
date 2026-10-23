/*
 VerifyResetCodeRequest.java

 Verify Reset Code Request DTO

 Author: Joshua Reid Adams (230317693)

 Date: 14th August 2026
*/

package za.ac.cput.dto;

public class VerifyResetCodeRequest {
    private String email;
    private String code;
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
