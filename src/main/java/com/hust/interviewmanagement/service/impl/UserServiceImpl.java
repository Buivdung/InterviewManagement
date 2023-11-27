package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Account;
import com.hust.interviewmanagement.entities.Department;
import com.hust.interviewmanagement.entities.Users;
import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.repository.AccountRepository;
import com.hust.interviewmanagement.repository.DepartmentRepository;
import com.hust.interviewmanagement.repository.UserRepository;
import com.hust.interviewmanagement.service.EmailService;
import com.hust.interviewmanagement.service.FileService;
import com.hust.interviewmanagement.service.UserService;
import com.hust.interviewmanagement.utils.MessageList;
import com.hust.interviewmanagement.web.request.SearchRequest;
import com.hust.interviewmanagement.web.request.UserChangePassword;
import com.hust.interviewmanagement.web.request.UserImport;
import com.hust.interviewmanagement.web.request.UserRequest;
import com.hust.interviewmanagement.web.result.UserSave;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final FileService<UserImport> fileService;
    private final AccountRepository accountRepository;
    private static final String ALPHA = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String ALPHA_UPPERCASE = ALPHA.toUpperCase(); // A-Z
    private static final String DIGITS = "0123456789"; // 0-9
    private static final String ALPHA_NUMERIC = ALPHA + ALPHA_UPPERCASE + DIGITS;
    private static final Random generator = new Random();


    @Override
    public List<Users> findUserByRoleInterviewAndRecruiter() {
        return userRepository.findUsersByAccount_RoleIn(List.of(ERole.ROLE_INTERVIEW, ERole.ROLE_RECRUITER));
    }

    @Override
    public List<Users> findUserByRoleManager() {
        return userRepository.findUsersByAccount_Role(ERole.ROLE_MANAGER);
    }

    @Override
    public List<Users> findUserByRoleInterview() {
        return userRepository.findUsersByAccount_Role(ERole.ROLE_INTERVIEW);
    }

    @Override
    public List<Users> findUserByRoleRecruiter() {
        return userRepository.findUsersByAccount_Role(ERole.ROLE_RECRUITER);
    }

    @Override
    public List<Users> findUserByRoleRecruiterAndManager() {
        return userRepository.findUsersByAccount_RoleIn(List.of(ERole.ROLE_MANAGER, ERole.ROLE_RECRUITER));
    }

    @Override
    @Transactional
    public UserSave saveUser(UserRequest userRequest) throws MessagingException {
        Users users = modelMapper.map(userRequest, Users.class);
        if (accountRepository.existsByEmail(userRequest.getEmail())) {
            return UserSave.builder()
                    .user(null)
                    .message(MessageList.MessageUser.EMAIL)
                    .build();
        }
        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageDepartment.DONT_DEPARTMENT));
        users.setDepartment(department);
        final String password = randomPassword(10);
        Account account = getAccount(userRequest, password);
        account.setUser(users);
        emailService.sendMailToUser(account, password);
        users.setAccount(account);
        return UserSave.builder()
                .user(userRepository.save(users))
                .build();
    }

    @Override
    public UserSave updateUser(UserRequest userRequest) {
        Users users = userRepository.findById(userRequest.getId())
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageUser.DONT_USER));
        if (!Objects.equals(users.getAccount().getEmail(), userRequest.getEmail())
                && accountRepository.existsByEmail(userRequest.getEmail())) {
            return UserSave.builder()
                    .user(null)
                    .message(MessageList.MessageUser.EMAIL)
                    .build();
        }
        modelMapper.map(userRequest, users);
        Department department = departmentRepository.findById(userRequest.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageDepartment.DONT_DEPARTMENT));
        users.setDepartment(department);
        users.getAccount().setEmail(userRequest.getEmail());
        users.getAccount().setRole(userRequest.getRole());
        users.getAccount().setStatus(userRequest.isStatus());
        return UserSave.builder()
                .user(userRepository.save(users))
                .build();
    }

    @Override
    public List<Users> saveAllUser(MultipartFile file) throws MessagingException {
        final String password = randomPassword(10);
        List<Users> users = getUsers(file, password);
        List<String> emails = users.stream().map(u -> u.getAccount().getEmail()).toList();
        if (accountRepository.existsByEmailIn(emails)) {
            return Collections.emptyList();
        }
        for (Users a : users) {
            emailService.sendMailToUser(a.getAccount(), password);
        }
        return userRepository.saveAll(users);
    }

    @Override
    public Page<Users> findAllUser(SearchRequest searchRequest) {
        Pageable pageable = PageRequest
                .of(searchRequest.getPageNumber() - 1, SearchRequest.PAGE_SIZE);
        if (searchRequest.getRole() == null || searchRequest.getRole().name().isEmpty()) {
            return userRepository.findAll(searchRequest.getParam(), pageable);
        }
        return userRepository.findAll(searchRequest.getParam(), searchRequest.getRole(), pageable);
    }

    @Override
    public Users findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(MessageList.MessageUser.DONT_USER));
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Users findUserByAccountId(String email) {
        return userRepository.findByAccount_Email(email).orElseThrow();
    }

    @Override
    public Users changePassword(UserChangePassword userChangePassword) {
        Users users = userRepository.findByAccount_Email(userChangePassword.getEmail()).orElseThrow();
        users.getAccount().setCheckPassword(true);
        users.getAccount().setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
        return userRepository.save(users);
    }

    private int randomNumber(int max) {
        return generator.nextInt(max);
    }

    private String randomPassword(int size) {
        List<String> result = new ArrayList<>();
        Consumer<String> appendChar = s -> {
            int number = randomNumber(s.length());
            result.add("" + s.charAt(number));
        };
        appendChar.accept(DIGITS);
        while (result.size() < size) {
            appendChar.accept(ALPHA_NUMERIC);
        }
        Collections.shuffle(result, generator);
        return String.join("", result);
    }

    private Account getAccount(UserRequest userRequest, String password) {
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(password));
        account.setStatus(userRequest.isStatus());
        account.setCheckPassword(false);
        account.setEmail(userRequest.getEmail());
        account.setRole(userRequest.getRole());
        return account;
    }

    private Account getAccount(UserImport userImport, String password) {
        Account account = new Account();
        account.setPassword(passwordEncoder.encode(password));
        account.setStatus(true);
        account.setCheckPassword(false);
        account.setEmail(userImport.getEmail());
        account.setRole(userImport.getRole());
        return account;
    }

    private List<Users> getUsers(MultipartFile file, String password) {
        List<Users> users = new ArrayList<>();
        List<UserImport> userImports = fileService.importExcel(file, UserImport.class);
        List<Department> departments = departmentRepository.findAll();
        for (UserImport u : userImports) {
            Users user = modelMapper.map(u, Users.class);
            Department department = departments.stream().filter(x -> x.getName().equals(u.getDepartment()))
                    .findFirst().orElseThrow();
            user.setDepartment(department);
            Account account = getAccount(u, password);
            account.setUser(user);
            user.setAccount(account);
            users.add(user);
        }
        return users;
    }

}
