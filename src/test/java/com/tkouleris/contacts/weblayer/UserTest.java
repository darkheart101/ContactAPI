package com.tkouleris.contacts.weblayer;

import com.tkouleris.contacts.dao.IUserRepository;
import com.tkouleris.contacts.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotSame;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UserTest {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    private Helper helper;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @BeforeEach
    public void cleanup(){
        this.helper.cleanAll();
    }

    @Test
    public void registerUser_whenUserIsValid_receiveCreated(){
        // given
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("mypassword");

        // when
        ResponseEntity<Object> response =  testRestTemplate.postForEntity("/api/users/register",user, Object.class);
        List<User> users = (List<User>) this.userRepository.findAll();

        // then
        assertEquals(response.getStatusCode().value(), 201);
        assertEquals(1, users.size());
        assertNotSame(users.get(0).getPassword(), user.getPassword());
    }
}
