package home.boottest1.security;
/*
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.beans.factory.annotation.Qualifier;
*/
import home.boottest1.entities.Users;
import home.boottest1.repos.UsersRepository;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service //@Service("userDetailsService")
@Primary
public class UserDetailsServiceImp implements home.boottest1.security.UserDetailsService {

  @Autowired
  private UsersRepository userRepository;

  @Transactional(readOnly = true)
  @Override
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

    Users user = userRepository.findUserByLogin(login);
   
    if (user == null) {throw new UsernameNotFoundException("User not found.");}
    
    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(login)
            .password(user.getPassword())
            .roles(user.getRoleString())  //getRole().toString()
            .build();
  
    return userDetails;       
  }
}
