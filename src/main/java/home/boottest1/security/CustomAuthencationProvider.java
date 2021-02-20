package home.boottest1.security;

import home.boottest1.entities.Users;
import home.boottest1.repos.UsersRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;



@Component
public class CustomAuthencationProvider implements AuthenticationProvider {
    @Autowired private UsersRepository dao;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
  //  @Autowired private UsersRolesInterface usersRolesInterface;
    private static final Logger logger = LogManager.getLogger();

    public void setDao(UsersRepository dao) {this.dao = dao;}

    @Override
    public Authentication authenticate(Authentication authentication) 
                                          throws AuthenticationException {
        String userName = authentication.getName().trim();
        String password = authentication.getCredentials().toString().trim();
        Users users = dao.findUserByLogin(userName);
        System.out.println("========================== USER LOGIN CHK: user "+ userName + " pass " + password);
        if (users == null) {throw new BadCredentialsException("Unknown user "+userName + " or bad password");}
        String pass = users.getPassword().trim();
        if (!passwordEncoder.matches(password, pass) ) {
        	if (password.equals(users.getPassword().trim())) {
        		logger.warn("Password was not encoded. The Pass is being encoded and reset"); 	
        		users.setPassword(passwordEncoder.encode(pass)); dao.save(users);
        	} else {throw new BadCredentialsException("Unknown user "+userName + " or bad password");}            
        }
        
        String [] uroles = users.getRoleString();
      /*  try {uroles = usersRolesInterface.findUserRoles(users); } catch (NullPointerException e) {
        	 uroles = new String[] {"ADMIN"}; 
        	}*/
        System.out.println("========================== USER "+ users.getLogin() + " ROLES CHECK: ");
        for(String x:uroles) {System.out.print(x + ";  ");}
        UserDetails principal = org.springframework.security.core.userdetails.User.builder()
                .username(users.getLogin())
                .password(users.getPassword())
                .roles(uroles)
                .build();

        Authentication authentication1 = new UsernamePasswordAuthenticationToken(
                principal, password, principal.getAuthorities());
        System.out.println("========================== USER authentication1 "+ authentication1);
        // return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
        return authentication1;
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    public void setBcryptEncoder(BCryptPasswordEncoder bcryptEncoder) {
        this.passwordEncoder = bcryptEncoder;
    }
}