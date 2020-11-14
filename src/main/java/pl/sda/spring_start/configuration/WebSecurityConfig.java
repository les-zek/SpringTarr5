package pl.sda.spring_start.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@EnableWebSecurity      // aktywacja metod zabezpieczeń z klasy WebSecurityConfigurerAdapter
@Configuration          // konfiguracja zabezpieczeń aplikacji
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    // http security - determinuje które adresy będą wymagały określonych uprawnień
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()        // uwerzytelniaj poniższe żądania http
                .antMatchers("/addPost").hasAnyAuthority("ROLE_USER")
                .antMatchers("/posts&**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .anyRequest().permitAll()   // każde inne żądanie nie uwierzytelniaj
                .and()
                .csrf().disable()
                .formLogin().loginPage("/login")    // adres dla żądania GET wyświetlający stronę logowania
                .usernameParameter("email")         // nazwa pola w kóre wprowadzamy email (th:name)
                .passwordParameter("password")      // nazwa pola w kóre wprowadzamy password (th:name)
                .loginProcessingUrl("/login_process")   // adres na który wysyłamy dane logowania (th:action)
                .failureUrl("/login&error=true")         // adres na który jesteśmy przekierowania po błędnym logowaniu
                .defaultSuccessUrl("/")             // po porawnym logowaniu przekierowanie na adres /
                .and()
                .logout()                           // wylogowanie
                .logoutUrl("/logout")               // adres do wylogowania
                .logoutSuccessUrl("/");             // przekierowanie po wylogowaniu
    }
    @Autowired
    private DataSource dataSource;
    @Autowired
    private EncoderAlgorithm encoderAlgorithm;
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .usersByUsernameQuery("SELECT u.email, u.password, u.status FROM user u WHERE u.email = ?")
                .authoritiesByUsernameQuery("SELECT u.email, r.role_name FROM user u JOIN user_roles ur ON " +
                        "(u.user_id = ur.user_user_id) JOIN role r ON (r.role_id = ur.roles_role_id) " +
                        "WHERE u.email = ?")
                .dataSource(dataSource)                                 // obiekt przechowujący wynikowy result set
                .passwordEncoder(encoderAlgorithm.getPasswordEncoder());// obiekt do szyfrowania hasła
    }

}