package br.com.fiap.feign;

import com.fiap.user_service.users.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("api/v1/users/{userId}")
    UserDTO getUser(@PathVariable final Long userId);

}
