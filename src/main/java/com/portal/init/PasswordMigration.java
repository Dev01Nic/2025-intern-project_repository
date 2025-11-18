package com.portal.init;

 import com.portal.repository.DepartmentUserRepository;
import com.portal.repository.TechnicalUserRepository;
  import org.springframework.boot.CommandLineRunner;
  import org.springframework.context.annotation.Bean;
  import org.springframework.context.annotation.Configuration;
  import org.springframework.security.crypto.password.PasswordEncoder;
 @Configuration
public class PasswordMigration {

   @Bean
       public CommandLineRunner encodeExistingPasswords(TechnicalUserRepository userRepository,
    		   DepartmentUserRepository departmentUserRepository,
                                         PasswordEncoder passwordEncoder) {
                                                    	    return args -> {
    	     userRepository.findAll().forEach(user -> {
            	     String pw = user.getPassword();
                      if (pw == null) return;
                 
                 // bcrypt hashes start with $2a$, $2b$, or $2y$ and are ~60 chars
                      if (!(pw.startsWith("$2a$") || pw.startsWith("$2b$") || pw.startsWith("$2y$"))) {
                	         String encoded = passwordEncoder.encode(pw);
                            user.setPassword(encoded);
                           userRepository.save(user);
                            System.out.println("Migrated password for user: " + user.getUsername());
                        }
                   });
    	     departmentUserRepository.findAll().forEach(user -> {
                 String pw = user.getPassword();
                 if (pw == null) return;

                 if (!(pw.startsWith("$2a$") || pw.startsWith("$2b$") || pw.startsWith("$2y$"))) {
                     String encoded = passwordEncoder.encode(pw);
                     user.setPassword(encoded);
                     departmentUserRepository.save(user);
                     System.out.println("Migrated password for DepartmentUser: " + user.getUsername());
                 }
             });
                  System.out.println("✅ Password migration complete. You can remove this runner after first run.");
              };
         }
                                                      }
