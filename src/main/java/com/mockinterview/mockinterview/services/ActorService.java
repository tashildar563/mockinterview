package com.mockinterview.mockinterview.services;

import com.google.gson.Gson;
import com.mockinterview.mockinterview.Actor;
import com.mockinterview.mockinterview.CoreConstant;
import com.mockinterview.mockinterview.CoreConstant.Status;
import com.mockinterview.mockinterview.SprintAI.UserService;
import com.mockinterview.mockinterview.SprintAI.entities.User;
import com.mockinterview.mockinterview.UtilityMethods;
import com.mockinterview.mockinterview.entities.ApiResponse;
import com.mockinterview.mockinterview.entities.Candidate;
import com.mockinterview.mockinterview.entities.Employee;
import com.mockinterview.mockinterview.entities.SessionStore;
import com.mockinterview.mockinterview.repositories.ActorRepositories;
import com.mockinterview.mockinterview.repositories.CandidateRepository;
import com.mockinterview.mockinterview.repositories.EmployeeRepository;
import com.mockinterview.mockinterview.repositories.SessionStoreRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ActorService implements UserDetailsService {

  @Autowired
  ActorRepositories actorRepositories;
  @Autowired
  SessionStoreRepository sessionStoreRepository;

  @Autowired
  @Lazy
  AuthenticationManager authenticationManager;
  @Autowired
  JwtUtil jwtUtil;
  @Autowired
  CandidateRepository candidateRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
  private KeyService keyService;

  public Actor createActor(Map<String, Object> request) {
    String role = request.get("roleCode").toString();
    String password = request.get(CoreConstant.PASSWORD_L_CASE).toString();
    String email = request.get(CoreConstant.EMAIL_L_CASE).toString();
    switch (role.toUpperCase()) {
      case "EMPLOYEE":
        Employee emp = new Employee();
        emp.setRoleCode(role);
        emp.setName(UtilityMethods.stringOf(request.get(CoreConstant.NAME_L_CASE)));
        emp.setEmail(UtilityMethods.stringOf(request.get(CoreConstant.EMAIL_L_CASE)));
        emp.setMobileNumber(UtilityMethods.stringOf(request.get("phone")));
        emp.setPassword(passwordEncoder.encode(password));
        emp.setCreatedOn(System.currentTimeMillis());
        emp.setUpdatedOn(System.currentTimeMillis());
        emp.setCreatedBy("System");
        emp.setUpdatedBy("System");
        emp.setActive(true);
        return emp;

      case CoreConstant.CANDIDATE_U_CASE:
        Candidate cand = candidateRepository.findByEmailAndIsActive(email, true);
        if (cand == null) {
          cand = new Candidate();
          cand.setRoleCode(role);
          cand.setName(UtilityMethods.stringOf(request.get(CoreConstant.NAME_L_CASE)));
          cand.setEmail(UtilityMethods.stringOf(request.get(CoreConstant.EMAIL_L_CASE)));
          cand.setMobileNumber(UtilityMethods.stringOf(request.get("phone")));
          cand.setPassword(passwordEncoder.encode(password));
          cand.setCreatedOn(System.currentTimeMillis());
          cand.setUpdatedOn(System.currentTimeMillis());
          cand.setCreatedBy("System");
          cand.setUpdatedBy("System");
          cand.setActive(true);
          return cand;
        } else {
          return null;
        }

      default:
        throw new IllegalArgumentException("Invalid role: " + role);
    }
  }
  @Autowired
  private UserService userService;
  public ResponseEntity<ApiResponse> login(Map<String, Object> credential) throws Exception {
    long currentTimeMillis = System.currentTimeMillis();
    PrivateKey privateKey = keyService.getPrivateKey();
    String payload = KeyService.decrypt((String) credential.get("payload"), privateKey);
    Gson gson = new Gson();
    Map<String, Object> map = gson.fromJson(payload, Map.class);
    String email = UtilityMethods.stringOf(map.get(CoreConstant.EMAIL_L_CASE));
    Map<String, Object> userMap = new HashMap<>();
    String roleCode = UtilityMethods.stringOf(map.get("roleCode"));
    Optional<User> user = null;
    if (roleCode.equals(CoreConstant.CANDIDATE_U_CASE)) {
      user = userService.findByEmail(email);
//      actor = candidateRepository.findByEmailAndIsActive(email, true);
      userMap.put(CoreConstant.ID_L_CASE, user.get().getId());
      userMap.put(CoreConstant.NAME_L_CASE, user.get().getName());
    }
    if (!user.isPresent()) {
      throw new UsernameNotFoundException("user not found");
    }
    SessionStore sessionStore = sessionStoreRepository.findByEmailAndExpireAtGreaterThanAndValid(
        email,
        currentTimeMillis, true);
    if (sessionStore != null) {
      sessionStore.setValid(false);
      sessionStore.setUpdatedBy(user.get().getId());
      sessionStore.setUpdatedOn(currentTimeMillis);
      sessionStore.setActive(false);
      sessionStoreRepository.save(sessionStore);
      return ResponseEntity
          .status(HttpStatus.CONFLICT)
          .body(new ApiResponse<>(
              "FAIL",
              "It looks like you're already logged in elsewhere, logout from your other session to continue.",
              null
          ));

    }
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            user.get().getEmail(), UtilityMethods.stringOf(map.get(CoreConstant.PASSWORD_L_CASE))
        )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtUtil.generateToken(user.get().getEmail(), String.valueOf(user.get().getId()),
        user.get().getRole().name());

    Claims claims = Jwts.parser()
        .setSigningKey(jwtUtil.getSecretKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    sessionStore = new SessionStore(email, token, (Long) claims.get(CoreConstant.ISSUED_AT_L_CASE),
        (Long) claims.get(CoreConstant.EXPIRE_AT_L_CASE));
    sessionStore.setCreatedBy(String.valueOf(user.get().getId()));
    sessionStore.setUpdatedBy(String.valueOf(user.get().getId()));
    sessionStore.setCreatedOn(currentTimeMillis);
    sessionStore.setUpdatedOn(currentTimeMillis);
    sessionStore.setActive(true);
    sessionStore = sessionStoreRepository.save(sessionStore);
    map.put(CoreConstant.TOKEN_L_CASE, sessionStore.getToken());
    map.remove(CoreConstant.EMAIL_L_CASE);
    map.remove(CoreConstant.PASSWORD_L_CASE);
    map.put("user", user);
    return ResponseEntity.ok(
        new ApiResponse<>("SUCCESS", "Actor Logged in Successful", map));
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Actor actorEntity = candidateRepository.findByemail(username);
    if (actorEntity == null) {
      actorEntity = employeeRepository.findByemail(username);
    }
    if (actorEntity != null) {
      return new org.springframework.security.core.userdetails.User(
          actorEntity.getEmail(),
          actorEntity.getPassword(),
          Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))
      );
    }
    throw new UsernameNotFoundException("User Not found");
  }

  public Map<String, Object> getActorDetails(String id, String roleCode) {
    Map<String, Object> actor = new HashMap<>();
    if (roleCode.equals(CoreConstant.CANDIDATE_U_CASE)) {
      Optional<Candidate> candidate = candidateRepository.findById(new ObjectId(id));
      actor.put(CoreConstant.NAME_L_CASE, candidate.get().getName());
      actor.put(CoreConstant.EMAIL_L_CASE, candidate.get().getEmail());
      actor.put(CoreConstant.MOBILE_NUM_L_CASE, candidate.get().getMobileNumber());
      actor.put(CoreConstant.ROLE_CODE_L_CASE, candidate.get().getRoleCode());
      actor.put(CoreConstant.CODE_L_CASE, candidate.get().getCode());
    } else if (roleCode.equals("EMPLOYEE")) {
      Optional<Employee> employee = employeeRepository.findById(new ObjectId(id));
      actor.put(CoreConstant.NAME_L_CASE, employee.get().getName());
      actor.put(CoreConstant.EMAIL_L_CASE, employee.get().getEmail());
      actor.put(CoreConstant.MOBILE_NUM_L_CASE, employee.get().getMobileNumber());
      actor.put(CoreConstant.ROLE_CODE_L_CASE, employee.get().getRoleCode());
      actor.put(CoreConstant.CODE_L_CASE, employee.get().getCode());
    }
    return actor;
  }

  public Actor deleteAnActor(String id, String roleCode) {
    if (roleCode.equals(CoreConstant.CANDIDATE_U_CASE)) {
      Optional<Candidate> candidate = candidateRepository.findById(new ObjectId(id));
      candidate.get().setActive(false);
      return candidate.get();
    } else if (roleCode.equals("EMPLOYEE")) {
      Optional<Employee> employee = employeeRepository.findById(new ObjectId(id));
      employee.get().setActive(false);
      return employee.get();
    }
    throw new IllegalArgumentException("Invalid Credentials");
  }

  public Actor resetPassword(String email, String password) {
    if (email != null) {
      Candidate candidate = candidateRepository.findByEmailAndIsActive(email, true);
      if (candidate != null) {
        String encodedString = passwordEncoder.encode(password);
        candidate.setPassword(encodedString);
        candidate.setUpdatedBy("System");
        candidate.setUpdatedOn(System.currentTimeMillis());
        return candidate;
      }
    }
    return null;
  }

  public ResponseEntity<ApiResponse> logout(String token) {
    SessionStore sessionStore = sessionStoreRepository.findByTokenAndValid(token.trim(), true);
    if (sessionStore != null) {
      sessionStore.setValid(false);
      sessionStore.setActive(false);
      sessionStoreRepository.save(sessionStore);
    }
    return ResponseEntity.ok(
        new ApiResponse<>(Status.SUCCESS_U_CASE, "Actor Logged out", new HashMap()));
  }
}
