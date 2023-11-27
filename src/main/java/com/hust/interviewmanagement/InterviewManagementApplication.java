package com.hust.interviewmanagement;

import com.hust.interviewmanagement.entities.*;
import com.hust.interviewmanagement.enums.ERole;
import com.hust.interviewmanagement.enums.EStatus;
import com.hust.interviewmanagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@EnableAsync
public class InterviewManagementApplication implements CommandLineRunner {


    final private SkillRepository skillRepository;
    final private BenefitsRepository benefitsRepository;
    final private LevelRepository levelRepository;
    final private DepartmentRepository departmentRepository;
    final private CandidateRepository candidateRepository;
    final private JobRepository jobRepository;
    final private UserRepository userRepository;
    final private InterviewScheduleRepository interviewScheduleRepository;
    final private PasswordEncoder passwordEncoder;
//    final private SkillJobRepository skillJobRepository;


    public static void main(String[] args) {
        SpringApplication.run(InterviewManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Skill> skills = List.of(Skill.builder().name("Java").build(),
                Skill.builder().name("Nodejs").build(),
                Skill.builder().name(".net").build(),
                Skill.builder().name("C++").build(),
                Skill.builder().name("BA").build(),
                Skill.builder().name("Communication").build()
        );

        List<Benefits> benefits = List.of(
                Benefits.builder().name("Health insurance").build(),
                Benefits.builder().name("Dental insurance").build(),
                Benefits.builder().name("Vision insurance").build(),
                Benefits.builder().name("Life insurance").build(),
                Benefits.builder().name("Paid time off").build(),
                Benefits.builder().name("Mentorship").build(),
                Benefits.builder().name("Travel").build(),
                Benefits.builder().name("Company mission").build()
        );

        List<Department> departments = List.of(
                Department.builder().name("IT").build(),
                Department.builder().name("HR").build(),
                Department.builder().name("Finance").build(),
                Department.builder().name("Communication").build(),
                Department.builder().name("Marketing").build(),
                Department.builder().name("Accounting").build()
        );
        List<Level> levels = List.of(
                Level.builder().name("Fresher 1").build(),
                Level.builder().name("Junior 2.1").build(),
                Level.builder().name("Junior 2.2").build(),
                Level.builder().name("Junior 3.1").build(),
                Level.builder().name("Junior 3.2").build(),
                Level.builder().name("Delivery").build(),
                Level.builder().name("Leader").build(),
                Level.builder().name("Manager").build(),
                Level.builder().name("Vice Head").build()
        );

        List<Job> jobs = List.of(
                Job.builder().title("Back-end ")
                        .salaryFrom(BigDecimal.valueOf(1000000))
                        .salaryTo(BigDecimal.valueOf(2000000))
                        .status(EStatus.OPEN)
                        .workingAddress("Ha Noi")
                        .level(Level.builder().id(1L).build())
                        .build(),
                Job.builder().title("Font-end ")
                        .salaryFrom(BigDecimal.valueOf(1000000))
                        .salaryTo(BigDecimal.valueOf(2000000))
                        .status(EStatus.OPEN)
                        .workingAddress("Ha Noi")
                        .level(Level.builder().id(2L).build())
                        .build(),
                Job.builder().title("BA")
                        .salaryFrom(BigDecimal.valueOf(100000))
                        .salaryTo(BigDecimal.valueOf(200000))
                        .status(EStatus.OPEN)
                        .workingAddress("TP HCM")
                        .level(Level.builder().id(4L).build())
                        .build(),
                Job.builder().title("Full stack")
                        .salaryFrom(BigDecimal.valueOf(1000000))
                        .salaryTo(BigDecimal.valueOf(2000000))
                        .status(EStatus.OPEN)
                        .workingAddress("Da Nang")
                        .level(Level.builder().id(3L).build())
                        .build()
        );

        List<Candidate> candidates = List.of(
                Candidate.builder().fullName("Dung").email("Dung@gmail.com").build(),
                Candidate.builder().fullName("Huy").email("Huy@gmail.com").build(),
                Candidate.builder().fullName("Khai").email("Khai@gmail.com").build()
        );
        List<Users> users = List.of(
                Users.builder().fullName("Dung").account(Account.builder().email("Dungg@gmail.com").password(passwordEncoder.encode("123456")).role(ERole.ROLE_MANAGER).build()).build(),
                Users.builder().fullName("Dung_1").account(Account.builder().email("Dungg1@gmail.com").password(passwordEncoder.encode("123456")).role(ERole.ROLE_RECRUITER).build()).build(),
                Users.builder().fullName("Dung_2").account(Account.builder().email("Dungg2@gmail.com").password(passwordEncoder.encode("123456")).role(ERole.ROLE_INTERVIEW).build()).build(),
                Users.builder().fullName("Dung_3").account(Account.builder().email("Dungg3@gmail.com").password(passwordEncoder.encode("123456")).role(ERole.ROLE_INTERVIEW).build()).build(),
                Users.builder().fullName("Dung_4").account(Account.builder().email("Dungg4@gmail.com").password(passwordEncoder.encode("123456")).role(ERole.ROLE_INTERVIEW).build()).build()
        );

//        skillRepository.saveAll(skills);
//        benefitsRepository.saveAll(benefits);
//        levelRepository.saveAll(levels);
//        departmentRepository.saveAll(departments);
//        candidateRepository.saveAll(candidates);
//        userRepository.saveAll(users);
//        jobRepository.saveAll(jobs);
//        System.out.println(interviewScheduleRepository.findAllBySchedule());
//        System.out.println(interviewScheduleRepository.findAllSchedule());


    }
}
