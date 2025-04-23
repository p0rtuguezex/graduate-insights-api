package pe.com.graduate.insights.api.infrastructure.controller.impl;

import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.graduate.insights.api.application.ports.input.UserUseCase;
import pe.com.graduate.insights.api.domain.models.request.UserRequest;
import pe.com.graduate.insights.api.domain.models.response.User;
import pe.com.graduate.insights.api.domain.utils.ConstantsUtils;

@Slf4j
@RestController
@RequestMapping("/User")
public class UserController {

  @Autowired private UserUseCase userUseCase;

  // definir los endpoint respectivos

  @GetMapping("/{id}")
  public ResponseEntity<User> getUser(@PathVariable Long id) {
    return userUseCase
        .getDomain(id)
        .map(User -> ResponseEntity.ok(User))
        .orElseGet(() -> ResponseEntity.notFound().build());
  }

    @GetMapping("/list")
    public ResponseEntity<List<User>> getList() {
        return ResponseEntity.ok(userUseCase.getList());
    }


    @PostMapping("")
  public ResponseEntity<Void> saveUser(@Valid @RequestBody UserRequest userRequest) {
    userUseCase.save(userRequest);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateUser(
      @Valid @RequestBody UserRequest userRequest, @PathVariable Long id) {
    userUseCase.update(userRequest, id);
    return ResponseEntity.status(HttpStatus.ACCEPTED).build();
  }



  @DeleteMapping("/{id}")
  public  ResponseEntity<String> deleteUser(@PathVariable Long id){
    userUseCase.delete(id);
    return ResponseEntity.ok(ConstantsUtils.DELETE_OK);
  }

  @GetMapping("/listPageable")
  public ResponseEntity<List<User>> getListPageable(
          @RequestParam(value = "search",defaultValue = "") String search,
          @RequestParam(value = "page",defaultValue = "1") String page,
          @RequestParam(value = "size",defaultValue = "10") String size){
    Pageable pageable= PageRequest.of(Integer.parseInt(page)-1,Integer.parseInt(size));
    var userPage=userUseCase.getPagination(search,pageable);
    return ResponseEntity.ok(userPage.getContent());

  }


}
