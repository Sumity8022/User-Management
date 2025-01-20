package com.demos.securityconfig;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.demos.entities.MyUser;
import com.demos.entities.Role;
import com.demos.repos.MyUserRepo;

@Service
public class MyUserDetailService implements UserDetailsService {
	
	@Autowired
	private MyUserRepo repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		MyUser myuser = repo.findByEmail(username);//fetch user data from db based on email
		
		
		//check object came or not
		if(myuser!=null)
		{
			List<Role> roles = myuser.getRoles();
			
			//convert this role into SimpleGrantedAuthority Object
			//User()
			
			List<SimpleGrantedAuthority> listOfSimpleGrantedAuthority = roles.stream().map(rol->new SimpleGrantedAuthority(rol.getName())).collect(Collectors.toList());
			
			
			return new User(myuser.getEmail(),myuser.getPassword(),listOfSimpleGrantedAuthority);
			
			
			
		}else
		{
			throw new UsernameNotFoundException("inavlid UserName or PAssword !!!");
		}
		
		
		
	}
	
	
	
	

}
